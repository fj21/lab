<template>
  <div class="posts-detail-container">
    <div class="posts-detail-content">
      <div class="posts-header">
        <div class="back-button" @click="goBack">
          <i class="el-icon-arrow-left"></i> 返回
        </div>
      </div>
      
      <div class="posts-main" v-if="post">
        <div class="posts-left">
          <div class="posts-body">
            <div class="posts-text">{{ post.content }}</div>
            
            <div class="posts-media">
              <div v-for="(media, index) in post.mediaList" :key="index" class="media-item">
                <img :src="media.url" :alt="`Media ${index + 1}`" />
              </div>
            </div>
          </div>
          
          <div class="posts-actions">
            <div class="action-button" @click="handleLike">
              <i :class="['el-icon-star-on', { 'liked': isLiked }]"></i>
              <span>{{ post.likeCount }}</span>
            </div>
            <div class="action-button" @click="handleCollect">
              <i :class="['el-icon-collection-tag', { 'collected': isCollected }]"></i>
              <span>{{ post.collectCount }}</span>
            </div>
            <div class="action-button" @click="focusCommentInput">
              <i class="el-icon-chat-dot-round"></i>
              <span>{{ post.commentCount }}</span>
            </div>
            <div class="action-button">
              <i class="el-icon-share"></i>
              <span>分享</span>
            </div>
          </div>
        </div>
        
        <div class="posts-right">
          <div class="author-card">
            <img :src="post.userBasicVO.image" :alt="post.userBasicVO.username" class="author-avatar" />
            <div class="author-details">
              <div class="author-name">{{ post.userBasicVO.username }}</div>
              <div class="author-follow">
                <el-button type="primary" size="small" class="follow-button">关注</el-button>
              </div>
            </div>
            <div class="post-date">{{ formatDate(post.createdAt) }}</div>
            <div class="author-bio">{{ post.userBasicVO.bio || '这个人很懒，什么都没有留下。' }}</div>
          </div>
          
          <div class="comments-section">
            <h3>评论 ({{ post.commentCount }})</h3>
            
            <div class="comment-input">
              <img :src="userAvatar" alt="Your avatar" class="comment-avatar" />
              <el-input
                v-model="commentText"
                placeholder="发表评论..."
                :rows="2"
                type="textarea"
                ref="commentInputRef"
              />
              <el-button type="primary" @click="submitComment" :disabled="!commentText.trim()">发送</el-button>
            </div>
            
            <div class="comments-list">
              <div v-for="comment in comments" :key="comment.id" class="comment-item">
                <img :src="comment.userAvatar" :alt="comment.username" class="comment-avatar" />
                <div class="comment-content">
                  <div class="comment-header">
                    <span class="comment-username">{{ comment.username }}</span>
                    <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
                  </div>
                  <div class="comment-text">{{ comment.content }}</div>
                  <div class="comment-actions">
                    <span @click="replyToComment(comment)">回复</span>
                    <span v-if="comment.replies && comment.replies.length" @click="toggleReplies(comment)">
                      {{ comment.showReplies ? '收起回复' : `查看回复(${comment.replies.length})` }}
                    </span>
                  </div>
                  
                  <div class="comment-replies" v-if="comment.showReplies && comment.replies && comment.replies.length">
                    <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                      <img :src="reply.userAvatar" :alt="reply.username" class="reply-avatar" />
                      <div class="reply-content">
                        <div class="reply-header">
                          <span class="reply-username">{{ reply.username }}</span>
                          <span class="reply-date">{{ formatDate(reply.createdAt) }}</span>
                        </div>
                        <div class="reply-text">
                          <span class="reply-to">@{{ reply.replyTo }}</span> {{ reply.content }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="load-more-comments" v-if="hasMoreComments">
                <el-button @click="loadMoreComments" :loading="loadingComments">加载更多评论</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const postId = computed(() => route.params.id);

// Fake post data
const post = ref(null);
const comments = ref([]);
const commentText = ref('');
const commentInputRef = ref(null);
const isLiked = ref(false);
const isCollected = ref(false);
const hasMoreComments = ref(true);
const loadingComments = ref(false);
const userAvatar = ref('https://randomuser.me/api/portraits/men/1.jpg');

// Fetch post data
onMounted(() => {
  // Simulate API call
  setTimeout(() => {
    // Fake post data
    post.value = {
      id: postId.value,
      content: '人工智能研究团队最新研究成果：基于深度学习的图像识别系统取得突破性进展。我们的团队经过六个月的努力，成功开发了一种新型的图像识别算法，该算法在标准测试集上的准确率达到了98.7%，超过了当前业界的最高水平。这一成果已被提交到顶级学术会议CVPR 2024，并有望获得接收。',
      coverUrl: 'https://picsum.photos/id/1/800/400',
      userBasicVO: {
        username: '张教授',
        image: 'https://randomuser.me/api/portraits/men/1.jpg',
        bio: '人工智能研究院教授，主要研究方向为计算机视觉与深度学习。已在顶级期刊和会议上发表论文50余篇，获得国家自然科学基金多项。'
      },
      mediaList: [
        { url: 'https://picsum.photos/id/1/800/400' },
        { url: 'https://picsum.photos/id/2/800/400' },
        { url: 'https://picsum.photos/id/3/800/400' }
      ],
      likeCount: 42,
      collectCount: 18,
      commentCount: 7,
      createdAt: new Date('2023-11-15')
    };
    
    // Fake comments
    comments.value = [
      {
        id: 1,
        username: '李研究员',
        userAvatar: 'https://randomuser.me/api/portraits/women/2.jpg',
        content: '恭喜团队取得这一重要突破！期待在会议上看到详细的论文。',
        createdAt: new Date('2023-11-15T10:30:00'),
        showReplies: false,
        replies: [
          {
            id: 101,
            username: '张教授',
            userAvatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            replyTo: '李研究员',
            content: '谢谢支持，我们会继续努力完善这一算法。',
            createdAt: new Date('2023-11-15T11:15:00')
          },
          {
            id: 102,
            username: '王助理',
            userAvatar: 'https://randomuser.me/api/portraits/men/3.jpg',
            replyTo: '李研究员',
            content: '感谢关注，论文已经在修改中，很快会完成最终版本。',
            createdAt: new Date('2023-11-15T14:20:00')
          }
        ]
      },
      {
        id: 2,
        username: '赵博士',
        userAvatar: 'https://randomuser.me/api/portraits/women/4.jpg',
        content: '这个准确率非常令人印象深刻，能否分享一下你们是如何解决过拟合问题的？',
        createdAt: new Date('2023-11-16T09:45:00'),
        showReplies: false,
        replies: [
          {
            id: 201,
            username: '张教授',
            userAvatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            replyTo: '赵博士',
            content: '我们主要采用了改进的正则化技术和数据增强方法，可以私下详细交流。',
            createdAt: new Date('2023-11-16T10:30:00')
          }
        ]
      },
      {
        id: 3,
        username: '刘教授',
        userAvatar: 'https://randomuser.me/api/portraits/men/5.jpg',
        content: '希望能够安排一次研讨会，详细介绍这项技术的实现细节。',
        createdAt: new Date('2023-11-17T15:20:00'),
        showReplies: false,
        replies: []
      }
    ];
  }, 500);
});

// Format date
const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleString('zh-CN');
};

// Go back
const goBack = () => {
  router.back();
};

// Handle like
const handleLike = () => {
  if (!post.value) return;
  
  if (!isLiked.value) {
    post.value.likeCount++;
    isLiked.value = true;
    ElMessage.success('点赞成功');
  } else {
    post.value.likeCount--;
    isLiked.value = false;
    ElMessage.success('已取消点赞');
  }
};

// Handle collect
const handleCollect = () => {
  if (!post.value) return;
  
  if (!isCollected.value) {
    post.value.collectCount++;
    isCollected.value = true;
    ElMessage.success('收藏成功');
  } else {
    post.value.collectCount--;
    isCollected.value = false;
    ElMessage.success('已取消收藏');
  }
};

// Focus comment input
const focusCommentInput = () => {
  if (commentInputRef.value) {
    commentInputRef.value.focus();
  }
};

// Submit comment
const submitComment = () => {
  if (!commentText.value.trim() || !post.value) return;
  
  // Add new comment
  const newComment = {
    id: Date.now(),
    username: '当前用户',
    userAvatar: userAvatar.value,
    content: commentText.value,
    createdAt: new Date(),
    showReplies: false,
    replies: []
  };
  
  comments.value.unshift(newComment);
  post.value.commentCount++;
  commentText.value = '';
  
  ElMessage.success('评论发布成功');
};

// Reply to comment
const replyToComment = (comment) => {
  commentText.value = `@${comment.username} `;
  focusCommentInput();
};

// Toggle replies
const toggleReplies = (comment) => {
  comment.showReplies = !comment.showReplies;
};

// Load more comments
const loadMoreComments = () => {
  loadingComments.value = true;
  
  // Simulate API call
  setTimeout(() => {
    // Add more fake comments
    const moreComments = [
      {
        id: 4,
        username: '陈研究员',
        userAvatar: 'https://randomuser.me/api/portraits/women/6.jpg',
        content: '这项技术在医学图像分析领域有很大的应用潜力，期待后续的发展。',
        createdAt: new Date('2023-11-18T11:10:00'),
        showReplies: false,
        replies: []
      },
      {
        id: 5,
        username: '吴同学',
        userAvatar: 'https://randomuser.me/api/portraits/men/7.jpg',
        content: '作为一名学生，非常期待能够参与到这类前沿研究中。请问实验室是否有相关的招生计划？',
        createdAt: new Date('2023-11-19T16:45:00'),
        showReplies: false,
        replies: [
          {
            id: 501,
            username: '张教授',
            userAvatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            replyTo: '吴同学',
            content: '我们实验室一直欢迎优秀的学生加入，可以关注我们的招生信息或直接联系我。',
            createdAt: new Date('2023-11-19T17:30:00')
          }
        ]
      }
    ];
    
    comments.value = [...comments.value, ...moreComments];
    loadingComments.value = false;
    
    // After loading once, no more comments
    hasMoreComments.value = false;
  }, 1000);
};
</script>

<style scoped>
.posts-detail-container {
  min-height: 100vh;
  background-color: #0d1117;
  color: #e6edf3;
  padding: 20px;
  padding-top: 80px; /* Space for fixed navigation */
}

.posts-detail-content {
  max-width: 1200px;
  margin: 0 auto;
}

.posts-header {
  padding: 20px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #8b949e;
  transition: color 0.3s;
}

.back-button:hover {
  color: #58a6ff;
}

.posts-main {
  display: flex;
  gap: 20px;
}

.posts-left {
  flex: 1;
  background-color: #161b22;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.posts-right {
  width: 350px;
}

.posts-body {
  padding: 20px;
}

.posts-text {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 20px;
  white-space: pre-line;
}

.posts-media {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
}

.media-item img {
  width: 100%;
  border-radius: 4px;
  transition: transform 0.3s;
  cursor: pointer;
}

.media-item img:hover {
  transform: scale(1.02);
}

.posts-actions {
  display: flex;
  gap: 20px;
  padding: 15px 20px;
  border-top: 1px solid #30363d;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #8b949e;
  transition: color 0.3s;
}

.action-button:hover {
  color: #58a6ff;
}

.action-button i.liked {
  color: #f85149;
}

.action-button i.collected {
  color: #8957e5;
}

.author-card {
  background-color: #161b22;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-bottom: 15px;
}

.author-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.author-name {
  font-weight: bold;
  font-size: 1.1rem;
}

.follow-button {
  background-color: #f85149;
  border-color: #f85149;
}

.follow-button:hover {
  background-color: #ff6b64;
  border-color: #ff6b64;
}

.post-date {
  font-size: 0.8rem;
  color: #8b949e;
  margin-bottom: 15px;
}

.author-bio {
  font-size: 0.9rem;
  line-height: 1.5;
  color: #c9d1d9;
}

.comments-section {
  background-color: #161b22;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.comments-section h3 {
  margin-bottom: 20px;
  color: #e6edf3;
}

.comment-input {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 30px;
}

.comment-avatar, .reply-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.comment-input .el-input {
  margin: 10px 0;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 10px;
}

.comment-content {
  flex: 1;
  background-color: #1c2128;
  border-radius: 8px;
  padding: 15px;
}

.comment-header, .reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.comment-username, .reply-username {
  font-weight: bold;
  color: #e6edf3;
}

.comment-date, .reply-date {
  font-size: 12px;
  color: #8b949e;
}

.comment-text, .reply-text {
  margin-bottom: 10px;
  line-height: 1.5;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.comment-actions span {
  color: #8b949e;
  cursor: pointer;
  font-size: 12px;
}

.comment-actions span:hover {
  color: #58a6ff;
}

.comment-replies {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.reply-item {
  display: flex;
  gap: 10px;
}

.reply-content {
  flex: 1;
  background-color: #21262d;
  border-radius: 8px;
  padding: 10px;
}

.reply-to {
  color: #58a6ff;
  margin-right: 5px;
}

.load-more-comments {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* Element Plus overrides */
:deep(.el-textarea__inner) {
  background-color: #21262d;
  border-color: #30363d;
  color: #e6edf3;
}

:deep(.el-button) {
  background-color: #8957e5;
  border-color: #8957e5;
  color: #fff;
}

:deep(.el-button:hover) {
  background-color: #9e6ff0;
  border-color: #9e6ff0;
}

/* Responsive design */
@media (max-width: 768px) {
  .posts-main {
    flex-direction: column;
  }
  
  .posts-right {
    width: 100%;
  }
}
</style>
