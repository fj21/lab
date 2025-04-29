<template>
  <div class="user-profile-container">
    <div class="profile-header">
      <h1>个人资料</h1>
      <div class="profile-actions">
        <el-button type="primary" @click="handleSave" :loading="saving">保存更改</el-button>
      </div>
    </div>

    <div class="profile-content">
      <div class="profile-sidebar">
        <div class="avatar-section">
          <el-avatar :size="120" :src="userForm.avatar || 'https://randomuser.me/api/portraits/men/1.jpg'"></el-avatar>
          <el-upload
            class="avatar-uploader"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleAvatarChange"
          >
            <el-button size="small" type="primary">更换头像</el-button>
          </el-upload>
        </div>
        <div class="user-stats">
          <div class="stat-item">
            <div class="stat-label">发布帖子</div>
            <div class="stat-value">{{ userStats.postCount || 0 }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">获得点赞</div>
            <div class="stat-value">{{ userStats.likeCount || 0 }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">注册时间</div>
            <div class="stat-value">{{ formatDate(userInfo.createdAt) }}</div>
          </div>
        </div>
      </div>

      <div class="profile-details">
        <el-form :model="userForm" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="userForm.phone" disabled></el-input>
            <div class="form-hint">手机号不可修改</div>
          </el-form-item>
          <el-form-item label="个性签名">
            <el-input
              v-model="userForm.signature"
              type="textarea"
              :rows="4"
              placeholder="请输入个性签名"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="user-posts">
      <h2>我的帖子</h2>
      <div v-if="userPosts.length > 0" class="posts-grid">
        <div v-for="post in userPosts" :key="post.id" class="post-card" @click="viewPostDetail(post.id)">
          <div class="post-image" v-if="post.coverUrl">
            <img :src="post.coverUrl" alt="帖子封面" />
          </div>
          <div class="post-content">
            <div class="post-title">{{ post.title }}</div>
            <div class="post-date">{{ post.date }}</div>
          </div>
        </div>
      </div>
      <div v-else class="no-posts">
        <el-empty description="暂无帖子"></el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getUserInfo, updateUserInfo } from '../api/user';
import { getUserPosts } from '../api/post';

const router = useRouter();
const userInfo = ref({});
const userForm = reactive({
  username: '',
  phone: '',
  signature: '',
  avatar: ''
});
const userStats = reactive({
  postCount: 0,
  likeCount: 0
});
const userPosts = ref([]);
const saving = ref(false);
const avatarFile = ref(null);

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '未知';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN').replace(/\//g, '-');
};

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    console.log('UserProfile: Fetching user info...');
    console.log('UserProfile: Token:', localStorage.getItem('token'));
    console.log('UserProfile: UserId:', localStorage.getItem('userId'));

    const response = await getUserInfo();
    console.log('UserProfile: User info response:', response);

    if (response.code === 200 && response.data) {
      userInfo.value = response.data;

      // Store userId in localStorage if not already there
      if (!localStorage.getItem('userId') && response.data.id) {
        localStorage.setItem('userId', response.data.id);
        console.log('UserProfile: User ID stored:', response.data.id);
      }

      // 填充表单
      userForm.username = response.data.username || '';
      userForm.phone = response.data.phone || '';
      userForm.signature = response.data.signature || '';
      userForm.avatar = response.data.avatar || '';

      // 填充统计数据
      userStats.postCount = response.data.postCount || 0;
      userStats.likeCount = response.data.likeCount || 0;
    } else {
      console.error('UserProfile: Failed to get user info, response code:', response.code);
      ElMessage.error('获取用户信息失败');
      // 如果获取失败，可能是未登录，跳转到登录页
      router.push('/login');
    }
  } catch (error) {
    console.error('UserProfile: Failed to fetch user info:', error);
    console.error('UserProfile: Error details:', error.message);
    ElMessage.error('获取用户信息失败，请稍后重试');

    // Clear token and redirect to login
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    router.push('/login');
  }
};

// 获取用户帖子
const fetchUserPosts = async () => {
  try {
    const response = await getUserPosts();
    if (response.code === 200 && response.data) {
      userPosts.value = response.data.postVOList.map(post => ({
        id: post.id,
        title: post.content.split('\n')[0] || '无标题',
        date: formatDate(post.createdAt),
        coverUrl: post.coverUrl
      }));
    }
  } catch (error) {
    console.error('Failed to fetch user posts:', error);
    ElMessage.error('获取用户帖子失败，请稍后重试');
  }
};

// 处理头像上传
const handleAvatarChange = (file) => {
  avatarFile.value = file.raw;

  // 预览头像
  const reader = new FileReader();
  reader.onload = (e) => {
    userForm.avatar = e.target.result;
  };
  reader.readAsDataURL(file.raw);
};

// 保存用户信息
const handleSave = async () => {
  saving.value = true;
  try {
    // 构建更新数据
    const updateData = {
      username: userForm.username,
      signature: userForm.signature
    };

    // 如果有新头像，需要上传
    if (avatarFile.value) {
      // 这里应该有上传头像的逻辑，然后将返回的URL赋值给updateData.avatar
      // 由于没有实现文件上传，这里只是模拟
      updateData.avatar = userForm.avatar;
    }

    const response = await updateUserInfo(updateData);
    if (response.code === 200) {
      ElMessage.success('保存成功');
      // 刷新用户信息
      await fetchUserInfo();
    } else {
      ElMessage.error(response.message || '保存失败');
    }
  } catch (error) {
    console.error('Failed to save user info:', error);
    ElMessage.error('保存失败，请稍后重试');
  } finally {
    saving.value = false;
  }
};

// 查看帖子详情
const viewPostDetail = (postId) => {
  router.push({
    path: '/posts/detail/' + postId
  });
};

// 生命周期钩子
onMounted(async () => {
  console.log('UserProfile: Component mounted');

  // 检查是否登录
  const token = localStorage.getItem('token');
  console.log('UserProfile: Token from localStorage:', token);

  if (!token) {
    console.log('UserProfile: No token found, redirecting to login');
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }

  // Log user ID if available
  const userId = localStorage.getItem('userId');
  console.log('UserProfile: UserId from localStorage:', userId);

  try {
    console.log('UserProfile: Fetching user data...');
    // 获取用户信息和帖子
    await fetchUserInfo();
    await fetchUserPosts();
    console.log('UserProfile: Data fetched successfully');
  } catch (error) {
    console.error('UserProfile: Failed to initialize user profile:', error);
    console.error('UserProfile: Error details:', error.message);
    ElMessage.error('加载用户资料失败，请稍后重试');
  }
});
</script>

<style scoped>
.user-profile-container {
  max-width: 1200px;
  margin: 80px auto 40px;
  padding: 0 20px;
  color: #e6edf3;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #30363d;
}

.profile-header h1 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

.profile-content {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
}

.profile-sidebar {
  width: 300px;
  background-color: #161b22;
  border-radius: 8px;
  padding: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
}

.user-stats {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #30363d;
}

.stat-label {
  color: #8b949e;
}

.stat-value {
  font-weight: bold;
}

.profile-details {
  flex: 1;
  background-color: #161b22;
  border-radius: 8px;
  padding: 30px;
}

.form-hint {
  font-size: 12px;
  color: #8b949e;
  margin-top: 5px;
}

.user-posts {
  margin-top: 40px;
}

.user-posts h2 {
  font-size: 22px;
  margin-bottom: 20px;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.post-card {
  background-color: #161b22;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.post-image {
  height: 160px;
  overflow: hidden;
}

.post-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.post-content {
  padding: 15px;
}

.post-title {
  font-weight: bold;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.post-date {
  font-size: 12px;
  color: #8b949e;
}

.no-posts {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

/* Element Plus overrides */
:deep(.el-input__inner), :deep(.el-textarea__inner) {
  background-color: #0d1117;
  border-color: #30363d;
  color: #e6edf3;
}

:deep(.el-form-item__label) {
  color: #8b949e;
}

/* Responsive styles */
@media (max-width: 992px) {
  .profile-content {
    flex-direction: column;
  }

  .profile-sidebar {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .posts-grid {
    grid-template-columns: 1fr;
  }
}
</style>
