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
    // 从localStorage获取token，添加到请求头
    const token = localStorage.getItem('token');

    console.log('Request interceptor:', {
      url: config.url,
      method: config.method,
      token: token ? localStorage.getItem('token') : 'Not found',
      userId: localStorage.getItem('userId') || 'Not found'
    });

    if (token) {
      // Add token to headers in multiple formats to ensure compatibility
      config.headers['Authorization'] = `Bearer ${token}`;
      config.headers['token'] = token;

      // Add userId to the header if available
      const userId = localStorage.getItem('userId');
      if (userId) {
        config.headers['userId'] = userId;
      }

      console.log('Token added to request headers:', {
        'Authorization': `Bearer ${token}`,
        'token': token,
        'userId': userId || 'Not available'
      });
    } else {
      console.warn('No token found in localStorage for request:', config.url);
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
    console.log('Response received for URL:', response.config.url, 'Status:', response.status);
    console.log('Response Data:',response.data);
    // 如果返回的状态码不是200，则判断为错误
    if (res.code !== 200) {
      console.error('API error response:', {
        url: response.config.url,
        code: res.code,
        message: res.message
      });

      ElMessage.error(res.message || '请求失败');

      // 401: 未登录或token过期
      if (res.code === 401) {
        console.warn('Authentication error (401) detected, clearing token and redirecting to login');
        // 清除登录信息
        // localStorage.removeItem('token');
        // localStorage.removeItem('userId');

        // Use router instead of direct window location change
        setTimeout(() => {
          window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
        }, 100);
      }

      return Promise.reject(new Error(res.message || '请求失败'));
    } else {
      return res;
    }
  },
  (error) => {
    console.error('Response error:', error);
    console.error('Error details:', {
      url: error.config?.url,
      message: error.message,
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data
    });

    let message = '网络错误，请稍后再试';

    if (error.message && error.message.includes('Network Error')) {
      message = '网络连接失败，请检查服务器是否正常运行';
      console.error('Network connection failed, possible reasons:');
      console.error('1. Backend server not started');
      console.error('2. Incorrect backend server port configuration');
      console.error('3. CORS issues');
      console.error('4. Firewall or network issues');
    } else if (error.response?.data?.message) {
      message = error.response.data.message;
    } else if (error.message) {
      message = error.message;
    }

    // Don't show error message for CSRF errors to avoid confusing the user
    if (!(error.response?.data?.code === 'BAD_CSRF_REQUEST')) {
      ElMessage.error(message);
    } else {
      console.warn('CSRF error detected, but not showing error message to user');
    }

    // 只处理401未授权，不处理403 CSRF错误
    if (error.response?.status === 401) {
      console.warn('Authentication error (401) detected in error handler, clearing token and redirecting to login');
      localStorage.removeItem('token');
      localStorage.removeItem('userId');

      // Redirect with the current path as the redirect parameter
      setTimeout(() => {
        window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
      }, 100);
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