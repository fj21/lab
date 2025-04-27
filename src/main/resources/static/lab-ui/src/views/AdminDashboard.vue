<template>
  <div class="admin-dashboard">
    <!-- 左侧导航栏 -->
    <div class="admin-sidebar">
      <div class="admin-logo">
        <img src="../assets/lab-logo.svg" alt="实验室标志" class="lab-logo" />
        <span>管理后台</span>
      </div>

      <div class="menu-items">
        <div
          v-for="(item, index) in menuItems"
          :key="index"
          :class="['menu-item', { active: currentSection === item.key }]"
          @click="currentSection = item.key"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="admin-content">
      <!-- 顶部信息栏 -->
      <div class="admin-header">
        <div class="header-title">{{ getCurrentSectionTitle }}</div>
        <div class="header-actions">
          <el-button type="primary" @click="handleAction">
            {{ getActionButtonText }}
          </el-button>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content-area">
        <!-- 新闻管理 -->
        <div v-if="currentSection === 'news'" class="section-content">
          <div class="filter-row">
            <el-select v-model="currentCategory" placeholder="选择分类" @change="loadNewsData">
              <el-option label="全部" :value="null" />
              <el-option label="新闻动态" :value="0" />
              <el-option label="通知公告" :value="1" />
              <el-option label="学术动态" :value="2" />
            </el-select>
            <el-button type="primary" @click="refreshNewsData">刷新</el-button>
          </div>

          <el-table :data="newsData" style="width: 100%" v-loading="loading">
            <el-table-column prop="date" label="发布日期" width="180" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="categoryName" label="分类" width="120" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="editNews(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteNews(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container" v-if="newsData.length > 0">
            <el-button @click="loadMoreNews" :disabled="!hasMoreNews || loading">
              加载更多
            </el-button>
          </div>
        </div>

        <!-- 用户管理 -->
        <div v-if="currentSection === 'users'" class="section-content">
          <el-table :data="userData" style="width: 100%">
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="role" label="角色" width="120" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteUser(scope.row)">禁用</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 内容管理 -->
        <div v-if="currentSection === 'content'" class="section-content">
          <el-tabs v-model="contentTab">
            <el-tab-pane label="研究方向" name="research">
              <content-editor :type="'research'" />
            </el-tab-pane>
            <el-tab-pane label="科研成果" name="achievements">
              <content-editor :type="'achievements'" />
            </el-tab-pane>
            <el-tab-pane label="团队介绍" name="team">
              <content-editor :type="'team'" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  Document,
  User,
  Setting,
  Collection,
  PieChart
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import ContentEditor from '../components/ContentEditor.vue'
import { getPosts, createPost } from '../api/post'

// 菜单项配置
const menuItems = [
  { key: 'news', label: '新闻管理', icon: 'Document' },
  { key: 'users', label: '用户管理', icon: 'User' },
  { key: 'content', label: '内容管理', icon: 'Collection' },
  { key: 'settings', label: '系统设置', icon: 'Setting' },
  { key: 'statistics', label: '数据统计', icon: 'PieChart' }
]

const currentSection = ref('news')
const contentTab = ref('research')
const loading = ref(false)
const currentCategory = ref(null)
const lastPostId = ref(0)
const hasMoreNews = ref(true)

// 实际数据
const newsData = ref([])

const userData = ref([
  {
    username: 'admin',
    role: '管理员',
    status: '正常'
  },
  {
    username: 'researcher',
    role: '研究员',
    status: '正常'
  }
])

// 计算属性
const getCurrentSectionTitle = computed(() => {
  const item = menuItems.find(item => item.key === currentSection.value)
  return item ? item.label : ''
})

const getActionButtonText = computed(() => {
  switch (currentSection.value) {
    case 'news':
      return '发布新闻'
    case 'users':
      return '添加用户'
    case 'content':
      return '保存更改'
    default:
      return '操作'
  }
})

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN').replace(/\//g, '-');
};

// 获取分类名称
const getCategoryName = (category) => {
  switch (category) {
    case 0:
      return '新闻动态';
    case 1:
      return '通知公告';
    case 2:
      return '学术动态';
    default:
      return '未分类';
  }
};

// 加载新闻数据
const loadNewsData = async (reset = true) => {
  try {
    loading.value = true;

    if (reset) {
      lastPostId.value = 0;
      newsData.value = [];
      hasMoreNews.value = true;
    }

    const response = await getPosts(currentCategory.value, lastPostId.value);

    if (response.code === 200 && response.data) {
      const posts = response.data.postVOList || [];

      if (posts.length === 0) {
        hasMoreNews.value = false;
      } else {
        // 转换帖子数据格式
        const formattedPosts = posts.map(post => ({
          id: post.id,
          date: formatDate(post.createdAt),
          title: post.content.split('\n')[0] || '无标题', // 使用内容的第一行作为标题
          content: post.content,
          category: post.category,
          categoryName: getCategoryName(post.category),
          coverUrl: post.coverUrl
        }));

        newsData.value = [...newsData.value, ...formattedPosts];
        lastPostId.value = response.data.lastPostId;
      }
    } else {
      ElMessage.error('获取新闻列表失败');
    }
  } catch (error) {
    console.error('Failed to load news data:', error);
    ElMessage.error('加载新闻数据失败');
  } finally {
    loading.value = false;
  }
};

// 刷新新闻数据
const refreshNewsData = () => {
  loadNewsData(true);
};

// 加载更多新闻
const loadMoreNews = () => {
  if (!hasMoreNews.value || loading.value) return;
  loadNewsData(false);
};

// 方法
const handleAction = () => {
  switch (currentSection.value) {
    case 'news':
      // 打开新闻发布对话框
      showCreateNewsDialog();
      break;
    case 'users':
      // 实现用户添加逻辑
      break;
    case 'content':
      // 实现内容保存逻辑
      break;
  }
};

// 显示创建新闻对话框
const showCreateNewsDialog = () => {
  ElMessageBox.prompt('请输入新闻标题', '发布新闻', {
    confirmButtonText: '下一步',
    cancelButtonText: '取消',
    inputPlaceholder: '请输入新闻标题'
  }).then(({ value }) => {
    if (value) {
      showCreateNewsContentDialog(value);
    }
  }).catch(() => {
    // 用户取消操作
  });
};

// 显示创建新闻内容对话框
const showCreateNewsContentDialog = (title) => {
  ElMessageBox.prompt('请输入新闻内容', '发布新闻', {
    confirmButtonText: '下一步',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputPlaceholder: '请输入新闻内容'
  }).then(({ value }) => {
    if (value) {
      showCreateNewsCategoryDialog(title, value);
    }
  }).catch(() => {
    // 用户取消操作
  });
};

// 显示创建新闻分类对话框
const showCreateNewsCategoryDialog = (title, content) => {
  ElMessageBox.confirm(
    '请选择新闻分类',
    '发布新闻',
    {
      confirmButtonText: '发布',
      cancelButtonText: '取消',
      distinguishCancelAndClose: true,
      showCancelButton: true,
      showInput: true,
      inputType: 'select',
      inputPlaceholder: '请选择分类',
      inputValue: 0,
      inputOptions: [
        { label: '新闻动态', value: 0 },
        { label: '通知公告', value: 1 },
        { label: '学术动态', value: 2 }
      ]
    }
  ).then(({ value }) => {
    // 发布新闻
    createNewsPost(title, content, value);
  }).catch(() => {
    // 用户取消操作
  });
};

// 创建新闻帖子
const createNewsPost = async (title, content, category) => {
  try {
    const fullContent = `${title}\n\n${content}`;

    const loadingInstance = ElLoading.service({
      text: '正在发布...',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    const postData = {
      type: 0, // 图片类型
      category: category,
      visibility: 0, // 公开
      content: fullContent,
      files: [] // 暂不上传文件
    };

    const response = await createPost(postData);

    loadingInstance.close();

    if (response.code === 200) {
      ElMessage.success('发布成功');
      refreshNewsData();
    } else {
      ElMessage.error(`发布失败: ${response.message || '未知错误'}`);
    }
  } catch (error) {
    console.error('Failed to create post:', error);
    ElMessage.error('发布失败，请稍后重试');
  }
};

const editNews = (row) => {
  // 实现新闻编辑逻辑
  console.log('Edit news:', row);
  ElMessageBox.prompt('编辑新闻内容', '编辑新闻', {
    confirmButtonText: '保存',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputValue: row.content
  }).then(({ value }) => {
    if (value) {
      ElMessage.info('编辑功能即将上线');
      // TODO: 实现编辑API调用
    }
  }).catch(() => {
    // 用户取消操作
  });
};

const deleteNews = (row) => {
  ElMessageBox.confirm('确定要删除这条新闻吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // TODO: 实现删除API调用
    ElMessage.info('删除功能即将上线');
  }).catch(() => {
    // 用户取消操作
  });
};

const editUser = (row) => {
  // 实现用户编辑逻辑
  console.log('Edit user:', row)
}

const deleteUser = (row) => {
  ElMessageBox.confirm('确定要禁用该用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 实现禁用逻辑
    ElMessage.success('操作成功')
  })
}

// 在组件挂载时加载数据
onMounted(() => {
  loadNewsData();
});
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
}

.admin-sidebar {
  width: 250px;
  background-color: #161b22;
  border-right: 1px solid #30363d;
  padding: 20px 0;
}

.admin-logo {
  display: flex;
  align-items: center;
  padding: 0 20px;
  margin-bottom: 30px;
}

.lab-logo {
  height: 32px;
  margin-right: 10px;
}

.menu-items {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.menu-item:hover {
  background-color: #1f2937;
}

.menu-item.active {
  background-color: #1f2937;
  border-left: 3px solid #58a6ff;
}

.menu-item .el-icon {
  margin-right: 10px;
  font-size: 18px;
}

.admin-content {
  flex: 1;
  padding: 20px;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #30363d;
}

.header-title {
  font-size: 24px;
  font-weight: bold;
}

.content-area {
  background-color: #161b22;
  border-radius: 8px;
  padding: 20px;
}

.section-content {
  min-height: 500px;
}

:deep(.el-table) {
  background-color: transparent;
  color: #e6edf3;
}

:deep(.el-table tr) {
  background-color: #161b22;
}

:deep(.el-table th) {
  background-color: #1f2937;
  color: #e6edf3;
  border-bottom: 1px solid #30363d;
}

:deep(.el-table td) {
  border-bottom: 1px solid #30363d;
}

:deep(.el-tabs__item) {
  color: #8b949e;
}

:deep(.el-tabs__item.is-active) {
  color: #58a6ff;
}

:deep(.el-tabs__active-bar) {
  background-color: #58a6ff;
}

.filter-row {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>