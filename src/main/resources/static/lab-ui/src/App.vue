<template>
  <div id="app">
    <!-- Don't show navigation on login and register pages -->
    <Navigation v-if="!isAuthPage" />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Navigation from './components/Navigation.vue';

export default {
  name: 'App',
  components: {
    Navigation
  },
  setup() {
    const route = useRoute();

    // Check if current page is login or register
    const isAuthPage = computed(() => {
      return route.path === '/login' || route.path === '/register';
    });

    return {
      isAuthPage
    };
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  width: 100%;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
  width: 100%;
}

.main-content {
  padding-top: 20px;
  min-height: calc(100vh - 60px);
}

/* For auth pages */
.auth-page {
  background: linear-gradient(#141e30, #243b55);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
