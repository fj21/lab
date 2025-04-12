<template>
  <div class="auth-container register-page">
    <div style="display: flex; flex-direction: column; align-items: center; width: 100%;">
      <div class="lab-title">CQU LAB</div>
      <div class="auth-form-container">
        <div class="auth-form">
          <h2 class="auth-title">用户注册</h2>
          <form @submit.prevent="handleRegister" style="width: 100%; display: flex; flex-direction: column; align-items: center;">
            <div class="auth-input">
              <el-input 
                v-model="registerForm.phone" 
                placeholder=" " 
                :prefix-icon="Phone"
              />
              <div class="auth-form-label">手机号</div>
            </div>
            
            <div class="auth-input">
              <el-input 
                v-model="registerForm.username" 
                placeholder=" " 
                :prefix-icon="User"
              />
              <div class="auth-form-label">用户名</div>
            </div>
            
            <div class="auth-input">
              <el-input 
                v-model="registerForm.password" 
                placeholder=" "
                type="password" 
                :prefix-icon="Lock"
              />
              <div class="auth-form-label">密码</div>
            </div>
            
            <div class="auth-input">
              <el-input 
                v-model="confirmPassword" 
                placeholder=" "
                type="password" 
                :prefix-icon="Lock"
              />
              <div class="auth-form-label">确认密码</div>
            </div>
            
            <div class="auth-error" v-if="registerError">{{ registerError }}</div>
            
            <div class="button-container">
              <el-button 
                class="auth-button active-glow" 
                type="primary" 
                @click="handleRegister" 
                :loading="loading"
              >
                注册
              </el-button>
            </div>
          </form>
          
          <div class="auth-toggle">
            已有账号？ <router-link to="/login">返回登录</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock, Phone } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { register } from '../api/user';
import '../assets/auth.css';

// 响应式数据
const registerForm = reactive({
  phone: '',
  username: '',
  password: ''
});

const confirmPassword = ref('');
const loading = ref(false);
const registerError = ref('');
const router = useRouter();

// 注册处理
const handleRegister = async () => {
  // 表单验证
  if (!registerForm.phone) {
    registerError.value = '请输入手机号';
    return;
  }
  if (!registerForm.username) {
    registerError.value = '请输入用户名';
    return;
  }
  if (!registerForm.password) {
    registerError.value = '请输入密码';
    return;
  }
  if (!confirmPassword.value) {
    registerError.value = '请确认密码';
    return;
  }
  
  // 密码一致性验证
  if (registerForm.password !== confirmPassword.value) {
    registerError.value = '两次输入的密码不一致';
    return;
  }
  
  // 手机号格式验证
  const phoneReg = /^1[3-9]\d{9}$/;
  if (!phoneReg.test(registerForm.phone)) {
    registerError.value = '手机号格式不正确';
    return;
  }
  
  // 密码强度验证
  if (registerForm.password.length < 6) {
    registerError.value = '密码长度不能少于6位';
    return;
  }
  
  try {
    loading.value = true;
    registerError.value = '';
    
    // 调用注册API
    await register(registerForm);
    
    ElMessage.success('注册成功，请登录');
    
    // 跳转到登录页
    router.push('/login');
  } catch (error) {
    console.error('注册失败:', error);
    registerError.value = error.message || '注册失败，请稍后再试';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 样式已在auth.css中定义 */
</style> 