<template>
  <div class="news-container">
    <div class="news-header">
      <h1>新闻中心</h1>
      <div class="news-tabs">
        <div
          v-for="(tab, index) in tabs"
          :key="index"
          :class="['tab', { active: activeTab === index }]"
          @click="activeTab = index"
        >
          {{ tab }}
        </div>
      </div>
    </div>

    <div class="news-grid" v-infinite-scroll="loadMorePosts" :infinite-scroll-disabled="loading || !hasMorePosts" infinite-scroll-distance="10">
      <div
        v-for="(item, index) in filteredNews"
        :key="item.id || index"
        class="news-item"
        @click="viewNewsDetail(item)"
      >
        <div class="news-image">
          <img :src="item.coverUrl || getRandomImage(index)" alt="News Image">
        </div>
        <div class="news-content">
          <div class="news-date">{{ item.date }}</div>
          <div class="news-title">{{ item.title }}</div>
          <div class="news-summary" v-if="item.summary">{{ item.summary }}</div>
        </div>
      </div>

      <!-- Loading and No More Data indicators -->
      <div v-if="loading" class="loading-indicator">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      <div v-if="!loading && !hasMorePosts && filteredNews.length > 0" class="no-more-data">
        没有更多数据了
      </div>
      <div v-if="!loading && filteredNews.length === 0" class="no-data">
        暂无数据
      </div>
    </div>

    <!-- News Detail Modal -->
    <el-dialog
      v-model="showNewsDetail"
      :title="selectedNews ? selectedNews.title : ''"
      width="70%"
      class="news-detail-dialog"
      destroy-on-close
    >
      <div class="news-detail-content" v-if="selectedNews">
        <div class="news-detail-header">
          <div class="news-detail-date">{{ selectedNews.date }}</div>
          <div class="news-detail-source">来源: 实验室新闻中心</div>
        </div>

        <!-- 如果有媒体列表，显示第一张图片或视频 -->
        <div v-if="selectedNews.mediaList && selectedNews.mediaList.length > 0" class="news-detail-media">
          <template v-for="(media, index) in selectedNews.mediaList" :key="index">
            <div v-if="index === 0" class="news-detail-image">
              <img v-if="media.mediaType === 0" :src="media.mediaUrl" alt="News Media">
              <video v-else-if="media.mediaType === 1" controls :src="media.mediaUrl" :poster="media.coverUrl"></video>
            </div>
          </template>
        </div>
        <!-- 如果没有媒体列表，显示封面图 -->
        <div v-else-if="selectedNews.coverUrl" class="news-detail-image">
          <img :src="selectedNews.coverUrl" alt="News Cover Image">
        </div>
        <!-- 如果既没有媒体列表也没有封面图，显示随机图片 -->
        <div v-else class="news-detail-image">
          <img :src="getRandomImage(selectedNews.id || 0)" alt="News Detail Image">
        </div>

        <div class="news-detail-text">
          <p>{{ selectedNews.content }}</p>
        </div>

        <!-- 如果有多张图片，显示图片列表 -->
        <div v-if="selectedNews.mediaList && selectedNews.mediaList.length > 1" class="news-detail-gallery">
          <h3>相关图片</h3>
          <div class="gallery-grid">
            <div v-for="(media, index) in selectedNews.mediaList.slice(1)" :key="index" class="gallery-item">
              <img v-if="media.mediaType === 0" :src="media.mediaUrl" alt="Gallery Image">
              <video v-else-if="media.mediaType === 1" controls :src="media.mediaUrl" :poster="media.coverUrl"></video>
            </div>
          </div>
        </div>

        <div class="news-detail-footer">
          <div class="news-detail-tags">
            <span class="tag">{{ tabs[activeTab] }}</span>
            <span class="tag">实验室动态</span>
          </div>

          <div class="news-detail-actions">
            <div class="action-button">
              <i class="el-icon-share"></i>
              <span>分享</span>
            </div>
            <div class="action-button">
              <i class="el-icon-star-off"></i>
              <span>收藏</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElLoading, ElMessage } from 'element-plus';
import { getPosts, getPostDetail } from '../api/post';

const route = useRoute();
const router = useRouter();
const activeTab = ref(0);
const tabs = ['全部', '新闻动态', '通知公告', '学术动态'];
const showNewsDetail = ref(false);
const selectedNews = ref(null);
const newsItems = ref([]);
const loading = ref(false);
const lastPostId = ref(0);
const hasMorePosts = ref(true);

// 根据URL参数设置初始标签页和显示详情
onMounted(async () => {
  const type = route.query.type;
  const newsId = route.query.id;

  if (type === 'notice') {
    activeTab.value = 2; // 通知公告
  } else if (type === 'academic') {
    activeTab.value = 3; // 学术动态
  } else if (type === 'news') {
    activeTab.value = 1; // 新闻动态
  }

  // 加载新闻列表
  await loadPosts();

  // 如果有newsId参数，显示对应的新闻详情
  if (newsId) {
    try {
      const postId = parseInt(newsId);
      if (!isNaN(postId)) {
        await loadPostDetail(postId);
      }
    } catch (error) {
      console.error('Failed to load post detail:', error);
      ElMessage.error('加载新闻详情失败');
    }
  }
});

// 监听标签页变化，重新加载数据
watch(activeTab, async () => {
  newsItems.value = [];
  lastPostId.value = 0;
  hasMorePosts.value = true;
  await loadPosts();
});

// 加载帖子列表
const loadPosts = async () => {
  if (loading.value || !hasMorePosts.value) return;

  loading.value = true;
  const loadingInstance = ElLoading.service({
    target: '.news-grid',
    text: '加载中...'
  });

  try {
    // 根据当前标签页获取对应分类的帖子
    const category = getCategoryFromTab();
    const response = await getPosts(category, lastPostId.value);

    if (response.code === 200 && response.data) {
      const posts = response.data.postVOList || [];

      if (posts.length === 0) {
        hasMorePosts.value = false;
      } else {
        // 转换帖子数据格式
        const formattedPosts = posts.map(post => ({
          id: post.id,
          date: formatDate(post.createdAt),
          title: post.title, // 使用内容的第一行作为标题
          content: post.content,
          type: getCategoryType(post.category),
          summary: post.content,
          coverUrl: post.coverUrl || getRandomImage(post.id),
          mediaList: post.mediaVOList || []
        }));

        newsItems.value = [...newsItems.value, ...formattedPosts];
        lastPostId.value = response.data.lastPostId;
      }
    } else {
      ElMessage.error('获取新闻列表失败');
    }
  } catch (error) {
    console.error('Failed to load posts:', error);
    ElMessage.error('获取新闻列表失败');
  } finally {
    loading.value = false;
    loadingInstance.close();
  }
};

// 加载帖子详情
const loadPostDetail = async (postId) => {
  try {
    const response = await getPostDetail(postId);

    if (response.code === 200 && response.data) {
      const post = response.data;

      selectedNews.value = {
        id: post.id,
        date: formatDate(post.createdAt),
        title: post.content.split('\n')[0] || '无标题',
        content: post.content,
        type: getCategoryType(post.category),
        coverUrl: post.coverUrl || getRandomImage(post.id),
        mediaList: post.mediaVOList || []
      };

      showNewsDetail.value = true;
    } else {
      ElMessage.error('获取新闻详情失败');
    }
  } catch (error) {
    console.error('Failed to load post detail:', error);
    ElMessage.error('获取新闻详情失败');
  }
};

// 根据标签页获取分类ID
const getCategoryFromTab = () => {
  if (activeTab.value === 0) return null; // 全部

  const categoryMap = {
    1: 0, // 新闻动态
    2: 1, // 通知公告
    3: 2  // 学术动态
  };

  return categoryMap[activeTab.value];
};

// 根据分类ID获取类型
const getCategoryType = (category) => {
  const typeMap = {
    0: 'news',     // 新闻动态
    1: 'notice',   // 通知公告
    2: 'academic'  // 学术动态
  };

  return typeMap[category] || 'news';
};


// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '';

  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN').replace(/\//g, '-');
};

// 根据当前标签页筛选新闻
const filteredNews = computed(() => {
  if (activeTab.value === 0) {
    return newsItems.value;
  } else {
    const typeMap = {
      1: 'news',
      2: 'notice',
      3: 'academic'
    };
    return newsItems.value.filter(item => item.type === typeMap[activeTab.value]);
  }
});

// 获取随机图片
const getRandomImage = (index) => {
  const imageId = (typeof index === 'number' ? index : 0) % 30 + 10;
  return `https://picsum.photos/id/${imageId}/800/400`;
};

// 查看新闻详情
const viewNewsDetail = async (news) => {
  if (news.id) {
    // 更新URL，但不重新加载页面
    router.replace({
      path: route.path,
      query: { ...route.query, id: news.id }
    }).catch(err => {
      if (err.name !== 'NavigationDuplicated') {
        console.error('Navigation error:', err);
      }
    });

    await loadPostDetail(news.id);
  } else {
    selectedNews.value = news;
    showNewsDetail.value = true;
  }
};
</script>

<style scoped>
.news-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding: 20px;
  padding-top: 80px; /* Space for fixed navigation */
}

.news-header {
  max-width: 1200px;
  margin: 0 auto 30px;
  text-align: center;
}

.news-header h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: #58a6ff;
}

.news-tabs {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 30px;
}

.tab {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #21262d;
}

.tab:hover {
  background-color: #30363d;
}

.tab.active {
  background-color: #58a6ff;
  color: #0d1117;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.news-item {
  background-color: #161b22;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.news-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.news-image {
  height: 180px;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.news-item:hover .news-image img {
  transform: scale(1.05);
}

.news-content {
  padding: 15px;
}

.news-date {
  font-size: 0.8rem;
  color: #8b949e;
  margin-bottom: 5px;
}

.news-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin-bottom: 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 0.9rem;
  color: #8b949e;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* News Detail Dialog Styles */
:deep(.news-detail-dialog .el-dialog) {
  background-color: #161b22;
  border-radius: 8px;
  color: #e6edf3;
}

:deep(.news-detail-dialog .el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #30363d;
}

:deep(.news-detail-dialog .el-dialog__title) {
  color: #58a6ff;
  font-size: 1.5rem;
}

:deep(.news-detail-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: #8b949e;
}

:deep(.news-detail-dialog .el-dialog__body) {
  padding: 20px;
}

.news-detail-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  color: #8b949e;
  font-size: 0.9rem;
}

.news-detail-image {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.news-detail-image img {
  width: 100%;
  height: auto;
}

.news-detail-text {
  line-height: 1.8;
  margin-bottom: 30px;
  white-space: pre-line;
}

.news-detail-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 1px solid #30363d;
}

.news-detail-tags {
  display: flex;
  gap: 10px;
}

.tag {
  background-color: #21262d;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.news-detail-actions {
  display: flex;
  gap: 15px;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #8b949e;
  transition: color 0.3s;
}

.action-button:hover {
  color: #58a6ff;
}
</style>