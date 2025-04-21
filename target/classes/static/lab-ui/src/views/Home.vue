<template>
  <div class="home-container">
    <div class="header">
      <div class="logo">
        <span>实验室门户</span>
      </div>
      <div class="user-info" v-if="userInfo">
        <el-dropdown trigger="click">
          <div class="avatar-container">
            <el-avatar :src="userInfo.image" />
            <span class="username">{{ userInfo.username }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="showUserProfile">个人资料</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="content">
      <div class="hero-section">
        <h1>欢迎来到实验室门户系统</h1>
        <p>探索科研成果，分享实验心得，连接学术世界</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" @click="goToPostWall">发现内容</el-button>
          <el-button size="large" @click="goToAbout">了解更多</el-button>
        </div>
      </div>

      <div class="features-section">
        <h2>主要功能</h2>
        <div class="features-grid">
          <div class="feature-card" @click="goToPostWall">
            <i class="el-icon-picture-outline"></i>
            <h3>内容发现</h3>
            <p>浏览实验室最新的研究成果、活动信息和学术分享</p>
          </div>
          <div class="feature-card">
            <i class="el-icon-user"></i>
            <h3>团队介绍</h3>
            <p>了解实验室成员、研究方向和学术背景</p>
          </div>
          <div class="feature-card">
            <i class="el-icon-document"></i>
            <h3>论文库</h3>
            <p>查阅实验室发表的学术论文和研究成果</p>
          </div>
          <div class="feature-card">
            <i class="el-icon-chat-dot-round"></i>
            <h3>交流讨论</h3>
            <p>参与学术讨论，分享研究见解，与同行交流</p>
          </div>
        </div>
      </div>

      <div class="latest-posts-section">
        <h2>最新内容</h2>
        <div class="posts-preview">
          <div class="post-card" v-for="(post, index) in latestPosts" :key="index" @click="viewPostDetail(post.id)">
            <div class="post-image">
              <img :src="post.coverUrl" :alt="post.content" />
            </div>
            <div class="post-content">
              <p class="post-text">{{ post.content }}</p>
              <div class="post-info">
                <div class="user-info">
                  <img :src="post.userBasicVO?.image" :alt="post.userBasicVO?.username" class="user-avatar" />
                  <span class="username">{{ post.userBasicVO?.username }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="view-more-card" @click="goToPostWall">
            <div class="view-more-content">
              <i class="el-icon-arrow-right"></i>
              <span>查看更多</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserInfo } from '../api/user';
import request from '../utils/request';

const userInfo = ref(null);
const router = useRouter();
const latestPosts = ref([]);

// 页面加载时获取用户信息
onMounted(async () => {
  try {
    const res = await getUserInfo();
    userInfo.value = res.data;

    // 获取最新帖子
    fetchLatestPosts();
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
});

// 获取最新帖子
const fetchLatestPosts = async () => {
  try {
    const response = await request.get('/api/post/section', {
      params: {
        pageSize: 3 // 只获取3条数据用于首页展示
      }
    });

    if (response.code === 200) {
      latestPosts.value = response.data.postVOList || [];
    }
  } catch (error) {
    console.error('获取最新帖子失败:', error);
  }
};

// 显示用户信息
const showUserProfile = () => {
  ElMessage.info('个人资料功能即将上线');
};

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 清除token
    localStorage.removeItem('token');
    // 跳转到登录页
    router.push('/login');
    ElMessage.success('已退出登录');
  }).catch(() => {});
};

// 跳转到帖子墙
const goToPostWall = () => {
  router.push('/posts');
};

// 跳转到关于我们页面
const goToAbout = () => {
  router.push('/about');
};

// 查看帖子详情
const viewPostDetail = (postId) => {
  router.push(`/post/detail/${postId}`);
};
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.header {
  height: 60px;
  background-color: white;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #FF2442;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 10px;
  font-size: 14px;
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f5f5f5;
  color: #333;
}

/* Hero Section */
.hero-section {
  width: 100%;
  padding: 60px 20px;
  text-align: center;
  background-color: white;
  margin-bottom: 30px;
}

.hero-section h1 {
  font-size: 36px;
  margin-bottom: 20px;
  color: #333;
}

.hero-section p {
  font-size: 18px;
  margin-bottom: 30px;
  color: #666;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

/* Features Section */
.features-section {
  width: 100%;
  max-width: 1200px;
  padding: 40px 20px;
  margin-bottom: 30px;
}

.features-section h2 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 40px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.feature-card i {
  font-size: 40px;
  color: #FF2442;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

/* Latest Posts Section */
.latest-posts-section {
  width: 100%;
  max-width: 1200px;
  padding: 40px 20px;
  margin-bottom: 30px;
}

.latest-posts-section h2 {
  text-align: center;
  font-size: 28px;
  margin-bottom: 40px;
  color: #333;
}

.posts-preview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.post-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s;
}

.post-card:hover {
  transform: translateY(-5px);
}

.post-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.post-content {
  padding: 15px;
}

.post-text {
  margin: 0 0 10px;
  font-size: 14px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  color: #333;
}

.post-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.view-more-card {
  background-color: #f9f9f9;
  border-radius: 8px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s;
  min-height: 300px;
  border: 2px dashed #ddd;
}

.view-more-card:hover {
  background-color: #f0f0f0;
}

.view-more-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #666;
}

.view-more-content i {
  font-size: 30px;
}

.view-more-content span {
  font-size: 16px;
}
</style>