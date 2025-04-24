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
    component: () => import('../views/EnhancedHome.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/home-original',
    name: 'HomeOriginal',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/labIntro',
    name: 'LabIntro',
    component: () => import('../views/LabIntro.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/teams',
    name: 'Teams',
    component: () => import('../views/Teams.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('../views/News.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/education',
    name: 'Education',
    component: () => import('../views/Education.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
  },
  {
    path: '/science',
    name: 'Science',
    component: () => import('../views/Science.vue'),
    meta: { requiresAuth: false } // 不需要登录也能访问
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
  routes,
  // Add scrollBehavior to handle scroll position when navigating
  scrollBehavior(_to, _from, savedPosition) {
    // If the user used browser navigation buttons, restore the position
    if (savedPosition) {
      return savedPosition;
    }
    // Otherwise, scroll to top
    return { top: 0 };
  }
});

// 全局前置守卫
router.beforeEach((to, _from, next) => {
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

// Add global error handler for navigation failures
router.onError((error) => {
  console.error('Navigation error:', error);
  // You could redirect to an error page or handle the error in another way
});

export default router;