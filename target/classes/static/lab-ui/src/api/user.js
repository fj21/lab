import request from '../utils/request';

/**
 * 用户登录
 * @param {Object} data - 登录信息
 * @param {string} data.phone - 手机号
 * @param {string} data.password - 密码
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  });
}

/**
 * 用户注册
 * @param {Object} data - 注册信息
 * @param {string} data.phone - 手机号
 * @param {string} data.username - 用户名
 * @param {string} data.password - 密码
 * @returns {Promise}
 */
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  });
}

/**
 * 获取当前用户信息
 * @returns {Promise}
 */
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  });
}

/**
 * 获取用户基本信息
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getUserBasicInfo(userId) {
  return request({
    url: `/user/basic/${userId}`,
    method: 'get'
  });
}

/**
 * 更新用户信息
 * @param {Object} data - 更新信息
 * @param {string} [data.username] - 用户名
 * @param {string} [data.image] - 头像URL
 * @param {string} [data.signature] - 个性签名
 * @returns {Promise}
 */
export function updateUserInfo(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  });
} 