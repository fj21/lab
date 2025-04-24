<template>
  <div class="post-grid-container">
    <div class="post-grid-header">
      <h2>{{ title }}</h2>
      <div class="post-grid-tabs" v-if="showCategories">
        <div 
          v-for="category in categories" 
          :key="category.id"
          :class="['category-tab', { active: selectedCategory === category.id }]"
          @click="selectCategory(category.id)"
        >
          {{ category.name }}
        </div>
      </div>
    </div>
    
    <div class="post-grid">
      <div 
        v-for="post in posts" 
        :key="post.id" 
        class="post-card"
        @click="viewPost(post.id)"
      >
        <div class="post-image">
          <img :src="post.coverUrl" :alt="post.content" />
          <div class="post-type-badge" v-if="post.mediaType === 1">
            <i class="el-icon-video-camera"></i>
          </div>
        </div>
        <div class="post-content">
          <p class="post-text">{{ post.content }}</p>
          <div class="post-info">
            <div class="user-info">
              <img :src="post.userBasicVO?.image" :alt="post.userBasicVO?.username" class="user-avatar" />
              <span class="username">{{ post.userBasicVO?.username }}</span>
            </div>
            <div class="post-stats">
              <span class="stat-item">
                <i class="el-icon-view"></i>
                {{ formatNumber(post.viewCount || 0) }}
              </span>
              <span class="stat-item">
                <i class="el-icon-star-off"></i>
                {{ formatNumber(post.likeCount || 0) }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="post-card create-card" v-if="showCreateCard" @click="createPost">
        <div class="create-content">
          <i class="el-icon-plus"></i>
          <span>发布新内容</span>
        </div>
      </div>
    </div>
    
    <div class="load-more" v-if="hasMore && posts.length > 0" @click="loadMore">
      <span>加载更多</span>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'PostGrid',
  props: {
    title: {
      type: String,
      default: '发现'
    },
    posts: {
      type: Array,
      default: () => []
    },
    categories: {
      type: Array,
      default: () => [
        { id: 0, name: '全部' },
        { id: 1, name: '研究成果' },
        { id: 2, name: '设备资源' },
        { id: 3, name: '实验室生活' },
        { id: 4, name: '学术交流' }
      ]
    },
    showCategories: {
      type: Boolean,
      default: true
    },
    showCreateCard: {
      type: Boolean,
      default: true
    },
    hasMore: {
      type: Boolean,
      default: true
    }
  },
  emits: ['load-more', 'category-change', 'view-post', 'create-post'],
  setup(props, { emit }) {
    const router = useRouter();
    const selectedCategory = ref(0);
    
    const selectCategory = (categoryId) => {
      selectedCategory.value = categoryId;
      emit('category-change', categoryId);
    };
    
    const loadMore = () => {
      emit('load-more');
    };
    
    const viewPost = (postId) => {
      emit('view-post', postId);
      router.push(`/post/detail/${postId}`);
    };
    
    const createPost = () => {
      emit('create-post');
      router.push('/post/create');
    };
    
    const formatNumber = (num) => {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + 'w';
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k';
      }
      return num;
    };
    
    return {
      selectedCategory,
      selectCategory,
      loadMore,
      viewPost,
      createPost,
      formatNumber
    };
  }
};
</script>

<style scoped>
.post-grid-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.post-grid-header {
  margin-bottom: 20px;
}

.post-grid-header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 15px;
}

.post-grid-tabs {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  overflow-x: auto;
  padding-bottom: 5px;
}

.category-tab {
  padding: 6px 16px;
  border-radius: 18px;
  background-color: #f5f5f5;
  color: #666;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap;
  transition: all 0.3s;
}

.category-tab:hover {
  background-color: #eee;
}

.category-tab.active {
  background-color: #FF2442;
  color: white;
}

.post-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.post-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.post-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.post-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.post-card:hover .post-image img {
  transform: scale(1.05);
}

.post-type-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
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
  height: 63px; /* Approximately 3 lines */
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

.username {
  font-size: 12px;
  color: #666;
}

.post-stats {
  display: flex;
  gap: 10px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #999;
}

.create-card {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  border: 2px dashed #ddd;
  min-height: 300px;
}

.create-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #666;
}

.create-content i {
  font-size: 30px;
  color: #FF2442;
}

.load-more {
  margin-top: 30px;
  text-align: center;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  cursor: pointer;
  color: #666;
  transition: background-color 0.3s;
}

.load-more:hover {
  background-color: #eee;
}
</style>
