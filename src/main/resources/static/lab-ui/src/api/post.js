import request from '../utils/request';

/**
 * 获取帖子列表
 * @param {Number} category - 分类 0-新闻动态 1-通知公告 2-学术动态
 * @param {Number} lastPostId - 上一页最后一条帖子ID，用于分页
 * @returns {Promise} - 返回帖子列表
 */
export function getPosts(category = null, lastPostId = 0) {
  return request.get('/post/section', {
    params: {
      category,
      lastPostId
    }
  });
}

/**
 * 获取帖子详情
 * @param {Number} postId - 帖子ID
 * @returns {Promise} - 返回帖子详情
 */
export function getPostDetail(postId) {
  return request.get('/post/postDetail', {
    params: {
      postId
    }
  });
}

/**
 * 发布帖子
 * @param {Object} postData - 帖子数据
 * @param {Number} postData.type - 类型 0-图片 1-视频
 * @param {Number} postData.category - 分类 0-新闻动态 1-通知公告 2-学术动态
 * @param {Number} postData.visibility - 可见性 0-公开 1-私密
 * @param {String} postData.content - 文字内容
 * @param {File[]} postData.files - 文件列表
 * @returns {Promise} - 返回发布结果
 */
export function createPost(postData) {
  const formData = new FormData();
  formData.append('postCreateDTO', new Blob([JSON.stringify(postData)],{type: "application/json"}));
  if (postData.files && postData.files.length > 0) {
    postData.files.forEach(file => {
      formData.append('files', file);
    });
  }

  return request.post('/post/post', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 点赞帖子
 * @param {Number} postId - 帖子ID
 * @returns {Promise} - 返回点赞结果
 */
export function likePost(postId) {
  return request.post('/post/doLikePost', null, {
    params: {
      postId
    }
  });
}

/**
 * 收藏帖子
 * @param {Number} postId - 帖子ID
 * @returns {Promise} - 返回收藏结果
 */
export function collectPost(postId) {
  return request.post('/post/doCollectPost', null, {
    params: {
      postId
    }
  });
}

/**
 * 获取用户发布的帖子
 * @param {Number} authorId - 作者ID，不传则获取当前用户的帖子
 * @param {Number} lastPostId - 上一页最后一条帖子ID，用于分页
 * @returns {Promise} - 返回帖子列表
 */
export function getUserPosts(authorId = null, lastPostId = 0) {
  return request.get('/post/personPosts', {
    params: {
      authorId,
      lastPostId
    }
  });
}
