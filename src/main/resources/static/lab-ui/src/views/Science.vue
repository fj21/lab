<template>
  <div class="science-container">
    <div class="science-header">
      <h1>研究方向</h1>
      <div class="science-subtitle">探索人工智能的前沿领域</div>
    </div>

    <div class="research-areas">
      <div
        v-for="(area, index) in researchAreas"
        :key="index"
        class="research-area"
        @click="showResearchDetail(area)"
      >
        <div class="research-icon">
          <i :class="area.icon"></i>
        </div>
        <div class="research-title">{{ area.title }}</div>
        <div class="research-description">{{ area.description }}</div>
      </div>
    </div>

    <!-- Research Detail Dialog -->
    <el-dialog
      v-model="showDetail"
      :title="selectedArea ? selectedArea.title : ''"
      width="70%"
      class="research-detail-dialog"
      destroy-on-close
      :append-to-body="false"
      :modal="true"
      :close-on-click-modal="true"
      :show-close="true"
      @closed="handleDialogClosed"
    >
      <div class="research-detail-content" v-if="selectedArea">
        <div class="research-detail-header">
          <div class="researcher-info">
            <img :src="getResearcherAvatar(selectedArea.title)" alt="Researcher" class="researcher-avatar">
            <div class="researcher-details">
              <div class="researcher-name">{{ getResearcherName(selectedArea.title) }}</div>
              <div class="researcher-title">研究方向负责人</div>
            </div>
          </div>
          <div class="research-date">更新日期: {{ getCurrentDate() }}</div>
        </div>

        <div class="research-detail-body">
          <div class="research-detail-image">
            <img :src="getResearchImage(selectedArea.title)" alt="Research Image">
          </div>

          <div class="research-detail-text">
            <h3>研究背景</h3>
            <p>{{ selectedArea.background || generateBackground(selectedArea.title) }}</p>

            <h3>研究内容</h3>
            <p>{{ selectedArea.content || generateContent(selectedArea.title) }}</p>

            <h3>应用前景</h3>
            <p>{{ selectedArea.applications || generateApplications(selectedArea.title) }}</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';

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
  },
  {
    icon: 'el-icon-cpu',
    title: '强化学习',
    description: '研究智能体通过与环境交互学习最优策略的方法，应用于游戏、机器人控制等领域。'
  },
  {
    icon: 'el-icon-data-analysis',
    title: '联邦学习',
    description: '探索在保护数据隐私的前提下，多方协作训练AI模型的技术，促进数据安全与AI发展的平衡。'
  },
  {
    icon: 'el-icon-monitor',
    title: '多模态学习',
    description: '研究融合视觉、语言、声音等多种模态信息的AI技术，实现更全面的智能感知与理解。'
  }
];

// 状态变量
const showDetail = ref(false);
const selectedArea = ref(null);

// 获取路由参数
const router = useRouter();
const route = useRoute();

// 显示研究详情
const showResearchDetail = (area) => {
  // Prevent duplicate dialogs by checking if we're already showing this area
  if (showDetail.value && selectedArea.value && selectedArea.value.title === area.title) {
    return;
  }

  // First close any existing dialog to prevent duplication
  showDetail.value = false;
  selectedArea.value = null;

  // Small timeout to ensure dialog is fully closed before opening a new one
  setTimeout(() => {
    selectedArea.value = area;
    showDetail.value = true;

    // Update URL with the selected area without reloading the page
    const areaId = encodeURIComponent(area.title);
    router.replace({
      path: route.path,
      query: { area: areaId }
    }).catch(err => {
      if (err.name !== 'NavigationDuplicated') {
        console.error('Navigation error:', err);
      }
    });
  }, 100);
};

// 检查URL参数，如果有area参数，则自动显示对应的研究领域详情
onMounted(() => {
  // Reset dialog state on mount to prevent duplicates
  showDetail.value = false;
  selectedArea.value = null;

  // Check for URL parameters after a short delay
  setTimeout(() => {
    const areaParam = route.query.area;
    if (areaParam) {
      // Find the research area that matches the URL parameter
      const decodedAreaTitle = decodeURIComponent(areaParam);
      const matchedArea = researchAreas.find(area => area.title === decodedAreaTitle);

      if (matchedArea) {
        // Directly set values instead of using showResearchDetail to avoid duplicate dialogs
        selectedArea.value = matchedArea;
        showDetail.value = true;
      }
    }
  }, 300);
});

// Close dialog when navigating away
watch(() => route.path, (newPath) => {
  if (showDetail.value) {
    showDetail.value = false;
  }
});

// Clean up when component is unmounted
onBeforeUnmount(() => {
  if (showDetail.value) {
    showDetail.value = false;
  }
});

// Handle dialog closed event
const handleDialogClosed = () => {
  // Clear the area parameter from the URL when dialog is closed
  if (route.query.area) {
    router.replace({
      path: route.path,
      query: {}
    }).catch(err => {
      if (err.name !== 'NavigationDuplicated') {
        console.error('Navigation error:', err);
      }
    });
  }
};

// 获取当前日期
const getCurrentDate = () => {
  return new Date().toLocaleDateString('zh-CN');
};

// 获取研究者头像
const getResearcherAvatar = (title) => {
  // 根据研究方向返回不同的头像
  const hash = title.length % 10 + 20;
  return `https://randomuser.me/api/portraits/${hash % 2 === 0 ? 'men' : 'women'}/${hash}.jpg`;
};

// 获取研究者姓名
const getResearcherName = (title) => {
  const names = ['张教授', '李研究员', '王博士', '刘教授', '陈研究员', '林博士'];
  return names[title.length % names.length];
};

// 获取研究图片
const getResearchImage = (title) => {
  const imageId = title.length % 30 + 100;
  return `https://picsum.photos/id/${imageId}/800/400`;
};

// 生成研究背景
const generateBackground = (title) => {
  return `${title}是人工智能领域的重要研究方向，近年来随着计算能力的提升和算法的创新，该领域取得了显著进展。我们实验室在该方向的研究始于2015年，经过多年的积累，已形成了完整的理论体系和技术路线。目前，我们的研究团队由5名教授、10名博士后和20余名研究生组成，致力于解决该领域的前沿科学问题和关键技术挑战。`;
};

// 生成研究内容
const generateContent = (title) => {
  return `我们的研究主要集中在以下几个方面：\n\n1. 理论基础：探索${title}的数学基础和理论框架，提出新的模型和算法。\n\n2. 算法优化：针对现有算法的局限性，开发更高效、更鲁棒的新型算法。\n\n3. 应用研究：将${title}技术应用于实际场景，解决实际问题。\n\n4. 跨学科融合：结合其他学科知识，拓展${title}的应用边界。\n\n我们已在国际顶级期刊和会议上发表论文50余篇，获得国家和省部级科研项目10余项，申请专利30余项。`;
};

// 生成应用前景
const generateApplications = (title) => {
  return `${title}技术具有广阔的应用前景，主要体现在以下几个方面：\n\n1. 智能制造：提升生产效率和产品质量，降低成本。\n\n2. 医疗健康：辅助疾病诊断、药物研发和个性化治疗。\n\n3. 智慧城市：优化城市管理和服务，提升市民生活质量。\n\n4. 金融服务：风险评估、智能投顾和反欺诈等。\n\n5. 教育培训：个性化学习和智能教学辅助。\n\n未来，随着技术的不断成熟和应用场景的拓展，${title}将在更多领域发挥重要作用，创造更大的社会价值和经济效益。`;
};


</script>

<style scoped>
.science-container {
  min-height: 100vh;
  background-color: #0d1117;
  color:rgb(115, 128, 141);
  padding: 20px;
  padding-top: 80px; /* Space for fixed navigation */
  position: relative;
  z-index: 1; /* Ensure it's below the navigation bar */
}

.science-header {
  max-width: 1200px;
  margin: 0 auto 40px;
  text-align: center;
}

.science-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #58a6ff;
}

.science-subtitle {
  font-size: 1.2rem;
  color: #8b949e;
  margin-bottom: 30px;
}

.research-areas {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.research-area {
  background-color: #161b22;
  border-radius: 8px;
  padding: 25px;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.research-area:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.research-icon {
  font-size: 2.5rem;
  margin-bottom: 15px;
  color: #58a6ff;
}

.research-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.research-description {
  font-size: 0.9rem;
  color: #8b949e;
  line-height: 1.5;
}

/* Research Detail Dialog Styles */
:deep(.research-detail-dialog .el-dialog) {
  background-color: #161b22;
  border-radius: 8px;
  color: #e6edf3;
  max-width: 900px;
  z-index: 999; /* Ensure dialog is below navigation */
}

:deep(.research-detail-dialog .el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #30363d;
}

:deep(.research-detail-dialog .el-dialog__title) {
  color: #58a6ff;
  font-size: 1.5rem;
}

:deep(.research-detail-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: #8b949e;
}

:deep(.research-detail-dialog .el-dialog__body) {
  padding: 20px;
}

.research-detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.researcher-info {
  display: flex;
  align-items: center;
}

.researcher-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}

.researcher-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.researcher-title {
  font-size: 0.9rem;
  color: #8b949e;
}

.research-date {
  font-size: 0.9rem;
  color: #8b949e;
}

.research-detail-body {
  margin-bottom: 30px;
}

.research-detail-image {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.research-detail-image img {
  width: 100%;
  height: auto;
}

.research-detail-text {
  margin-bottom: 30px;
}

.research-detail-text h3 {
  color: #58a6ff;
  margin: 20px 0 10px;
  font-size: 1.2rem;
}

.research-detail-text p {
  line-height: 1.8;
  margin-bottom: 15px;
  white-space: pre-line;
}






</style>