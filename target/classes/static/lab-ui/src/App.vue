<template>
  <div id="app">
    <!-- Don't show navigation on login and register pages -->
    <Navigation v-if="!isAuthPage" />
    <div class="main-content">
      <!-- Add key to router-view to force component re-creation when route changes -->
      <router-view v-slot="{ Component }">
        <keep-alive>
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Navigation from './components/Navigation.vue';

const route = useRoute();

// Check if current page is login or register
const isAuthPage = computed(() => {
  return route.path === '/login' || route.path === '/register';
});
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
  background-color: #0d1117;
  overflow-y: auto;
  color: rgba(255, 255, 255, 0.87);
}

#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  width: 100%;
  padding-top: 60px; /* Height of the navigation bar */
  min-height: calc(100vh - 60px);
  position: relative;
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
