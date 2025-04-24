<template>
  <div class="post-create-container">
    <div class="post-create-header">
      <div class="back-button" @click="goBack">
        <i class="el-icon-arrow-left"></i> 返回
      </div>
      <h1>发布内容</h1>
    </div>
    
    <div class="post-create-form">
      <el-form :model="postForm" label-position="top">
        <el-form-item label="内容分类">
          <el-radio-group v-model="postForm.category">
            <el-radio-button :label="0">新闻</el-radio-button>
            <el-radio-button :label="1">设备</el-radio-button>
            <el-radio-button :label="2">师生</el-radio-button>
            <el-radio-button :label="3">生活</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="可见性">
          <el-radio-group v-model="postForm.visibility">
            <el-radio-button :label="0">公开</el-radio-button>
            <el-radio-button :label="1">仅实验室成员可见</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="内容类型">
          <el-radio-group v-model="postForm.type">
            <el-radio-button :label="0">图片</el-radio-button>
            <el-radio-button :label="1">视频</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="文字内容">
          <el-input 
            v-model="postForm.content" 
            type="textarea" 
            :rows="5" 
            placeholder="分享你的研究成果、学术见解或日常感想..."
          ></el-input>
        </el-form-item>
        
        <el-form-item label="上传媒体">
          <el-upload
            :action="uploadAction"
            :auto-upload="false"
            :limit="postForm.type === 0 ? 9 : 1"
            :multiple="postForm.type === 0"
            :file-list="fileList"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            list-type="picture-card"
            :class="{ 'hide-upload-button': fileList.length >= (postForm.type === 0 ? 9 : 1) }"
          >
            <i class="el-icon-plus"></i>
            <template #tip>
              <div class="el-upload__tip">
                {{ postForm.type === 0 ? '最多上传9张图片' : '上传一个视频文件' }}
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item>
          <div class="form-actions">
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" @click="submitPost" :loading="submitting">发布</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const submitting = ref(false);
const fileList = ref([]);

// 表单数据
const postForm = reactive({
  type: 0, // 0-图片 1-视频
  category: 0, // 0-新闻 1-设备 2-师生 3-生活
  visibility: 0, // 0-公开 1-仅实验室成员可见
  content: ''
});

// 上传地址（实际项目中应该是后端API地址）
const uploadAction = computed(() => {
  return '/api/upload'; // 这里应该是实际的上传地址
});

// 返回上一页
const goBack = () => {
  if (hasUnsavedChanges()) {
    ElMessageBox.confirm('您有未保存的内容，确定要离开吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.back();
    }).catch(() => {});
  } else {
    router.back();
  }
};

// 检查是否有未保存的更改
const hasUnsavedChanges = () => {
  return postForm.content.trim() !== '' || fileList.value.length > 0;
};

// 处理文件变化
const handleFileChange = (file, fileList) => {
  // 如果是视频类型，只保留最新上传的一个文件
  if (postForm.type === 1 && fileList.length > 1) {
    const latestFile = fileList[fileList.length - 1];
    fileList.value = [latestFile];
  } else {
    fileList.value = fileList;
  }
};

// 处理文件移除
const handleFileRemove = (file, fileList) => {
  fileList.value = fileList;
};

// 提交表单
const submitPost = async () => {
  // 表单验证
  if (!postForm.content.trim()) {
    ElMessage.warning('请输入文字内容');
    return;
  }
  
  if (fileList.value.length === 0) {
    ElMessage.warning('请上传至少一个媒体文件');
    return;
  }
  
  try {
    submitting.value = true;
    
    // 在实际项目中，这里应该调用API上传文件和创建帖子
    // 这里使用模拟数据
    
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    ElMessage.success('发布成功');
    router.push('/posts');
  } catch (error) {
    console.error('发布失败:', error);
    ElMessage.error('发布失败，请稍后再试');
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>
.post-create-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding: 20px;
  padding-top: 80px; /* Space for fixed navigation */
}

.post-create-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #8b949e;
  margin-right: 20px;
  transition: color 0.3s;
}

.back-button:hover {
  color: #58a6ff;
}

.post-create-header h1 {
  font-size: 24px;
  color: #e6edf3;
  margin: 0;
}

.post-create-form {
  max-width: 800px;
  margin: 0 auto;
  background-color: #161b22;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 20px;
}

/* Hide upload button when limit is reached */
.hide-upload-button :deep(.el-upload--picture-card) {
  display: none;
}

/* Element Plus overrides */
:deep(.el-form-item__label) {
  color: #8b949e;
}

:deep(.el-radio-button__inner) {
  background-color: #21262d;
  border-color: #30363d;
  color: #8b949e;
}

:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #8957e5;
  border-color: #8957e5;
  color: #fff;
  box-shadow: -1px 0 0 0 #8957e5;
}

:deep(.el-textarea__inner) {
  background-color: #21262d;
  border-color: #30363d;
  color: #e6edf3;
}

:deep(.el-upload--picture-card) {
  background-color: #21262d;
  border-color: #30363d;
  color: #8b949e;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  background-color: #21262d;
  border-color: #30363d;
}

:deep(.el-button) {
  background-color: #21262d;
  border-color: #30363d;
  color: #8b949e;
}

:deep(.el-button--primary) {
  background-color: #8957e5;
  border-color: #8957e5;
  color: #fff;
}

:deep(.el-button--primary:hover) {
  background-color: #9e6ff0;
  border-color: #9e6ff0;
}

:deep(.el-button:hover) {
  background-color: #30363d;
  border-color: #8b949e;
  color: #e6edf3;
}

:deep(.el-upload__tip) {
  color: #8b949e;
}
</style>
