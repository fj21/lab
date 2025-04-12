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
      <h2>欢迎来到实验室门户系统</h2>
      <p>这是首页内容区域，更多功能敬请期待...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserInfo } from '../api/user';

const userInfo = ref(null);
const router = useRouter();

// 页面加载时获取用户信息
onMounted(async () => {
  try {
    const res = await getUserInfo();
    userInfo.value = res.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
});

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
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background-color: #141e30;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #03e9f4;
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
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(#243b55, #141e30);
  color: white;
}

h2 {
  margin-bottom: 20px;
  font-size: 28px;
  color: #03e9f4;
}

p {
  font-size: 16px;
  margin-bottom: 20px;
}
</style> 