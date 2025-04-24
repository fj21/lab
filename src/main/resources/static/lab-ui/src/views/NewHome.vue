<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <div class="top-nav">
      <div class="logo-area">
        <img src="../assets/lab-logo.svg" alt="实验室标志" class="lab-logo" />
        <div class="logo-text">
          <div class="main-title">实验室门户</div>
          <div class="sub-title">LABORATORY PORTAL</div>
        </div>
      </div>
      <div class="nav-links">
        <div class="nav-item" @click="goToHome">首页</div>
        <div class="nav-item">研究方向</div>
        <div class="nav-item">科研成果</div>
        <div class="nav-item">研究团队</div>
        <div class="nav-item">人才培养</div>
        <div class="nav-item">科学普及</div>
        <div class="nav-item" @click="goToForum">在线论坛</div>
        <div class="nav-item">EN | 中文</div>
        <div class="nav-item search-icon">
          <el-icon><Search /></el-icon>
        </div>
        <div class="nav-item menu-icon">
          <el-icon><Menu /></el-icon>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧视觉区域 -->
      <div class="visual-area">
        <div class="visual-content">
          <!-- 这里可以放置3D模型、动画或图片 -->
          <div class="lab-name-animation">实验室门户</div>
        </div>
      </div>

      <!-- 右侧信息区域 -->
      <div class="info-area">
        <div class="tabs">
          <div
            v-for="(tab, index) in tabs"
            :key="index"
            :class="['tab', { active: activeTab === index }]"
            @click="activeTab = index"
          >
            {{ tab }}
          </div>
        </div>

        <div class="news-list">
          <div v-for="(item, index) in currentTabItems" :key="index" class="news-item">
            <div class="news-date">{{ item.date }}</div>
            <div class="news-title">{{ item.title }}</div>
          </div>
        </div>

        <div class="more-btn" @click="viewMore">了解更多</div>
      </div>
    </div>

    <!-- 页面指示器 -->
    <div class="page-indicator">
      <div
        v-for="i in 5"
        :key="i"
        :class="['indicator-dot', { active: i === 1 }]"
      ></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Search, Menu } from '@element-plus/icons-vue';

const router = useRouter();
const activeTab = ref(0);
const tabs = ['新闻动态', '通知公告', '学术动态'];

// 模拟数据
const newsItems = [
  {
    date: '2024/05/30',
    title: '人工智能实验室青年学者论坛：2024年新兴技术与应用研讨会成功举办'
  },
  {
    date: '2023/12/11',
    title: '2023年度实验室人工智能研究突出进展研讨会圆满结束！'
  },
  {
    date: '2023/11/17',
    title: '【实验室新闻】知名AI专家学者来访，与实验室人员进行学术交流'
  },
  {
    date: '2023/10/25',
    title: '实验室成功研发新一代智能算法，性能提升30%'
  },
  {
    date: '2023/09/18',
    title: '实验室与多家企业签署战略合作协议，共同推进AI技术落地'
  }
];

const noticeItems = [
  {
    date: '2024/06/15',
    title: '关于2024年暑期实验室开放日活动安排的通知'
  },
  {
    date: '2024/05/20',
    title: '2024年度实验室研究生招生面试通知'
  },
  {
    date: '2024/04/10',
    title: '关于组织参加第十届全国人工智能创新大赛的通知'
  },
  {
    date: '2024/03/05',
    title: '实验室2024年度科研项目申报指南'
  },
  {
    date: '2024/02/28',
    title: '关于实验室设备更新与维护的通知'
  }
];

const academicItems = [
  {
    date: '2024/06/10',
    title: '实验室最新研究成果在国际顶级期刊Nature发表'
  },
  {
    date: '2024/05/15',
    title: '实验室主任受邀在ICML 2024作特邀报告'
  },
  {
    date: '2024/04/20',
    title: '实验室研究生在国际AI挑战赛中获得冠军'
  },
  {
    date: '2024/03/12',
    title: '实验室与斯坦福大学建立联合研究中心'
  },
  {
    date: '2024/02/05',
    title: '实验室开发的智能系统获得国家科技进步奖'
  }
];

const tabsContent = [newsItems, noticeItems, academicItems];

const currentTabItems = computed(() => {
  return tabsContent[activeTab.value];
});

// 导航方法
const goToHome = () => {
  router.push('/');
};

const goToForum = () => {
  router.push('/post/wall');
};

const viewMore = () => {
  // 查看更多新闻
  console.log('View more clicked');
};
</script>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
  background-color: #0e1117;
  color: #ffffff;
  position: relative;
  overflow: hidden;
}

/* 顶部导航栏样式 */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  position: relative;
  z-index: 10;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

.lab-logo {
  height: 40px;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.main-title {
  font-size: 18px;
  font-weight: bold;
}

.sub-title {
  font-size: 12px;
  color: #aaaaaa;
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-item {
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
}

.nav-item:hover {
  color: #8a2be2; /* 紫色高亮 */
}

.search-icon, .menu-icon {
  font-size: 18px;
}

/* 主要内容区域样式 */
.main-content {
  display: flex;
  height: calc(100vh - 80px);
}

/* 左侧视觉区域 */
.visual-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.visual-content {
  width: 100%;
  height: 100%;
  background: url('../assets/lab-visual.jpg') center center no-repeat;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lab-name-animation {
  font-size: 48px;
  font-weight: bold;
  color: #ffffff;
  text-shadow: 0 0 10px rgba(138, 43, 226, 0.8);
  animation: glow 2s infinite alternate;
}

@keyframes glow {
  from {
    text-shadow: 0 0 10px rgba(138, 43, 226, 0.8);
  }
  to {
    text-shadow: 0 0 20px rgba(138, 43, 226, 1), 0 0 30px rgba(138, 43, 226, 0.8);
  }
}

/* 右侧信息区域 */
.info-area {
  width: 400px;
  padding: 40px;
  display: flex;
  flex-direction: column;
}

.tabs {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.tab {
  font-size: 16px;
  cursor: pointer;
  padding-bottom: 5px;
  position: relative;
  color: #aaaaaa;
}

.tab.active {
  color: #ffffff;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #8a2be2;
}

.news-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.news-item {
  cursor: pointer;
}

.news-date {
  font-size: 12px;
  color: #aaaaaa;
  margin-bottom: 5px;
}

.news-title {
  font-size: 14px;
  line-height: 1.5;
  transition: color 0.3s;
}

.news-item:hover .news-title {
  color: #8a2be2;
}

.more-btn {
  margin-top: 30px;
  padding: 10px 20px;
  border: 1px solid #ffffff;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.more-btn:hover {
  background-color: #8a2be2;
  border-color: #8a2be2;
}

/* 页面指示器 */
.page-indicator {
  position: absolute;
  right: 40px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #555555;
  transition: all 0.3s;
}

.indicator-dot.active {
  background-color: #ffffff;
  height: 20px;
  border-radius: 10px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .visual-area {
    height: 50vh;
  }

  .info-area {
    width: 100%;
  }

  .page-indicator {
    display: none;
  }
}

@media (max-width: 768px) {
  .top-nav {
    padding: 15px 20px;
  }

  .nav-links {
    gap: 10px;
  }

  .nav-item:not(.search-icon):not(.menu-icon) {
    display: none;
  }

  .search-icon, .menu-icon {
    display: block;
  }
}
</style>
