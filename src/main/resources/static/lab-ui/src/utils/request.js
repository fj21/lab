import axios from 'axios';
import { ElMessage } from 'element-plus';
import { handleMockRequest, shouldMockRequest } from './mockApi';

// 创建axios实例
const request = axios.create({
  baseURL: '/api', // API基础路径
  timeout: 15000, // 请求超时时间增加到15秒
  withCredentials: true, // 允许跨域请求携带凭证
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    console.log('Sending request:', {
      url: config.url,
      method: config.method,
      baseURL: config.baseURL,
      data: config.data,
      params: config.params,
      headers: config.headers
    });

    // 从localStorage获取token，添加到请求头
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      config.headers['token'] = token; // Add token as a separate header
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
    console.log('Response received:', response);

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
    console.error('错误详情:', {
      message: error.message,
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data,
      config: error.config
    });

    let message = '网络错误，请稍后再试';

    if (error.message && error.message.includes('Network Error')) {
      message = '网络连接失败，请检查服务器是否正常运行';
      console.error('网络连接失败，可能的原因：');
      console.error('1. 后端服务器未启动');
      console.error('2. 后端服务器端口配置不正确');
      console.error('3. 跨域问题未解决');
      console.error('4. 防火墙或网络问题');
    } else if (error.response?.data?.message) {
      message = error.response.data.message;
    } else if (error.message) {
      message = error.message;
    }

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
    // if (shouldMockRequest(url)) {
    //   const mockResponse = handleMockRequest(url, 'get', null, config.params);
    //   if (mockResponse) {
    //     console.log(`[Mock API] Using mock data for GET ${url}`);
    //     return mockResponse;
    //   }
    // }
    return request.get(url, config);
  },

  async post(url, data, config = {}) {
    try {
      console.log(`[API] Sending POST request to ${url}`, { data, config });

      // Add additional debugging for registration requests
      if (url.includes('/register')) {
        console.log('Registration request details:', {
          url: url,
          fullUrl: `/api${url}`,
          data: data,
          headers: config.headers || 'Using default headers'
        });
      }

      const response = await request.post(url, data, config);
      console.log(`[API] POST ${url} response:`, response);
      return response;
    } catch (error) {
      console.error(`[API] POST ${url} error:`, error);
      console.error('Error details:', {
        message: error.message,
        status: error.response?.status,
        statusText: error.response?.statusText,
        data: error.response?.data,
        config: error.config
      });
      throw error;
    }
  },

  put(url, data, config = {}) {
    // 先尝试使用模拟数据
    // if (shouldMockRequest(url)) {
    //   const mockResponse = handleMockRequest(url, 'put', data, config.params);
    //   if (mockResponse) {
    //     console.log(`[Mock API] Using mock data for PUT ${url}`);
    //     return mockResponse;
    //   }
    // }
    return request.put(url, data, config);
  },

  delete(url, config = {}) {
    // 先尝试使用模拟数据
    // if (shouldMockRequest(url)) {
    //   const mockResponse = handleMockRequest(url, 'delete', null, config.params);
    //   if (mockResponse) {
    //     console.log(`[Mock API] Using mock data for DELETE ${url}`);
    //     return mockResponse;
    //   }
    // }
    return request.delete(url, config);
  }
};

export default http;