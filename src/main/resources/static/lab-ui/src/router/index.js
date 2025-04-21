import { createRouter, createWebHistory } from 'vue-router';

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true } // 游客可访问
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true } // 游客可访问
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true } // 需要登录才能访问
  },
  {
    path: '/posts',
    name: 'PostWall',
    component: () => import('../views/PostWall.vue'),
    meta: { requiresAuth: false } // 允许未登录用户访问
  },
  {
    path: '/post/detail/:id',
    name: 'PostDetail',
    component: () => import('../views/PostDetail.vue'),
    meta: { requiresAuth: false } // 允许未登录用户访问
  },
  {
    path: '/post/create',
    name: 'PostCreate',
    component: () => import('../views/PostCreate.vue'),
    meta: { requiresAuth: true } // 需要登录才能访问
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');

  // 需要认证但未登录，跳转到登录页
  if (to.matched.some(record => record.meta.requiresAuth) && !token) {
    next({ name: 'Login' });
  }
  // 已登录用户访问游客页面，跳转到首页
  else if (to.matched.some(record => record.meta.guest) && token) {
    next({ name: 'Home' });
  }
  // 其他情况正常通过
  else {
    next();
  }
});

export default router;