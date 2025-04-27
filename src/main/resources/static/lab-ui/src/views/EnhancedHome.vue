<template>
  <div class="enhanced-home-container">
    <!-- 页面指示器 -->
    <div class="page-indicator">
      <div
        v-for="i in 3"
        :key="i"
        :class="['indicator-dot', { active: currentSection === i }]"
        @click="scrollToSection(i)"
      ></div>
    </div>

    <!-- 第一部分：全屏赛博朋克风格背景 -->
    <section id="section1" class="home-section" ref="section1">
      <div class="cyberpunk-container">
        <div class="cyberpunk-overlay"></div>
        <div class="cyberpunk-background"></div>
        <div class="cyberpunk-grid"></div>
        <div class="cyberpunk-content">
          <div class="slogan-container">
            <div class="chinese-slogan">{{ cyberpunkData.slogan.chinese }}</div>
            <div class="english-slogan">{{ cyberpunkData.slogan.english }}</div>
          </div>
          <div class="cyberpunk-elements">
            <div class="data-circle">
              <div class="circle-inner">
                <div class="circle-number">{{ cyberpunkData.stats.circleValue }}</div>
                <svg class="circle-progress" width="120" height="120">
                  <circle cx="60" cy="60" r="54" stroke="#8a2be2" stroke-width="4" fill="none" stroke-dasharray="339.3" stroke-dashoffset="100" />
                </svg>
              </div>
            </div>
            <div class="data-stats">
              <div class="stat-item">
                <div class="stat-label">AI POWER</div>
                <div class="stat-value">{{ cyberpunkData.stats.aiPower }}<span class="percent">%</span></div>
              </div>
              <div class="stat-item">
                <div class="stat-label">EFFICIENCY</div>
                <div class="stat-value">{{ cyberpunkData.stats.efficiency }}<span class="unit">ms</span></div>
              </div>
            </div>
          </div>
        </div>
        <div class="scroll-down" @click="scrollToSection(2)">
          <i class="el-icon-arrow-down"></i>
        </div>
      </div>
    </section>

    <!-- 第二部分：粒子背景和新闻轮播 -->
    <section id="section2" class="home-section" ref="section2">
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
            @viewDetail="handleViewNewsDetail"
          />
        </div>
      </div>
    </section>

    <!-- 第三部分：研究领域展示 -->
    <section id="section3" class="home-section" ref="section3">
      <div class="research-content">
        <div class="section-header">
          <h2>研究领域</h2>
          <div class="section-subtitle">RESEARCH AREAS</div>
        </div>

        <div class="research-areas">
          <div
            class="research-area"
            v-for="(area, index) in researchAreas"
            :key="index"
            @click="navigateToResearchArea(area)"
          >
            <div class="research-icon">
              <i :class="area.icon"></i>
            </div>
            <div class="research-title">{{ area.title }}</div>
            <div class="research-description">{{ area.description }}</div>
            <div class="research-more">
              <span>了解详情</span>
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>

        <div class="section-button" @click="navigateTo('/science')">
          查看所有研究方向
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Search, Menu, ArrowDown } from '@element-plus/icons-vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Pagination, EffectFade, Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/effect-fade';
import ParticleBackground from '../components/ParticleBackground.vue';
import NewsSwiper from '../components/NewsSwiper.vue';
import { getPosts } from '../api/post';
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();
const currentRoute = computed(() => route.path);
const tabs = ['新闻动态', '通知公告', '学术动态'];

// 页面滚动相关
const section1 = ref(null);
const section2 = ref(null);
const section3 = ref(null);
const currentSection = ref(1);
const isScrolling = ref(false);

// 赛博朋克风格背景数据
const cyberpunkData = {
  slogan: {
    chinese: '智算未来 能赋无界',
    english: 'ARTIFICIAL INTELLIGENCE FOR UNLIMITED FUTURE'
  },
  stats: {
    circleValue: 14,
    aiPower: 31.3,
    efficiency: 55
  }
};

// 研究领域数据
const researchAreas = [
  {
    icon: 'el-icon-cpu',
    title: '机器学习与深度学习',
    description: '研究先进的机器学习算法和深度学习模型，解决复杂场景下的智能决策问题。'
  },
  {
    icon: 'el-icon-data-analysis',
    title: '大数据分析与挖掘',
    description: '开发高效的大数据处理技术，从海量数据中提取有价值的信息和知识。'
  },
  {
    icon: 'el-icon-monitor',
    title: '计算机视觉',
    description: '研究图像识别、目标检测、场景理解等视觉智能技术，赋能智能监控、自动驾驶等领域。'
  },
  {
    icon: 'el-icon-chat-dot-round',
    title: '自然语言处理',
    description: '探索语言理解与生成技术，推动智能对话、机器翻译、文本分析等应用发展。'
  },
  {
    icon: 'el-icon-connection',
    title: '知识图谱与推理',
    description: '构建结构化知识体系，实现智能推理与决策，支持智能问答和知识服务。'
  },
  {
    icon: 'el-icon-mobile',
    title: '智能交互技术',
    description: '研究人机交互新模式，打造更自然、高效、智能的交互体验。'
  }
];

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

// Navigate to specific research area
const navigateToResearchArea = (area) => {
  // Encode the research area title to use as a query parameter
  const areaId = encodeURIComponent(area.title);

  // Navigate to the science page with the area title as a parameter
  router.push({
    path: '/science',
    query: { area: areaId }
  });
};

// 滚动到指定部分
const scrollToSection = (sectionNumber) => {
  if (isScrolling.value) return;

  isScrolling.value = true;
  currentSection.value = sectionNumber;

  let targetSection;
  switch(sectionNumber) {
    case 1:
      targetSection = section1.value;
      break;
    case 2:
      targetSection = section2.value;
      break;
    case 3:
      targetSection = section3.value;
      break;
  }

  if (targetSection) {
    targetSection.scrollIntoView({ behavior: 'smooth' });

    // 防止连续滚动
    setTimeout(() => {
      isScrolling.value = false;
    }, 1000);
  }
};

// 监听滚动事件，更新当前部分
const handleScroll = () => {
  if (isScrolling.value) return;

  const scrollPosition = window.scrollY;
  const windowHeight = window.innerHeight;

  // 计算每个部分的位置
  const section1Top = section1.value?.offsetTop || 0;
  const section2Top = section2.value?.offsetTop || 0;
  const section3Top = section3.value?.offsetTop || 0;

  // 确定当前部分
  if (scrollPosition < section2Top - windowHeight / 2) {
    currentSection.value = 1;
  } else if (scrollPosition < section3Top - windowHeight / 2) {
    currentSection.value = 2;
  } else {
    currentSection.value = 3;
  }
};

// 实际数据
const newsItems = ref([]);
const noticeItems = ref([]);
const academicItems = ref([]);
const tabsContent = computed(() => [newsItems.value, noticeItems.value, academicItems.value]);

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN').replace(/\//g, '/');
};

// 加载新闻数据
const loadNewsData = async () => {
  try {
    // 加载新闻动态 (category=0)
    const newsResponse = await getPosts(0);
    if (newsResponse.code === 200 && newsResponse.data) {
      newsItems.value = newsResponse.data.postVOList.map(post => ({
        id: post.id,
        date: formatDate(post.createdAt),
        title: post.content.split('\n')[0] || '无标题', // 使用内容的第一行作为标题
        content: post.content,
        coverUrl: post.coverUrl
      }));
    }

    // 加载通知公告 (category=1)
    const noticeResponse = await getPosts(1);
    if (noticeResponse.code === 200 && noticeResponse.data) {
      noticeItems.value = noticeResponse.data.postVOList.map(post => ({
        id: post.id,
        date: formatDate(post.createdAt),
        title: post.content.split('\n')[0] || '无标题',
        content: post.content,
        coverUrl: post.coverUrl
      }));
    }

    // 加载学术动态 (category=2)
    const academicResponse = await getPosts(2);
    if (academicResponse.code === 200 && academicResponse.data) {
      academicItems.value = academicResponse.data.postVOList.map(post => ({
        id: post.id,
        date: formatDate(post.createdAt),
        title: post.content.split('\n')[0] || '无标题',
        content: post.content,
        coverUrl: post.coverUrl
      }));
    }
  } catch (error) {
    console.error('Failed to load news data:', error);
    ElMessage.error('加载新闻数据失败，请稍后重试');

    // 如果加载失败，使用默认数据
    newsItems.value = [
      { date: '2021/09/30', title: '人工智能教育部重点实验室建设项目验收会召开' },
      { date: '2021/07/14', title: 'AI时代数据开放共享创新论坛顺利举行' }
    ];

    noticeItems.value = [
      { date: '2024/06/15', title: '关于2024年暑期研究院开放日活动安排的通知' },
      { date: '2024/05/20', title: '2024年度研究院研究生招生面试通知' }
    ];

    academicItems.value = [
      { date: '2024/06/10', title: '研究院最新研究成果在国际顶级期刊Nature发表' },
      { date: '2024/05/15', title: '研究院主任受邀在ICML 2024作特邀报告' }
    ];
  }
};

// 动画元素
const animatedElements = [
  { label: 'Luminous' },
  { label: 'Nexus' },
  { label: 'Vivid' }
];

// 处理"了解更多"按钮点击
const handleViewMore = (tabIndex) => {
  console.log('View more for tab:', tabs[tabIndex]);
  // 根据不同的标签页跳转到不同的页面
  switch(tabIndex) {
    case 0: // 新闻动态
      router.push('/news?type=news');
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

// 处理点击新闻项
const handleViewNewsDetail = ({ item, type }) => {
  console.log('View news detail:', item, 'type:', type);

  // 使用真实的帖子ID
  const postId = item.id;
  let newsType = type;

  // 根据类型设置查询参数
  router.push({
    path: '/news',
    query: {
      id: postId,
      type: newsType
    }
  });
};

// 生命周期钩子
onMounted(async () => {
  window.addEventListener('scroll', handleScroll);
  // 初始滚动到顶部
  window.scrollTo(0, 0);

  // 加载新闻数据
  await loadNewsData();
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
.enhanced-home-container {
  width: 100%;
  background-color: #0d1117;
  color: #ffffff;
  position: relative;
  overflow-x: hidden;
  scroll-behavior: smooth;
}

/* Hide scrollbar for Chrome, Safari and Opera */
.enhanced-home-container::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
.enhanced-home-container {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}

/* 通用部分样式 */
.home-section {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 页面指示器 */
.page-indicator {
  position: fixed;
  right: 40px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 15px;
  z-index: 100;
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #555555;
  transition: all 0.3s;
  cursor: pointer;
}

.indicator-dot.active {
  background-color: #ffffff;
  height: 20px;
  border-radius: 10px;
}

/* 第一部分：赛博朋克风格 */
.cyberpunk-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.cyberpunk-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(13, 17, 23, 0.7) 0%, rgba(13, 17, 23, 0.5) 100%);
  z-index: 2;
}

.cyberpunk-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('https://images.unsplash.com/photo-1635776062127-a1720b6602b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2532&q=80');
  background-size: cover;
  background-position: center;
  filter: brightness(0.7) saturate(1.5);
  z-index: 1;
}

.cyberpunk-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(rgba(0, 162, 255, 0.1) 1px, transparent 1px),
                    linear-gradient(90deg, rgba(0, 162, 255, 0.1) 1px, transparent 1px);
  background-size: 40px 40px;
  z-index: 3;
  animation: gridPulse 8s infinite linear;
}

@keyframes gridPulse {
  0% {
    opacity: 0.3;
    background-size: 40px 40px;
  }
  50% {
    opacity: 0.5;
    background-size: 42px 42px;
  }
  100% {
    opacity: 0.3;
    background-size: 40px 40px;
  }
}

.cyberpunk-content {
  position: relative;
  z-index: 4;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  padding: 0 20px;
}

.slogan-container {
  text-align: center;
  margin-bottom: 60px;
}

.chinese-slogan {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 10px;
  text-shadow: 0 0 15px rgba(138, 43, 226, 0.8), 0 0 10px rgba(0, 162, 255, 0.6);
  letter-spacing: 4px;
  animation: textGlow 3s infinite alternate;
}

.english-slogan {
  font-size: 1.8rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-shadow: 0 0 10px rgba(0, 162, 255, 0.7);
  animation: textGlow 3s infinite alternate 1.5s;
}

@keyframes textGlow {
  0% {
    text-shadow: 0 0 15px rgba(138, 43, 226, 0.8), 0 0 10px rgba(0, 162, 255, 0.6);
  }
  100% {
    text-shadow: 0 0 20px rgba(138, 43, 226, 1), 0 0 30px rgba(0, 162, 255, 0.8), 0 0 40px rgba(255, 42, 109, 0.4);
  }
}

.cyberpunk-elements {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 80px;
  margin-top: 40px;
}

.data-circle {
  position: relative;
  width: 120px;
  height: 120px;
}

.circle-inner {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(13, 17, 23, 0.7);
  border-radius: 50%;
  box-shadow: 0 0 20px rgba(138, 43, 226, 0.5);
}

.circle-number {
  font-size: 2.5rem;
  font-weight: bold;
  color: #ff2a6d;
  text-shadow: 0 0 10px rgba(255, 42, 109, 0.7);
}

.circle-progress {
  position: absolute;
  top: 0;
  left: 0;
  transform: rotate(-90deg);
  filter: drop-shadow(0 0 5px rgba(138, 43, 226, 0.8));
  animation: rotateProgress 10s linear infinite;
}

@keyframes rotateProgress {
  0% {
    transform: rotate(-90deg);
  }
  100% {
    transform: rotate(270deg);
  }
}

.data-stats {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stat-item {
  background-color: rgba(13, 17, 23, 0.7);
  padding: 10px 20px;
  border-left: 3px solid #05d9e8;
  box-shadow: 0 0 15px rgba(5, 217, 232, 0.3);
  position: relative;
  overflow: hidden;
}

.stat-item:after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(5, 217, 232, 0.2), transparent);
  transform: translateX(-100%);
  animation: statScan 3s infinite;
}

.stat-item:nth-child(2):after {
  animation-delay: 1.5s;
}

@keyframes statScan {
  0% {
    transform: translateX(-100%);
  }
  50%, 100% {
    transform: translateX(100%);
  }
}

.stat-label {
  font-size: 0.9rem;
  color: #05d9e8;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
}

.percent, .unit {
  font-size: 1rem;
  margin-left: 2px;
  color: #ff2a6d;
}

.scroll-down {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  animation: bounce 2s infinite;
}

.scroll-down i {
  font-size: 20px;
  color: #ffffff;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0) translateX(-50%);
  }
  40% {
    transform: translateY(-20px) translateX(-50%);
  }
  60% {
    transform: translateY(-10px) translateX(-50%);
  }
}

/* 第二部分：粒子背景和新闻轮播 */
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
  padding: 40px 40px 20px 40px;
  display: flex;
  flex-direction: column;
  background-color: rgba(13, 17, 23, 0.7);
  backdrop-filter: blur(10px);
  border-left: 1px solid rgba(255, 255, 255, 0.1);
  overflow-y: auto;
}

/* 第三部分：研究领域 */
.research-content {
  width: 100%;
  height: 100%;
  padding: 80px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #161b22;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-header h2 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #ffffff;
}

.section-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 2px;
}

.research-areas {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  width: 100%;
  max-width: 1200px;
}

.research-area {
  background-color: rgba(31, 41, 55, 0.5);
  border-radius: 8px;
  padding: 30px;
  transition: all 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.research-area::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(138, 43, 226, 0.1) 0%, rgba(0, 0, 0, 0) 100%);
  opacity: 0;
  transition: opacity 0.3s;
}

.research-area:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  border-color: rgba(138, 43, 226, 0.5);
}

.research-area:hover::before {
  opacity: 1;
}

.research-icon {
  font-size: 36px;
  color: #8a2be2;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.research-area:hover .research-icon {
  transform: scale(1.1);
}

.research-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #ffffff;
  transition: color 0.3s;
}

.research-area:hover .research-title {
  color: #8a2be2;
}

.research-description {
  font-size: 14px;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 20px;
  flex: 1;
}

.research-more {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 5px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  transition: all 0.3s;
  margin-top: 10px;
  opacity: 0;
  transform: translateY(10px);
}

.research-area:hover .research-more {
  opacity: 1;
  transform: translateY(0);
  color: #8a2be2;
}

.section-button {
  margin-top: 60px;
  padding: 12px 30px;
  background-color: rgba(138, 43, 226, 0.8);
  color: #ffffff;
  border-radius: 30px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.section-button:hover {
  background-color: rgba(138, 43, 226, 1);
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .research-areas {
    grid-template-columns: repeat(2, 1fr);
  }

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
}

@media (max-width: 768px) {
  .research-areas {
    grid-template-columns: 1fr;
  }

  .chinese-slogan {
    font-size: 2.5rem;
  }

  .english-slogan {
    font-size: 1.2rem;
  }

  .cyberpunk-elements {
    flex-direction: column;
    gap: 40px;
  }

  .page-indicator {
    display: none;
  }

  .home-section {
    height: auto;
    min-height: 100vh;
  }
}
</style>
