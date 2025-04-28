/**
 * Mock API service
 * This file intercepts API calls and returns mock data
 */

import { getMockPosts, getMockPostDetail, getMockUserInfo } from './mockData';

// Flag to enable/disable mock API
const ENABLE_MOCK = false;

/**
 * Mock API handler
 * @param {string} url - API endpoint
 * @param {string} method - HTTP method
 * @param {Object} data - Request data
 * @param {Object} params - Query parameters
 * @returns {Promise} - Promise that resolves with mock data
 */
export function handleMockRequest(url, method, data, params) {
  // Skip if mock is disabled
  if (!ENABLE_MOCK) {
    return null;
  }

  console.log(`[Mock API] ${method} ${url}`, { data, params });

  // Handle different API endpoints
  if (url.startsWith('/post/section') && method.toLowerCase() === 'get') {
    return Promise.resolve(getMockPosts(params?.category, params?.lastPostId || 0));
  }

  if (url.startsWith('/post/detail/') && method.toLowerCase() === 'get') {
    const postId = url.split('/').pop();
    return Promise.resolve(getMockPostDetail(postId));
  }

  if (url === '/user/info' && method.toLowerCase() === 'get') {
    return Promise.resolve(getMockUserInfo());
  }

  // Default: return null to let the real API handle the request
  return null;
}

/**
 * Check if a request should be mocked
 * @param {string} url - API endpoint
 * @returns {boolean} - Whether the request should be mocked
 */
export function shouldMockRequest(url) {
  if (!ENABLE_MOCK) {
    return false;
  }

  // List of endpoints to mock
  const mockedEndpoints = [
    '/post/section',
    '/post/detail/',
    '/user/info'
  ];

  return mockedEndpoints.some(endpoint => url.startsWith(endpoint));
}
