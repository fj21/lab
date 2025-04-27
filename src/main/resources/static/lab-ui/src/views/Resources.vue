<template>
  <div class="resources-container">
    <div class="resources-header">
      <h1>{{ currentLang === 'zh' ? '资源中心' : 'Resources Center' }}</h1>
      <p>{{ currentLang === 'zh' ? '获取实验室提供的书籍、实验指南和其他学习资源' : 'Access books, experiment guides, and other learning resources provided by the laboratory' }}</p>
    </div>

    <div class="resource-categories">
      <div
        v-for="category in categories"
        :key="category.id"
        class="category-item"
        :class="{ active: selectedCategory === category.id }"
        @click="selectedCategory = category.id"
      >
        {{ currentLang === 'zh' ? category.name : category.nameEn }}
      </div>
    </div>

    <div class="resources-grid">
      <div v-for="resource in filteredResources" :key="resource.id" class="resource-card">
        <div class="resource-image">
          <img :src="resource.coverUrl || getDefaultCover(resource.type)" :alt="resource.title" />
          <div class="resource-type-badge">
            <i :class="getResourceTypeIcon(resource.type)"></i>
          </div>
        </div>
        <div class="resource-info">
          <h3>{{ resource.title }}</h3>
          <p class="resource-description">{{ resource.description }}</p>
          <div class="resource-meta">
            <span class="resource-type">{{ getResourceTypeName(resource.type) }}</span>
            <span class="resource-date">{{ formatDate(resource.createdAt) }}</span>
          </div>
          <el-button type="primary" size="small" @click="viewResourceDetail(resource)">
            {{ currentLang === 'zh' ? '查看详情' : 'View Details' }}
          </el-button>
        </div>
      </div>

      <div v-if="loading" class="loading-indicator">
        <i class="el-icon-loading"></i> {{ currentLang === 'zh' ? '加载中...' : 'Loading...' }}
      </div>

      <div v-if="!loading && filteredResources.length === 0" class="no-resources">
        <i class="el-icon-document"></i>
        <p>{{ currentLang === 'zh' ? '暂无资源' : 'No resources available' }}</p>
      </div>
    </div>

    <!-- Resource Detail Dialog -->
    <el-dialog
      v-model="showResourceDetail"
      :title="selectedResource ? selectedResource.title : ''"
      width="70%"
      class="resource-detail-dialog"
      destroy-on-close
    >
      <div class="resource-detail-content" v-if="selectedResource">
        <div class="resource-detail-header">
          <div class="resource-detail-type">{{ getResourceTypeName(selectedResource.type) }}</div>
          <div class="resource-detail-date">{{ formatDate(selectedResource.createdAt) }}</div>
        </div>

        <div class="resource-detail-image" v-if="selectedResource.coverUrl">
          <img :src="selectedResource.coverUrl" :alt="selectedResource.title" />
        </div>

        <div class="resource-detail-description">
          <h3>{{ currentLang === 'zh' ? '描述' : 'Description' }}</h3>
          <p>{{ selectedResource.description }}</p>
        </div>

        <div class="resource-detail-content-text" v-if="selectedResource.content">
          <h3>{{ currentLang === 'zh' ? '内容' : 'Content' }}</h3>
          <div v-html="selectedResource.content"></div>
        </div>

        <div class="resource-download" v-if="selectedResource.downloadUrl">
          <el-button type="primary" @click="downloadResource(selectedResource)">
            <i class="el-icon-download"></i>
            {{ currentLang === 'zh' ? '下载资源' : 'Download Resource' }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { Document, VideoPlay, Reading, Download } from '@element-plus/icons-vue';
import { getResources, getResourceDetail } from '../api/resource';
import request from '../utils/request';

const router = useRouter();
const currentLang = ref(localStorage.getItem('language') || 'zh');

// Resource categories
const categories = [
  { id: 0, name: '全部', nameEn: 'All' },
  { id: 1, name: '书籍', nameEn: 'Books' },
  { id: 2, name: '实验指南', nameEn: 'Experiment Guides' },
  { id: 3, name: '视频教程', nameEn: 'Video Tutorials' },
  { id: 4, name: '软件工具', nameEn: 'Software Tools' }
];

// State variables
const resources = ref([]);
const selectedCategory = ref(0);
const loading = ref(false);
const showResourceDetail = ref(false);
const selectedResource = ref(null);

// Filter resources by selected category
const filteredResources = computed(() => {
  if (selectedCategory.value === 0) {
    return resources.value;
  }
  return resources.value.filter(resource => resource.categoryId === selectedCategory.value);
});

// Get default cover image based on resource type
const getDefaultCover = (type) => {
  switch (type) {
    case 1: // Book
      return 'https://via.placeholder.com/300x400?text=Book';
    case 2: // Guide
      return 'https://via.placeholder.com/300x400?text=Guide';
    case 3: // Video
      return 'https://via.placeholder.com/300x400?text=Video';
    case 4: // Software
      return 'https://via.placeholder.com/300x400?text=Software';
    default:
      return 'https://via.placeholder.com/300x400?text=Resource';
  }
};

// Get icon for resource type
const getResourceTypeIcon = (type) => {
  switch (type) {
    case 1: // Book
      return 'el-icon-reading';
    case 2: // Guide
      return 'el-icon-document';
    case 3: // Video
      return 'el-icon-video-play';
    case 4: // Software
      return 'el-icon-download';
    default:
      return 'el-icon-document';
  }
};

// Get name for resource type
const getResourceTypeName = (type) => {
  if (currentLang.value === 'zh') {
    switch (type) {
      case 1: return '书籍';
      case 2: return '实验指南';
      case 3: return '视频教程';
      case 4: return '软件工具';
      default: return '资源';
    }
  } else {
    switch (type) {
      case 1: return 'Book';
      case 2: return 'Experiment Guide';
      case 3: return 'Video Tutorial';
      case 4: return 'Software Tool';
      default: return 'Resource';
    }
  }
};

// Format date
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString();
};

// View resource detail
const viewResourceDetail = async (resource) => {
  try {
    // Try to fetch detailed resource information from API
    const response = await getResourceDetail(resource.id);
    if (response && response.data) {
      selectedResource.value = response.data;
    } else {
      // Fallback to the basic resource info if API fails
      selectedResource.value = resource;
    }
    showResourceDetail.value = true;
  } catch (error) {
    console.error('获取资源详情失败:', error);
    // Fallback to the basic resource info if API fails
    selectedResource.value = resource;
    showResourceDetail.value = true;
  }
};

// Download resource
const downloadResource = async (resource) => {
  if (resource.downloadUrl) {
    try {
      // Call API to increment download count
      const response = await request.post(`/api/resource/download/${resource.id}`);
      if (response && response.data) {
        // Open download URL in new tab
        window.open(resource.downloadUrl, '_blank');
      }
    } catch (error) {
      console.error('下载资源失败:', error);
      // Still try to open the URL even if the API call fails
      window.open(resource.downloadUrl, '_blank');
    }
  } else {
    ElMessage.warning(currentLang.value === 'zh' ? '资源暂不可下载' : 'Resource is not available for download');
  }
};

// Fetch resources from API
const fetchResources = async () => {
  loading.value = true;
  try {
    // Try to fetch from the API
    try {
      const response = await getResources(selectedCategory.value === 0 ? null : selectedCategory.value);
      if (response && response.data) {
        resources.value = response.data.resourceList || [];
      }
    } catch (apiError) {
      console.error('API error:', apiError);
      // Fallback to mock data if API fails
      resources.value = [
        {
          id: 1,
          title: '人工智能基础教程',
          description: '本书详细介绍了人工智能的基本概念、算法和应用场景，适合初学者阅读。',
          type: 1, // Book
          categoryId: 1,
          coverUrl: 'https://via.placeholder.com/300x400?text=AI+Book',
          downloadUrl: null,
          createdAt: '2023-05-15'
        },
        {
          id: 2,
          title: '机器学习实验指南',
          description: '本指南提供了机器学习实验的详细步骤和注意事项，帮助学生完成实验任务。',
          type: 2, // Guide
          categoryId: 2,
          coverUrl: 'https://via.placeholder.com/300x400?text=ML+Guide',
          downloadUrl: '#',
          createdAt: '2023-06-20'
        },
        {
          id: 3,
          title: '深度学习视频教程',
          description: '由实验室教授录制的深度学习视频教程，包含理论讲解和实践演示。',
          type: 3, // Video
          categoryId: 3,
          coverUrl: 'https://via.placeholder.com/300x400?text=DL+Video',
          downloadUrl: null,
          content: '<p>视频内容包括：</p><ul><li>神经网络基础</li><li>卷积神经网络</li><li>循环神经网络</li><li>实际案例分析</li></ul>',
          createdAt: '2023-07-10'
        },
        {
          id: 4,
          title: 'TensorFlow 安装与配置指南',
          description: '详细介绍如何在不同操作系统上安装和配置TensorFlow环境。',
          type: 2, // Guide
          categoryId: 2,
          coverUrl: 'https://via.placeholder.com/300x400?text=TensorFlow',
          downloadUrl: '#',
          createdAt: '2023-08-05'
        },
        {
          id: 5,
          title: '数据可视化工具包',
          description: '实验室开发的数据可视化工具包，支持多种数据格式和可视化方式。',
          type: 4, // Software
          categoryId: 4,
          coverUrl: 'https://via.placeholder.com/300x400?text=Visualization',
          downloadUrl: '#',
          createdAt: '2023-09-15'
        }
      ];
    }
  } catch (error) {
    console.error('获取资源失败:', error);
    ElMessage.error(currentLang.value === 'zh' ? '获取资源失败' : 'Failed to fetch resources');
  } finally {
    loading.value = false;
  }
};

// Fetch resources on mount
onMounted(() => {
  fetchResources();
});

// Watch for category changes
watch(selectedCategory, () => {
  fetchResources();
});
</script>

<style scoped>
.resources-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.resources-header {
  text-align: center;
  margin-bottom: 40px;
}

.resources-header h1 {
  font-size: 2.5rem;
  color: #e6edf3;
  margin-bottom: 10px;
}

.resources-header p {
  color: #8b949e;
  font-size: 1.1rem;
  max-width: 800px;
  margin: 0 auto;
}

.resource-categories {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.category-item {
  padding: 10px 20px;
  background-color: #21262d;
  color: #8b949e;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover {
  background-color: #30363d;
  color: #e6edf3;
}

.category-item.active {
  background-color: #8957e5;
  color: #ffffff;
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
}

.resource-card {
  background-color: #21262d;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
}

.resource-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.resource-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.resource-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.resource-type-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(13, 17, 23, 0.8);
  color: #e6edf3;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.resource-info {
  padding: 20px;
}

.resource-info h3 {
  color: #e6edf3;
  margin-bottom: 10px;
  font-size: 1.2rem;
}

.resource-description {
  color: #8b949e;
  margin-bottom: 15px;
  font-size: 0.9rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.resource-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 0.8rem;
}

.resource-type {
  color: #8957e5;
}

.resource-date {
  color: #8b949e;
}

.loading-indicator, .no-resources {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px;
  color: #8b949e;
}

.no-resources i {
  font-size: 3rem;
  margin-bottom: 10px;
}

/* Resource Detail Dialog */
:deep(.resource-detail-dialog .el-dialog__header) {
  background-color: #21262d;
  padding: 15px 20px;
}

:deep(.resource-detail-dialog .el-dialog__title) {
  color: #e6edf3;
}

:deep(.resource-detail-dialog .el-dialog__body) {
  background-color: #0d1117;
  color: #e6edf3;
  padding: 20px;
}

.resource-detail-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  color: #8b949e;
}

.resource-detail-type {
  color: #8957e5;
  font-weight: bold;
}

.resource-detail-image {
  margin-bottom: 20px;
  text-align: center;
}

.resource-detail-image img {
  max-width: 100%;
  max-height: 400px;
  border-radius: 4px;
}

.resource-detail-description h3,
.resource-detail-content-text h3 {
  color: #e6edf3;
  margin-bottom: 10px;
  font-size: 1.2rem;
}

.resource-detail-description p {
  color: #8b949e;
  line-height: 1.6;
  margin-bottom: 20px;
}

.resource-detail-content-text {
  margin-bottom: 20px;
}

.resource-detail-content-text div {
  color: #8b949e;
  line-height: 1.6;
}

.resource-download {
  margin-top: 20px;
  text-align: center;
}

/* Responsive styles */
@media (max-width: 768px) {
  .resources-header h1 {
    font-size: 2rem;
  }

  .resource-categories {
    flex-direction: column;
    align-items: center;
  }

  .category-item {
    width: 100%;
    max-width: 300px;
    text-align: center;
  }
}
</style>
