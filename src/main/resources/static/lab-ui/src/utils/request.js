import axios from 'axios';
import { ElMessage } from 'element-plus';
import { handleMockRequest, shouldMockRequest } from './mockApi';

// 创建axios实例
const request = axios.create({
  baseURL: '/api', // API基础路径
  timeout: 5000, // 请求超时时间
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从localStorage获取token，添加到请求头
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    // 如果是模拟数据的请求，添加标记
    if (shouldMockRequest(config.url)) {
      config.mock = true;
    }

    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data;

    // 如果返回的状态码不是200，则判断为错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败');

      // 401: 未登录或token过期
      if (res.code === 401) {
        // 清除登录信息
        localStorage.removeItem('token');
        window.location.href = '/login';
      }

      return Promise.reject(new Error(res.message || '请求失败'));
    } else {
      return res;
    }
  },
  (error) => {
    console.error('响应错误:', error);
    const message = error.response?.data?.message || '网络错误，请稍后再试';
    ElMessage.error(message);

    // 处理401未授权
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }

    return Promise.reject(error);
  }
);

// 封装请求方法
const http = {
  get(url, config = {}) {
    // 先尝试使用模拟数据
    if (shouldMockRequest(url)) {
      const mockResponse = handleMockRequest(url, 'get', null, config.params);
      if (mockResponse) {
        console.log(`[Mock API] Using mock data for GET ${url}`);
        return mockResponse;
      }
    }
    return request.get(url, config);
  },

  post(url, data, config = {}) {
    // 先尝试使用模拟数据
    if (shouldMockRequest(url)) {
      const mockResponse = handleMockRequest(url, 'post', data, config.params);
      if (mockResponse) {
        console.log(`[Mock API] Using mock data for POST ${url}`);
        return mockResponse;
      }
    }
    return request.post(url, data, config);
  },

  put(url, data, config = {}) {
    // 先尝试使用模拟数据
    if (shouldMockRequest(url)) {
      const mockResponse = handleMockRequest(url, 'put', data, config.params);
      if (mockResponse) {
        console.log(`[Mock API] Using mock data for PUT ${url}`);
        return mockResponse;
      }
    }
    return request.put(url, data, config);
  },

  delete(url, config = {}) {
    // 先尝试使用模拟数据
    if (shouldMockRequest(url)) {
      const mockResponse = handleMockRequest(url, 'delete', null, config.params);
      if (mockResponse) {
        console.log(`[Mock API] Using mock data for DELETE ${url}`);
        return mockResponse;
      }
    }
    return request.delete(url, config);
  }
};

export default http;