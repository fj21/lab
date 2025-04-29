<template>
  <div class="navigation-container">
    <div class="navigation-content">
      <div class="logo-section">
        <div class="logo-item">
          <img src="https://via.placeholder.com/40x40?text=CQU" alt="重庆大学" class="logo-image" />
          <span class="logo-text">重庆大学</span>
        </div>
        <div class="logo-divider"></div>
        <div class="logo-item">
          <img src="https://via.placeholder.com/40x40?text=AI" alt="实验室" class="logo-image" />
          <span class="logo-text">实验室</span>
        </div>
      </div>

      <div class="right-section">
        <div class="nav-links">
          <div
            v-for="item in navItems"
            :key="item.path"
            class="nav-item"
            :class="{ 'active': isActive(item.path) }"
            @click="navigateTo(item.path)"
          >
            {{ currentLang === 'zh' ? item.label : item.enLabel }}
          </div>
        </div>

        <div class="language-selector">
          <span :class="{ 'active': currentLang === 'en' }" @click="changeLang('en')">EN</span>
          <span class="lang-divider">|</span>
          <span :class="{ 'active': currentLang === 'zh' }" @click="changeLang('zh')">中</span>
        </div>

        <div class="search-icon" @click="toggleSearch">
          <i class="el-icon-search"></i>
        </div>

        <div class="user-section" v-if="isLoggedIn">
          <el-dropdown trigger="click">
            <div class="user-info">
              <el-avatar :src="userInfo?.avatar || 'https://randomuser.me/api/portraits/men/1.jpg'" size="small"></el-avatar>
              <span class="username">{{ userInfo?.username || '用户' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="showUserProfile">个人资料</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="auth-buttons" v-else>
          <el-button size="small" type="primary" @click="navigateTo('/login')">登录</el-button>
          <el-button size="small" @click="navigateTo('/register')">注册</el-button>
        </div>

        <div class="menu-icon" @click="toggleMobileMenu">
          <i class="el-icon-menu"></i>
        </div>
      </div>
    </div>

    <!-- Mobile Menu -->
    <div class="mobile-menu" :class="{ 'active': mobileMenuOpen }">
      <div
        v-for="item in navItems"
        :key="item.path"
        class="mobile-nav-item"
        :class="{ 'active': isActive(item.path) }"
        @click="navigateTo(item.path); toggleMobileMenu()"
      >
        {{ currentLang === 'zh' ? item.label : item.enLabel }}
      </div>

      <div class="mobile-language">
        <span :class="{ 'active': currentLang === 'en' }" @click="changeLang('en')">English</span>
        <span :class="{ 'active': currentLang === 'zh' }" @click="changeLang('zh')">中文</span>
      </div>
    </div>

    <!-- Search Overlay -->
    <div class="search-overlay" :class="{ 'active': searchOpen }">
      <div class="search-container">
        <div class="search-header">
          <h2>搜索</h2>
          <div class="close-search" @click="toggleSearch">
            <i class="el-icon-close"></i>
          </div>
        </div>
        <div class="search-input">
          <el-input
            v-model="searchQuery"
            placeholder="输入关键词搜索..."
            :prefix-icon="Search"
            @keyup.enter="performSearch"
          ></el-input>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import { getUserInfo } from '../api/user';

const route = useRoute();
const router = useRouter();

// Navigation items
const navItems = [
  { label: '首页', path: '/home', enLabel: 'Home' },
  { label: '实验室概况', path: '/labIntro', enLabel: 'About' },
  { label: '团队成员', path: '/teams', enLabel: 'Team' },
  { label: '新闻动态', path: '/news', enLabel: 'News' },
  { label: '科学研究', path: '/science', enLabel: 'Research' },
  { label: '资源中心', path: '/resources', enLabel: 'Resources' },
  { label: '在线交流', path: '/posts', enLabel: 'Forum' }
];

// Reactive state
const userInfo = ref(null);
const currentLang = ref('zh');
const mobileMenuOpen = ref(false);
const searchOpen = ref(false);
const searchQuery = ref('');

// Check if user is logged in
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token');
});

// Check if a nav item is active
const isActive = (path) => {
  if (path === '/home' && route.path === '/') {
    return true;
  }
  return route.path.startsWith(path);
};

// Navigate to a path
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

// Toggle mobile menu
const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value;
  if (mobileMenuOpen.value) {
    searchOpen.value = false;
  }
};

// Toggle search overlay
const toggleSearch = () => {
  searchOpen.value = !searchOpen.value;
  if (searchOpen.value) {
    mobileMenuOpen.value = false;
    // Focus the search input after a short delay to allow the animation to complete
    setTimeout(() => {
      document.querySelector('.search-input input').focus();
    }, 300);
  }
};

// Perform search
const performSearch = () => {
  if (!searchQuery.value.trim()) return;

  // In a real app, we would navigate to a search results page
  ElMessage.info(`搜索: ${searchQuery.value}`);
  searchQuery.value = '';
  toggleSearch();
};

// Change language
const changeLang = (lang) => {
  currentLang.value = lang;
  // Store language preference in localStorage
  localStorage.setItem('language', lang);
  // Show success message in the appropriate language
  if (lang === 'en') {
    ElMessage.success('Language switched to English');
  } else {
    ElMessage.success('语言已切换为中文');
  }
};

// Show user profile
const showUserProfile = () => {
  router.push('/user/profile');
};

// Handle logout
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // Clear token and user info
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    userInfo.value = null;

    // Show success message
    ElMessage.success('已退出登录');

    // Redirect to home page
    router.push('/home');
  }).catch(() => {});
};

// Fetch user info
const fetchUserInfo = async () => {
  if (isLoggedIn.value) {
    try {
      console.log('Fetching user info...');
      const response = await getUserInfo();
      console.log('User info response:', response);

      if (response.code === 200 && response.data) {
        userInfo.value = response.data;

        // Store userId in localStorage if not already there
        if (!localStorage.getItem('userId') && response.data.id) {
          localStorage.setItem('userId', response.data.id);
          console.log('User ID stored from fetchUserInfo:', response.data.id);
        }
      } else {
        console.error('Failed to get user info, response code:', response.code);
        // If we get an error response, clear the token
        //localStorage.removeItem('token');
        //localStorage.removeItem('userId');
        userInfo.value = null;
      }
    } catch (error) {
      console.error('Failed to fetch user info:', error);
      console.error('Error details:', error.message);

      // If there's an error (like token expired), clear the token
      //localStorage.removeItem('token');
      //localStorage.removeItem('userId');
      userInfo.value = null;
    }
  } else {
    console.log('User not logged in, skipping fetchUserInfo');
  }
};

// Fetch user info on mount
onMounted(async () => {
  try {
    // Check if language preference is stored in localStorage
    const savedLang = localStorage.getItem('language');
    if (savedLang) {
      currentLang.value = savedLang;
    }

    // Add a small delay before fetching user info to ensure token is properly set
    setTimeout(async () => {
      // Fetch user info if logged in
      if (isLoggedIn.value) {
        console.log('Token found, fetching user info...');
        await fetchUserInfo();
      }
    }, 500);
  } catch (error) {
    console.error('初始化失败:', error);
  }
});
</script>

<style scoped>
.navigation-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: #0d1117;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.navigation-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 40px;
  width: 100%;
}

.logo-section {
  display: flex;
  align-items: center;
}

.logo-item {
  display: flex;
  align-items: center;
}

.logo-image {
  height: 40px;
  width: 40px;
  margin-right: 10px;
}

.logo-text {
  color: #e6edf3;
  font-weight: bold;
  font-size: 16px;
  white-space: nowrap;
}

.logo-divider {
  height: 20px;
  width: 1px;
  background-color: #30363d;
  margin: 0 15px;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.nav-links {
  display: flex;
  gap: 20px;
  margin-right: 10px;
}

.nav-item {
  color: #8b949e;
  cursor: pointer;
  padding: 5px 0;
  position: relative;
  transition: color 0.3s;
  white-space: nowrap;
}

.nav-item:hover, .nav-item.active {
  color: #e6edf3;
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #8957e5;
}

.language-selector {
  display: flex;
  align-items: center;
  color: #8b949e;
}

.language-selector span {
  cursor: pointer;
  transition: color 0.3s;
}

.language-selector span.active {
  color: #e6edf3;
}

.lang-divider {
  margin: 0 5px;
}

.search-icon, .menu-icon {
  color: #8b949e;
  cursor: pointer;
  transition: color 0.3s;
  font-size: 20px;
}

.search-icon:hover, .menu-icon:hover {
  color: #e6edf3;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
  color: #e6edf3;
  font-size: 14px;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

/* Mobile menu */
.menu-icon {
  display: none;
}

.mobile-menu {
  display: none;
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  background-color: #161b22;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  transform: translateY(-100%);
  transition: transform 0.3s ease-in-out;
  z-index: 999;
}

.mobile-menu.active {
  transform: translateY(0);
}

.mobile-nav-item {
  padding: 15px 0;
  color: #8b949e;
  border-bottom: 1px solid #30363d;
  cursor: pointer;
}

.mobile-nav-item.active {
  color: #e6edf3;
}

.mobile-language {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.mobile-language span {
  cursor: pointer;
  color: #8b949e;
  padding: 10px;
}

.mobile-language span.active {
  color: #e6edf3;
  background-color: #21262d;
  border-radius: 4px;
}

/* Search overlay */
.search-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(13, 17, 23, 0.9);
  z-index: 1001;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 100px;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s, visibility 0.3s;
}

.search-overlay.active {
  opacity: 1;
  visibility: visible;
}

.search-container {
  width: 80%;
  max-width: 600px;
  background-color: #161b22;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-header h2 {
  color: #e6edf3;
  margin: 0;
}

.close-search {
  cursor: pointer;
  color: #8b949e;
  font-size: 20px;
  transition: color 0.3s;
}

.close-search:hover {
  color: #e6edf3;
}

/* Element Plus overrides */
:deep(.el-input__inner) {
  background-color: #21262d;
  border-color: #30363d;
  color: #e6edf3;
}

:deep(.el-input__prefix) {
  color: #8b949e;
}

/* Responsive styles */
@media (max-width: 1200px) {
  .nav-links {
    gap: 15px;
  }

  .logo-text {
    font-size: 14px;
  }
}

@media (max-width: 992px) {
  .right-section .nav-links {
    display: none;
  }

  .menu-icon {
    display: block;
  }

  .mobile-menu {
    display: block;
  }
}

@media (max-width: 768px) {
  .logo-section {
    flex-direction: column;
    align-items: flex-start;
  }

  .logo-divider {
    display: none;
  }

  .language-selector {
    display: none;
  }

  .username {
    display: none;
  }
}
</style>
