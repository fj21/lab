<template>
  <div class="enhanced-home-container">

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧视觉区域 -->
      <div class="visual-area">
        <ParticleBackground>
          <div class="center-content">
            <div class="animated-text">
              <div class="chinese-text">智算未来 能赋无界</div>
              <div class="english-text">ARTIFICIAL INTELLIGENCE FOR UNLIMITED FUTURE</div>
            </div>
            <div class="animated-elements">
              <div class="element" v-for="(item, index) in animatedElements" :key="index">
                <div class="element-label">{{ item.label }}</div>
                <div class="element-circle">
                  <div class="loading-text">LOADING...</div>
                </div>
              </div>
            </div>
          </div>
        </ParticleBackground>
      </div>

      <!-- 右侧信息区域 -->
      <div class="info-area">
        <NewsSwiper
          :tabs="tabs"
          :tabs-content="tabsContent"
          @more="handleViewMore"
        />
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
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Search, Menu } from '@element-plus/icons-vue';
import ParticleBackground from '../components/ParticleBackground.vue';
import NewsSwiper from '../components/NewsSwiper.vue';

const router = useRouter();
const route = useRoute();
const currentRoute = computed(() => route.path);
const tabs = ['新闻动态', '通知公告', '学术动态'];

// Navigation functions
const navigateTo = (path) => {
  // Only navigate if we're not already on this path
  if (route.path !== path) {
    router.push(path).catch(err => {
      if (err.name !== 'NavigationDuplicated') {
        console.error('Navigation error:', err);
      }
    });
  }
};

const toggleLanguage = () => {
  // Toggle between languages (placeholder for now)
  console.log('Toggle language');
};

// 模拟数据
const newsItems = [
  {
    date: '2021/08/30',
    title: '人工智能研究院青年学者论坛：2024年新兴技术与应用研讨会成功举办'
  },
  {
    date: '2021/07/14',
    title: 'AI时代的新机遇：数字化创新论坛成功举办'
  },
  {
    date: '2021/07/09',
    title: '思想的碰撞：上海市人工智能学子的学术交流'
  },
  {
    date: '2021/06/25',
    title: '研究院成功研发新一代智能算法，性能提升30%'
  },
  {
    date: '2021/05/18',
    title: '研究院与多家企业签署战略合作协议，共同推进AI技术落地'
  }
];

const noticeItems = [
  {
    date: '2024/06/15',
    title: '关于2024年暑期研究院开放日活动安排的通知'
  },
  {
    date: '2024/05/20',
    title: '2024年度研究院研究生招生面试通知'
  },
  {
    date: '2024/04/10',
    title: '关于组织参加第十届全国人工智能创新大赛的通知'
  },
  {
    date: '2024/03/05',
    title: '研究院2024年度科研项目申报指南'
  },
  {
    date: '2024/02/28',
    title: '关于研究院设备更新与维护的通知'
  }
];

const academicItems = [
  {
    date: '2024/06/10',
    title: '研究院最新研究成果在国际顶级期刊Nature发表'
  },
  {
    date: '2024/05/15',
    title: '研究院主任受邀在ICML 2024作特邀报告'
  },
  {
    date: '2024/04/20',
    title: '研究院研究生在国际AI挑战赛中获得冠军'
  },
  {
    date: '2024/03/12',
    title: '研究院与斯坦福大学建立联合研究中心'
  },
  {
    date: '2024/02/05',
    title: '研究院开发的智能系统获得国家科技进步奖'
  }
];

const tabsContent = [newsItems, noticeItems, academicItems];

// 动画元素
const animatedElements = [
  { label: 'SUCCESS' },
  { label: 'DATA ANALYSIS' },
  { label: 'LOADING...' }
];

// 处理"了解更多"按钮点击
const handleViewMore = (tabIndex) => {
  console.log('View more for tab:', tabs[tabIndex]);
  // 根据不同的标签页跳转到不同的页面
  switch(tabIndex) {
    case 0: // 新闻动态
      router.push('/news');
      break;
    case 1: // 通知公告
      router.push('/news?type=notice');
      break;
    case 2: // 学术动态
      router.push('/news?type=academic');
      break;
    default:
      router.push('/news');
  }
};
</script>

<style scoped>
.enhanced-home-container {
  width: 100%;
  height: 100vh;
  background-color: #0d1117;
  color: #ffffff;
  position: relative;
  overflow: hidden;
}

/* 主要内容区域样式 */
.main-content {
  display: flex;
  height: 100vh;
  width: 100%;
}

/* 左侧视觉区域 */
.visual-area {
  flex: 2;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.center-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  padding: 0 20px;
}

.animated-text {
  text-align: center;
  margin-bottom: 80px;
}

.chinese-text {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #ffffff;
  text-shadow: 0 0 10px rgba(138, 43, 226, 0.8);
}

.english-text {
  font-size: 18px;
  color: #ffffff;
  opacity: 0.8;
}

.animated-elements {
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
  justify-content: center;
}

.element {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.element-label {
  font-size: 14px;
  color: #8a2be2;
  background-color: rgba(138, 43, 226, 0.2);
  padding: 5px 10px;
  border-radius: 4px;
}

.element-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 2px solid rgba(138, 43, 226, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.element-circle::before {
  content: '';
  position: absolute;
  top: -5px;
  left: -5px;
  right: -5px;
  bottom: -5px;
  border-radius: 50%;
  border: 1px solid rgba(138, 43, 226, 0.3);
  animation: pulse 2s infinite;
}

.loading-text {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.7);
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  100% {
    transform: scale(1.2);
    opacity: 0;
  }
}

/* 右侧信息区域 */
.info-area {
  width: 450px;
  padding: 40px;
  display: flex;
  flex-direction: column;
  background-color: rgba(13, 17, 23, 0.7);
  backdrop-filter: blur(10px);
  border-left: 1px solid rgba(255, 255, 255, 0.1);
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
