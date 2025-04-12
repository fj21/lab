<template>
  <router-view />
</template>

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
  background: linear-gradient(#141e30, #243b55);
  height: 100%;
  width: 100%;
  overflow: hidden;
}

#app {
  height: 100vh;
  width: 100vw;
  background: linear-gradient(#141e30, #243b55);
}
</style>
