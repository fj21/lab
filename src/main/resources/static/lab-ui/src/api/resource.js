import request from '../utils/request';

/**
 * 获取资源列表
 * @param {Number} category - 分类 1-书籍 2-实验指南 3-视频教程 4-软件工具
 * @param {Number} lastResourceId - 上一页最后一条资源ID，用于分页
 * @returns {Promise} - 返回资源列表
 */
export function getResources(category = null, lastResourceId = 0) {
  return request.get('/api/resource/list', {
    params: {
      category,
      lastResourceId
    }
  });
}

/**
 * 获取资源详情
 * @param {Number} resourceId - 资源ID
 * @returns {Promise} - 返回资源详情
 */
export function getResourceDetail(resourceId) {
  return request.get('/api/resource/detail', {
    params: {
      resourceId
    }
  });
}

/**
 * 上传资源
 * @param {Number} type - 类型 1-书籍 2-实验指南 3-视频教程 4-软件工具
 * @param {String} title - 标题
 * @param {String} description - 描述
 * @param {String} content - 内容
 * @param {File} coverFile - 封面文件
 * @param {File} resourceFile - 资源文件
 * @returns {Promise} - 返回上传结果
 */
export function uploadResource(type, title, description, content, coverFile, resourceFile) {
  const formData = new FormData();
  formData.append('type', type);
  formData.append('title', title);
  formData.append('description', description);
  formData.append('content', content);
  
  if (coverFile) {
    formData.append('coverFile', coverFile);
  }
  
  if (resourceFile) {
    formData.append('resourceFile', resourceFile);
  }
  
  return request.post('/api/resource/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/**
 * 更新资源
 * @param {Number} resourceId - 资源ID
 * @param {Object} data - 更新数据
 * @returns {Promise} - 返回更新结果
 */
export function updateResource(resourceId, data) {
  return request.put(`/api/resource/${resourceId}`, data);
}

/**
 * 删除资源
 * @param {Number} resourceId - 资源ID
 * @returns {Promise} - 返回删除结果
 */
export function deleteResource(resourceId) {
  return request.delete(`/api/resource/${resourceId}`);
}
