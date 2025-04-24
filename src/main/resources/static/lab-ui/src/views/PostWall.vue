<template>
  <div class="post-wall-container">
    <div class="post-wall-header">
      <h1>在线交流</h1>
      <p>分享研究成果，交流学术见解</p>
      
      <div class="category-filter">
        <el-radio-group v-model="selectedCategory" @change="handleCategoryChange">
          <el-radio-button :label="null">全部</el-radio-button>
          <el-radio-button :label="0">新闻</el-radio-button>
          <el-radio-button :label="1">设备</el-radio-button>
          <el-radio-button :label="2">师生</el-radio-button>
          <el-radio-button :label="3">生活</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div class="post-grid">
      <div v-for="post in posts" :key="post.id" class="post-card" @click="viewPostDetail(post.id)">
        <div class="post-image">
          <img :src="post.coverUrl" :alt="post.content" />
        </div>
        <div class="post-content">
          <p class="post-text">{{ post.content }}</p>
          <div class="post-info">
            <div class="user-info">
              <img :src="post.userBasicVO.image" :alt="post.userBasicVO.username" class="user-avatar" />
              <span class="username">{{ post.userBasicVO.username }}</span>
            </div>
            <div class="post-stats">
              <span class="like-count">
                <i class="el-icon-star-on"></i> {{ post.likeCount }}
              </span>
              <span class="date">{{ formatDate(post.createdAt) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="load-more" v-if="hasMorePosts">
      <el-button @click="loadMorePosts" :loading="loading">加载更多</el-button>
    </div>

    <div class="create-post-fab" @click="goToCreatePost" v-if="isLoggedIn">
      <el-button type="primary" circle>
        <i class="el-icon-plus"></i>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

// Fake data for posts
const fakePosts = [
  {
    id: 1,
    content: '人工智能研究团队最新研究成果：基于深度学习的图像识别系统取得突破性进展',
    coverUrl: 'https://picsum.photos/id/1/600/400',
    userBasicVO: {
      username: '张教授',
      image: 'https://randomuser.me/api/portraits/men/1.jpg'
    },
    likeCount: 42,
    createdAt: new Date('2023-11-15')
  },
  {
    id: 2,
    content: '实验室新设备展示：最新的高性能计算集群已投入使用，将大幅提升我们的研究能力',
    coverUrl: 'https://picsum.photos/id/2/600/400',
    userBasicVO: {
      username: '李研究员',
      image: 'https://randomuser.me/api/portraits/women/2.jpg'
    },
    likeCount: 38,
    createdAt: new Date('2023-11-10')
  },
  {
    id: 3,
    content: '学术讲座预告：下周将举办"人工智能与未来社会"主题讲座，欢迎各位师生参加',
    coverUrl: 'https://picsum.photos/id/3/600/400',
    userBasicVO: {
      username: '王助理',
      image: 'https://randomuser.me/api/portraits/men/3.jpg'
    },
    likeCount: 27,
    createdAt: new Date('2023-11-05')
  },
  {
    id: 4,
    content: '实验室团建活动回顾：上周末的户外拓展活动增强了团队凝聚力，感谢所有参与者',
    coverUrl: 'https://picsum.photos/id/4/600/400',
    userBasicVO: {
      username: '赵博士',
      image: 'https://randomuser.me/api/portraits/women/4.jpg'
    },
    likeCount: 56,
    createdAt: new Date('2023-10-30')
  },
  {
    id: 5,
    content: '研究生招生信息：2024年研究生招生简章已发布，欢迎有志于人工智能研究的同学报考',
    coverUrl: 'https://picsum.photos/id/5/600/400',
    userBasicVO: {
      username: '刘教授',
      image: 'https://randomuser.me/api/portraits/men/5.jpg'
    },
    likeCount: 63,
    createdAt: new Date('2023-10-25')
  },
  {
    id: 6,
    content: '论文发表喜讯：我实验室三篇论文被顶级会议CVPR 2023接收，祝贺相关研究团队',
    coverUrl: 'https://picsum.photos/id/6/600/400',
    userBasicVO: {
      username: '陈研究员',
      image: 'https://randomuser.me/api/portraits/women/6.jpg'
    },
    likeCount: 85,
    createdAt: new Date('2023-10-20')
  }
];

// Reactive state
const posts = ref([...fakePosts]);
const loading = ref(false);
const hasMorePosts = ref(true);
const selectedCategory = ref(null);
const isLoggedIn = ref(!!localStorage.getItem('token'));
const router = useRouter();

// Load initial posts
onMounted(() => {
  // In a real app, we would fetch posts from the API
  // For now, we're using fake data
});

// Format date for display
const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('zh-CN');
};

// Handle category change
const handleCategoryChange = (category) => {
  // In a real app, we would fetch posts by category
  // For now, we're just filtering the fake data
  if (category === null) {
    posts.value = [...fakePosts];
  } else {
    // Simulate filtering by randomly selecting a subset of posts
    posts.value = fakePosts.filter(() => Math.random() > 0.5);
  }
};

// Load more posts
const loadMorePosts = () => {
  loading.value = true;
  
  // Simulate API call delay
  setTimeout(() => {
    // Add more fake posts
    const morePosts = fakePosts.map(post => ({
      ...post,
      id: post.id + 100, // Ensure unique IDs
      content: `${post.content} (更多内容)`,
    }));
    
    posts.value = [...posts.value, ...morePosts];
    
    // After loading a few batches, indicate no more posts
    if (posts.value.length > 15) {
      hasMorePosts.value = false;
    }
    
    loading.value = false;
  }, 1000);
};

// View post detail
const viewPostDetail = (postId) => {
  router.push(`/post/detail/${postId}`);
};

// Go to create post page
const goToCreatePost = () => {
  router.push('/post/create');
};
</script>

<style scoped>
.post-wall-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding: 20px;
  padding-top: 80px; /* Space for fixed navigation */
}

.post-wall-header {
  text-align: center;
  margin-bottom: 40px;
}

.post-wall-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #fff;
}

.post-wall-header p {
  font-size: 1.2rem;
  color: #8b949e;
  margin-bottom: 20px;
}

.category-filter {
  margin: 20px 0;
}

.post-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.post-card {
  background-color: #161b22;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.post-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.post-content {
  padding: 15px;
}

.post-text {
  margin: 0 0 15px;
  font-size: 16px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  color: #e6edf3;
}

.post-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.username {
  font-size: 14px;
  color: #8b949e;
}

.post-stats {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #8b949e;
  font-size: 12px;
}

.like-count {
  display: flex;
  align-items: center;
  gap: 5px;
}

.load-more {
  display: flex;
  justify-content: center;
  margin: 30px 0;
}

.create-post-fab {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 10;
}

/* Element Plus overrides */
:deep(.el-radio-button__inner) {
  background-color: #161b22;
  border-color: #30363d;
  color: #8b949e;
}

:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #8957e5;
  border-color: #8957e5;
  color: #fff;
  box-shadow: -1px 0 0 0 #8957e5;
}

:deep(.el-button) {
  background-color: #8957e5;
  border-color: #8957e5;
  color: #fff;
}

:deep(.el-button:hover) {
  background-color: #9e6ff0;
  border-color: #9e6ff0;
}
</style>
