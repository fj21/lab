import { createRouter, createWebHistory } from 'vue-router';
import { ElMessage } from 'element-plus';
import AdminDashboard from '../views/AdminDashboard.vue'

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
  {
    path: '/posts/detail/:id',
    name: 'PostsDetail',
    component: () => import('../views/PostsDetail.vue'),
    meta: { requiresAuth: false } // 允许未登录用户访问
  },
  {
    path: '/resources',
    name: 'Resources',
    component: () => import('../views/Resources.vue'),
    meta: { requiresAuth: false } // 允许未登录用户访问
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: {
      requiresAuth: false,
      requiresAdmin: false
    }
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    component: () => import('../views/UserProfile.vue'),
    meta: { requiresAuth: true } // 需要登录才能访问
  }
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
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const isAuthenticated = !!token;
  const isAdmin = localStorage.getItem('userRole') === 'admin';
  const userId = localStorage.getItem('userId');

  console.log('Router guard:', {
    to: to.path,
    from: from.path,
    isAuthenticated,
    hasUserId: !!userId,
    requiresAuth: to.matched.some(record => record.meta.requiresAuth),
    requiresAdmin: to.matched.some(record => record.meta.requiresAdmin)
  });

  // Special case for login and register pages
  if (to.path === '/login' || to.path === '/register') {
    if (isAuthenticated) {
      console.log('Already authenticated, redirecting to home');
      next('/home');
    } else {
      next();
    }
    return;
  }

  // Check for admin routes
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (!isAuthenticated) {
      console.log('Redirecting to login: authentication required for admin page');
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else if (!isAdmin) {
      console.log('User is not an admin, redirecting to home');
      ElMessage.error('您没有权限访问该页面');
      next('/home');
    } else {
      next();
    }
  }
  // Check for authenticated routes
  else if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      console.log('Redirecting to login: authentication required');
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    } else {
      // Check if we have a userId, which is required for most authenticated operations
      if (!userId && to.path !== '/home') {
        console.warn('No userId found in localStorage, attempting to fetch user info');
        // We could try to fetch user info here, but for now just continue
      }
      next();
    }
  }
  // Public routes
  else {
    next();
  }
})

// Add global error handler for navigation failures
router.onError((error) => {
  console.error('Navigation error:', error);
  // You could redirect to an error page or handle the error in another way
});

export default router;

