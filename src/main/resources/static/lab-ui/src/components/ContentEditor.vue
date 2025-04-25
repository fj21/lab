<template>
  <div class="content-editor">
    <div class="editor-header">
      <h3>{{ getEditorTitle }}</h3>
    </div>
    
    <div class="editor-body">
      <el-form :model="contentForm" label-position="top">
        <el-form-item label="标题">
          <el-input v-model="contentForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        
        <el-form-item label="内容">
          <el-input 
            v-model="contentForm.content" 
            type="textarea" 
            :rows="10" 
            placeholder="请输入内容..."
          ></el-input>
        </el-form-item>
        
        <el-form-item label="上传图片">
          <el-upload
            action="/api/upload"
            :auto-upload="false"
            :limit="5"
            multiple
            :file-list="fileList"
            list-type="picture-card"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="saveContent">保存</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

// 接收类型参数
const props = defineProps({
  type: {
    type: String,
    required: true
  }
});

// 文件列表
const fileList = ref([]);

// 表单数据
const contentForm = ref({
  title: '',
  content: '',
  type: props.type
});

// 根据类型获取编辑器标题
const getEditorTitle = computed(() => {
  switch (props.type) {
    case 'research':
      return '研究方向编辑';
    case 'achievements':
      return '科研成果编辑';
    case 'team':
      return '团队介绍编辑';
    default:
      return '内容编辑';
  }
});

// 加载初始数据
onMounted(() => {
  // 在实际项目中，这里应该从后端API获取数据
  // 这里使用模拟数据
  setTimeout(() => {
    switch (props.type) {
      case 'research':
        contentForm.value = {
          title: '人工智能与机器学习研究',
          content: '本实验室主要研究方向包括深度学习、计算机视觉、自然语言处理等领域的前沿技术。我们致力于开发新的算法和模型，解决实际应用中的挑战性问题。',
          type: props.type
        };
        break;
      case 'achievements':
        contentForm.value = {
          title: '近期科研成果',
          content: '1. 在国际顶级期刊发表论文10篇\n2. 获得国家自然科学基金项目3项\n3. 开发的智能系统获得省级科技进步奖\n4. 与多家企业建立产学研合作关系',
          type: props.type
        };
        break;
      case 'team':
        contentForm.value = {
          title: '团队介绍',
          content: '我们的团队由教授、副教授、讲师、博士后、博士生和硕士生组成，形成了一个多层次、多学科交叉的研究团队。团队成员在各自领域都有深厚的研究积累和丰富的项目经验。',
          type: props.type
        };
        break;
    }
  }, 500);
});

// 保存内容
const saveContent = () => {
  if (!contentForm.value.title.trim()) {
    ElMessage.warning('请输入标题');
    return;
  }
  
  if (!contentForm.value.content.trim()) {
    ElMessage.warning('请输入内容');
    return;
  }
  
  // 在实际项目中，这里应该调用API保存数据
  // 这里使用模拟数据
  setTimeout(() => {
    ElMessage.success('保存成功');
  }, 500);
};

// 重置表单
const resetForm = () => {
  // 在实际项目中，这里应该重新从后端获取数据
  // 这里简单地清空表单
  contentForm.value = {
    title: '',
    content: '',
    type: props.type
  };
  fileList.value = [];
};
</script>

<style scoped>
.content-editor {
  width: 100%;
  background-color: #161b22;
  border-radius: 8px;
  padding: 20px;
}

.editor-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #30363d;
  padding-bottom: 10px;
}

.editor-header h3 {
  color: #e6edf3;
  margin: 0;
}

.editor-body {
  color: #e6edf3;
}

:deep(.el-form-item__label) {
  color: #8b949e;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  background-color: #0d1117;
  border-color: #30363d;
  color: #e6edf3;
}

:deep(.el-input__inner:focus),
:deep(.el-textarea__inner:focus) {
  border-color: #58a6ff;
}

:deep(.el-button) {
  background-color: #21262d;
  border-color: #30363d;
  color: #c9d1d9;
}

:deep(.el-button--primary) {
  background-color: #1f6feb;
  border-color: #1f6feb;
  color: #ffffff;
}

:deep(.el-button--primary:hover) {
  background-color: #388bfd;
  border-color: #388bfd;
}

:deep(.el-upload) {
  border: 1px dashed #30363d;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

:deep(.el-upload:hover) {
  border-color: #58a6ff;
}

:deep(.el-upload-list__item) {
  transition: all 0.5s;
}
</style>
