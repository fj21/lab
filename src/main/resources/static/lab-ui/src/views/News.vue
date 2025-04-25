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

    <div class="news-grid">
      <div
        v-for="(item, index) in filteredNews"
        :key="index"
        class="news-item"
        @click="viewNewsDetail(item)"
      >
        <div class="news-image">
          <img :src="getRandomImage(index)" alt="News Image">
        </div>
        <div class="news-content">
          <div class="news-date">{{ item.date }}</div>
          <div class="news-title">{{ item.title }}</div>
          <div class="news-summary" v-if="item.summary">{{ item.summary }}</div>
        </div>
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

        <div class="news-detail-image">
          <img :src="getRandomImage(0)" alt="News Detail Image">
        </div>

        <div class="news-detail-text">
          <p>{{ selectedNews.content || generateFakeContent(selectedNews.title) }}</p>
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
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const activeTab = ref(0);
const tabs = ['全部', '新闻动态', '通知公告', '学术动态'];
const showNewsDetail = ref(false);
const selectedNews = ref(null);

// 根据URL参数设置初始标签页和显示详情
onMounted(() => {
  const type = route.query.type;
  const newsId = route.query.id;

  if (type === 'notice') {
    activeTab.value = 2; // 通知公告
  } else if (type === 'academic') {
    activeTab.value = 3; // 学术动态
  } else if (type === 'news') {
    activeTab.value = 1; // 新闻动态
  }

  // 如果有newsId参数，显示对应的新闻详情
  if (newsId) {
    // 查找匹配的新闻项
    const decodedNewsId = decodeURIComponent(newsId);
    const matchedNews = newsItems.find(item => {
      const itemId = item.title.substring(0, 20).replace(/\s+/g, '-');
      return decodedNewsId.includes(itemId);
    });

    if (matchedNews) {
      selectedNews.value = matchedNews;
      showNewsDetail.value = true;
    } else {
      // If not found, create a temporary news item based on the ID
      console.warn('News item not found for ID:', decodedNewsId);

      // Try to extract a title from the ID
      const title = decodedNewsId.replace(/-/g, ' ');

      selectedNews.value = {
        title: title,
        date: new Date().toLocaleDateString('zh-CN'),
        type: type || 'news',
        summary: '详细信息正在加载中...'
      };

      showNewsDetail.value = true;
    }
  }
});

// 模拟新闻数据
const newsItems = [
  {
    date: '2021/09/30',
    title: '人工智能教育部重点实验室建设项目验收会召开',
    type: 'news',
    summary: '本次验收会汇集了来自全国各地的专家学者，对实验室建设项目进行了全面评估。'
  },
  {
    date: '2021/07/14',
    title: 'AI时代数据开放共享创新论坛顺利举行',
    type: 'news',
    summary: '论坛邀请了多位业界专家，深入探讨了AI技术如何推动各行业数字化转型。'
  },
  {
    date: '2021/07/09',
    title: '思源AI论坛-上海市人工智能重大专项学术研讨会举办',
    type: 'academic',
    summary: '来自上海多所高校的学生齐聚一堂，分享各自在人工智能领域的研究成果和心得体会。'
  },
  {
    date: '2024/04/25',
    title: '研究院成功研发新一代智能算法，性能提升30%',
    type: 'news',
    summary: '这一算法在图像识别、自然语言处理等多个领域都表现出色，将推动AI技术的进一步发展。'
  },
  {
    date: '2024/04/18',
    title: '研究院与多家企业签署战略合作协议，共同推进AI技术落地',
    type: 'news',
    summary: '此次合作将促进产学研深度融合，加速AI技术在实际场景中的应用。'
  },
  {
    date: '2024/06/15',
    title: '关于2024年暑期研究院开放日活动安排的通知',
    type: 'notice',
    summary: '开放日将于7月15日至7月20日举行，欢迎各界人士前来参观交流。'
  },
  {
    date: '2024/05/20',
    title: '2024年度研究院研究生招生面试通知',
    type: 'notice',
    summary: '面试将于6月10日至6月15日进行，请考生提前做好准备。'
  },
  {
    date: '2024/04/10',
    title: '关于组织参加第十届全国人工智能创新大赛的通知',
    type: 'notice',
    summary: '鼓励研究院师生积极参与，展示我院在人工智能领域的研究成果。'
  },
  {
    date: '2024/03/05',
    title: '研究院2024年度科研项目申报指南',
    type: 'notice',
    summary: '详细说明了今年科研项目的申报条件、流程和评审标准。'
  },
  {
    date: '2024/02/28',
    title: '关于研究院设备更新与维护的通知',
    type: 'notice',
    summary: '设备维护将于3月10日至3月15日进行，请各实验室做好相关安排。'
  },
  {
    date: '2024/06/10',
    title: '研究院最新研究成果在国际顶级期刊Nature发表',
    type: 'academic',
    summary: '这是研究院在国际顶级期刊上的又一重要成果，彰显了我院的科研实力。'
  },
  {
    date: '2024/05/15',
    title: '研究院主任受邀在ICML 2024作特邀报告',
    type: 'academic',
    summary: 'ICML是机器学习领域的顶级会议，此次受邀作报告是对研究院学术水平的认可。'
  },
  {
    date: '2024/04/20',
    title: '研究院研究生在国际AI挑战赛中获得冠军',
    type: 'academic',
    summary: '这是研究院学生在国际赛事中取得的又一佳绩，展示了我院的人才培养成果。'
  },
  {
    date: '2024/03/12',
    title: '研究院与斯坦福大学建立联合研究中心',
    type: 'academic',
    summary: '此次合作将促进国际学术交流，提升研究院的国际影响力。'
  },
  {
    date: '2024/02/05',
    title: '研究院开发的智能系统获得国家科技进步奖',
    type: 'academic',
    summary: '这是对研究院科研成果的高度认可，将激励我们继续在科研道路上前进。'
  }
];

// 根据当前标签页筛选新闻
const filteredNews = computed(() => {
  if (activeTab.value === 0) {
    return newsItems;
  } else {
    const typeMap = {
      1: 'news',
      2: 'notice',
      3: 'academic'
    };
    return newsItems.filter(item => item.type === typeMap[activeTab.value]);
  }
});

// 获取随机图片
const getRandomImage = (index) => {
  const imageId = (index % 10) + 10; // 使用10-19的图片ID
  return `https://picsum.photos/id/${imageId}/800/400`;
};

// 查看新闻详情
const viewNewsDetail = (news) => {
  selectedNews.value = news;
  showNewsDetail.value = true;
};

// 生成假内容
const generateFakeContent = (title) => {
  return `${title}\n\n近日，我院举办了一系列重要活动，旨在促进学术交流和科研创新。活动吸引了众多专家学者和学生参与，现场讨论热烈，思想碰撞频繁。\n\n在活动中，与会者就人工智能的最新发展趋势、技术挑战以及应用前景进行了深入探讨。多位专家分享了他们在各自领域的研究成果和实践经验，为参与者提供了宝贵的学习机会。\n\n此次活动不仅加强了学术界与产业界的联系，也为我院师生提供了与行业领袖面对面交流的平台。通过这样的交流活动，我们希望能够促进知识共享，推动技术创新，为人工智能的发展贡献力量。\n\n未来，我院将继续举办类似活动，搭建更多交流平台，促进学术研究与产业应用的深度融合，共同推动人工智能技术的进步与发展。`;
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