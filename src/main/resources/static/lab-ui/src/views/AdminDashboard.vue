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
          <el-table :data="newsData" style="width: 100%">
            <el-table-column prop="date" label="发布日期" width="180" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="category" label="分类" width="120" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="editNews(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteNews(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
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
import { ref, computed } from 'vue'
import {
  Document,
  User,
  Setting,
  Collection,
  PieChart
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ContentEditor from '../components/ContentEditor.vue'

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

// 模拟数据
const newsData = ref([
  {
    date: '2024-01-15',
    title: '人工智能实验室举办学术讲座',
    category: '学术动态'
  },
  {
    date: '2024-01-10',
    title: '2024年度实验室工作计划发布',
    category: '通知公告'
  }
])

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

// 方法
const handleAction = () => {
  switch (currentSection.value) {
    case 'news':
      // 实现新闻发布逻辑
      break
    case 'users':
      // 实现用户添加逻辑
      break
    case 'content':
      // 实现内容保存逻辑
      break
  }
}

const editNews = (row) => {
  // 实现新闻编辑逻辑
  console.log('Edit news:', row)
}

const deleteNews = (row) => {
  ElMessageBox.confirm('确定要删除这条新闻吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 实现删除逻辑
    ElMessage.success('删除成功')
  })
}

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
</style>