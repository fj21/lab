<template>
  <div class="auth-container">
    <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
      <div class="lab-title">CQU LAB</div>
      <div class="auth-form-container">
        <div class="auth-form">
          <h2 class="auth-title">用户登录</h2>
          <form @submit.prevent="handleLogin" style="width: 100%; display: flex; flex-direction: column; align-items: center;">
            <div class="auth-input">
              <el-input
                v-model="loginForm.phone"
                placeholder=" "
                :prefix-icon="User"
              />
              <div class="auth-form-label">手机号</div>
            </div>

            <div class="auth-input">
              <el-input
                v-model="loginForm.password"
                placeholder=" "
                type="password"
                :prefix-icon="Lock"
              />
              <div class="auth-form-label">密码</div>
            </div>

            <div class="auth-error" v-if="loginError">{{ loginError }}</div>

            <div class="button-container">
              <el-button
                class="auth-button active-glow"
                type="primary"
                @click="handleLogin"
                :loading="loading"
              >
                登录
              </el-button>
            </div>
          </form>

          <div class="auth-toggle">
            还没有账号？ <router-link to="/register">立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { login, getUserInfo } from '../api/user';
import '../assets/auth.css';

// 响应式数据
const loginForm = reactive({
  phone: '',
  password: ''
});

const loading = ref(false);
const loginError = ref('');
const router = useRouter();

// 登录处理
const handleLogin = async () => {
  // 表单验证
  if (!loginForm.phone) {
    loginError.value = '请输入手机号';
    return;
  }
  if (!loginForm.password) {
    loginError.value = '请输入密码';
    return;
  }

  // 手机号格式验证
  const phoneReg = /^1[3-9]\d{9}$/;
  if (!phoneReg.test(loginForm.phone)) {
    loginError.value = '手机号格式不正确';
    return;
  }

  try {
    loading.value = true;
    loginError.value = '';

    // 调用登录API
    const response = await login(loginForm);

    // 从响应中获取token和userId
    console.log('Login response:', response.data);

    if (response.data && response.data.token) {
      // 保存token
      localStorage.setItem('token', response.data.token);
      console.log('Token stored:', response.data.token);

      // 保存userId
      if (response.data.userId) {
        localStorage.setItem('userId', response.data.userId);
        console.log('User ID stored:', response.data.userId);
      } else {
        console.warn('No userId in login response');

        // 尝试从用户信息接口获取userId
        try {
          const { data } = await getUserInfo();
          if (data && data.id) {
            localStorage.setItem('userId', data.id);
            console.log('User ID retrieved from getUserInfo:', data.id);
          }
        } catch (err) {
          console.error('Failed to get user ID from getUserInfo:', err);
        }
      }
    } else {
      console.error('Invalid login response format:', response);
    }

    ElMessage.success('登录成功');

    // 如果有重定向参数，跳转到对应页面，否则跳转到首页
    const redirect = router.currentRoute.value.query.redirect;
    if (redirect) {
      console.log('Login successful, redirecting to:', redirect);

      // Add a small delay to ensure token is properly stored
      setTimeout(() => {
        router.push(redirect);
      }, 300);
    } else {
      console.log('Login successful, redirecting to home page');

      // Add a small delay to ensure token is properly stored
      setTimeout(() => {
        router.push('/home');
      }, 300);
    }
  } catch (error) {
    console.error('登录失败:', error);
    loginError.value = error.message || '登录失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 样式已在auth.css中定义 */
</style>