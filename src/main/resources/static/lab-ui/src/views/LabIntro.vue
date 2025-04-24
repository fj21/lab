<template>
  <div class="lab-intro-container">
    <div class="hero-section">
      <div class="hero-content">
        <h1>{{ currentLang === 'zh' ? '大家好，这里是' : 'Welcome to' }}</h1>
        <h1>{{ currentLang === 'zh' ? '上海交通大学人工智能研究院！' : 'Shanghai Jiao Tong University AI Institute!' }}</h1>
      </div>
    </div>

    <div class="content-section">
      <div class="sidebar">
        <div class="sidebar-item" :class="{ active: activeSection === 'overview' }" @click="scrollToSection('overview')">
          {{ currentLang === 'zh' ? '研究院概况' : 'Overview' }}
        </div>
        <div class="sidebar-item" :class="{ active: activeSection === 'foundation' }" @click="scrollToSection('foundation')">
          {{ currentLang === 'zh' ? '发展基础' : 'Foundation' }}
        </div>
        <div class="sidebar-item" :class="{ active: activeSection === 'content' }" @click="scrollToSection('content')">
          {{ currentLang === 'zh' ? '建设内容' : 'Content' }}
        </div>
        <div class="sidebar-item" :class="{ active: activeSection === 'goals' }" @click="scrollToSection('goals')">
          {{ currentLang === 'zh' ? '建设目标' : 'Goals' }}
        </div>
      </div>

      <div class="main-content">
        <div id="overview" class="content-block">
          <h2>{{ currentLang === 'zh' ? '研究院概况' : 'Institute Overview' }}</h2>
          <p>{{ currentLang === 'zh' ? '人工智能正在深刻改变社会发展模式，而对接着上海市的战略部署，上海交通大学人工智能研究院（以下简称"研究院"）。' : 'Artificial Intelligence is profoundly changing the mode of social development, and connecting with Shanghai\'s strategic deployment, Shanghai Jiao Tong University Artificial Intelligence Research Institute (hereinafter referred to as the "Institute").' }}</p>
          <p>{{ currentLang === 'zh' ? '研究院作为上海交通大学人工智能研究实体，构建央地共建实体化研究团队，成为学校人工智能科研成果转化与对外交流的一个一站口，积极推进校内外研究中心建设，统筹学校人工智能领域研究。' : 'The Institute, as the artificial intelligence research entity of Shanghai Jiao Tong University, builds a substantive research team jointly established by the central and local governments, becomes a one-stop port for the transformation and external exchange of the school\'s artificial intelligence research results, actively promotes the construction of research centers inside and outside the school, and coordinates the research in the field of artificial intelligence in the school.' }}</p>
        </div>

        <div id="foundation" class="content-block">
          <h2>{{ currentLang === 'zh' ? '发展基础' : 'Development Foundation' }}</h2>
          <p>{{ currentLang === 'zh' ? '上海交通大学在人工智能领域拥有深厚的研究基础和丰富的人才资源。学校拥有多个国家级重点实验室和研究中心，涵盖机器学习、计算机视觉、自然语言处理等多个人工智能核心领域。' : 'Shanghai Jiao Tong University has a solid research foundation and rich talent resources in the field of artificial intelligence. The university has multiple national key laboratories and research centers, covering multiple core areas of artificial intelligence such as machine learning, computer vision, natural language processing, etc.' }}</p>
          <p>{{ currentLang === 'zh' ? '研究院依托学校强大的学科优势和科研实力，整合校内外资源，打造具有国际影响力的人工智能研究平台。' : 'The Institute relies on the school\'s strong disciplinary advantages and scientific research strength, integrates resources inside and outside the school, and builds an artificial intelligence research platform with international influence.' }}</p>
        </div>

        <div id="content" class="content-block">
          <h2>{{ currentLang === 'zh' ? '建设内容' : 'Construction Content' }}</h2>
          <p>{{ currentLang === 'zh' ? '研究院重点开展基础理论研究、关键技术攻关、应用示范推广等工作，涵盖智能感知、机器学习、知识工程、人机交互等多个方向。' : 'The Institute focuses on basic theoretical research, key technology breakthroughs, application demonstration and promotion, etc., covering multiple directions such as intelligent perception, machine learning, knowledge engineering, human-computer interaction, etc.' }}</p>
          <p>{{ currentLang === 'zh' ? '同时，研究院积极推动产学研合作，与国内外知名企业、研究机构建立战略合作关系，共同推动人工智能技术创新和产业发展。' : 'At the same time, the Institute actively promotes industry-university-research cooperation, establishes strategic cooperative relationships with well-known enterprises and research institutions at home and abroad, and jointly promotes technological innovation and industrial development of artificial intelligence.' }}</p>
        </div>

        <div id="goals" class="content-block">
          <h2>{{ currentLang === 'zh' ? '建设目标' : 'Construction Goals' }}</h2>
          <p>{{ currentLang === 'zh' ? '研究院致力于建设世界一流的人工智能研究机构，培养高水平人工智能人才，产出具有国际影响力的原创性研究成果，推动人工智能技术在各行业的创新应用。' : 'The Institute is committed to building a world-class artificial intelligence research institution, cultivating high-level artificial intelligence talents, producing original research results with international influence, and promoting innovative applications of artificial intelligence technology in various industries.' }}</p>
          <p>{{ currentLang === 'zh' ? '到2025年，研究院将建成国内领先、国际知名的人工智能研究平台，在多个研究方向取得突破性进展，为上海建设具有全球影响力的科技创新中心提供有力支撑。' : 'By 2025, the Institute will build a domestically leading and internationally renowned artificial intelligence research platform, make breakthrough progress in multiple research directions, and provide strong support for Shanghai to build a globally influential science and technology innovation center.' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';

// Language state
const currentLang = ref(localStorage.getItem('language') || 'zh');

// Active section tracking
const activeSection = ref('overview');
const sections = ['overview', 'foundation', 'content', 'goals'];
const sectionElements = ref([]);

// Scroll to section
const scrollToSection = (sectionId) => {
  const element = document.getElementById(sectionId);
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' });
    activeSection.value = sectionId;
  }
};

// Handle scroll to update active section
const handleScroll = () => {
  const scrollPosition = window.scrollY + 100; // Add offset for better UX

  for (let i = sectionElements.value.length - 1; i >= 0; i--) {
    const section = sectionElements.value[i];
    if (section && scrollPosition >= section.offsetTop) {
      activeSection.value = sections[i];
      break;
    }
  }
};

// Setup scroll listener
onMounted(() => {
  // Get section elements
  sectionElements.value = sections.map(id => document.getElementById(id));
  
  // Add scroll event listener
  window.addEventListener('scroll', handleScroll);
  
  // Check for language changes
  window.addEventListener('storage', (event) => {
    if (event.key === 'language') {
      currentLang.value = event.newValue;
    }
  });
  
  // Initial check for active section
  handleScroll();
});

// Clean up
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
.lab-intro-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding-top: 60px; /* Space for fixed navigation */
}

.hero-section {
  height: 60vh;
  background-image: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url('https://picsum.photos/id/1/1920/1080');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 0 20px;
}

.hero-content {
  max-width: 800px;
}

.hero-content h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #fff;
  line-height: 1.2;
}

.content-section {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.sidebar {
  width: 250px;
  position: sticky;
  top: 100px;
  height: fit-content;
  margin-right: 40px;
}

.sidebar-item {
  padding: 15px 20px;
  margin-bottom: 10px;
  background-color: #161b22;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.sidebar-item:hover {
  background-color: #21262d;
}

.sidebar-item.active {
  background-color: #21262d;
  border-left-color: #8957e5;
  color: #fff;
}

.main-content {
  flex: 1;
}

.content-block {
  margin-bottom: 60px;
  scroll-margin-top: 100px; /* For smooth scrolling */
}

.content-block h2 {
  font-size: 1.8rem;
  margin-bottom: 20px;
  color: #fff;
  border-bottom: 2px solid #30363d;
  padding-bottom: 10px;
}

.content-block p {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 20px;
  color: #8b949e;
}

/* Responsive styles */
@media (max-width: 768px) {
  .content-section {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    position: static;
    margin-right: 0;
    margin-bottom: 30px;
  }
  
  .hero-content h1 {
    font-size: 2rem;
  }
}
</style>
