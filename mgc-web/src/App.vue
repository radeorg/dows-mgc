<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
</script>

<template>
  <BasicLayout />
</template>

<style>
/* 全局样式优化 - 蓝色主题 */
:root {
  /* mgc主题色彩 - 浅蓝色调 */
  --primary-color: #4A90E2;
  --primary-color-light: #7EB3F5;
  --primary-lighter: #B8D7F9;
  --secondary-color: #FFFFFF;
  --primary-color-dark: #1E5CA9;
  --text-primary: #333333;
  --text-secondary: #666666;
  --background-main: #FFFFFF;
  --background-soft: #F0F7FF;
  --border-color: #E1F0FF;
  --shadow-color: rgba(74, 144, 226, 0.15);
  --gradient-primary: linear-gradient(135deg, #4A90E2, #7EB3F5);
  --gradient-soft: linear-gradient(135deg, #F0F7FF, #FFFFFF);
  --mouse-x: 50%;
  --mouse-y: 50%;
  --success-color: #52c41a;
  --warning-color: #faad14;
  --error-color: #f5222d;
}

* {
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell',
    'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow-x: hidden;
  background: var(--background-main);
  color: var(--text-primary);
  position: relative;
  transition: background-color 0.3s ease;
}

html {
  overflow-x: hidden;
  position: relative;
}

/* 全局动画基类 */
.mgc-transition {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.mgc-hover-scale:hover {
  transform: scale(1.02);
}

.mgc-hover-lift:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px var(--shadow-color);
}

/* 全局按钮样式增强 */
.ant-btn-primary {
  background: var(--gradient-primary) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(74, 144, 226, 0.3) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.ant-btn-primary:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 6px 20px rgba(74, 144, 226, 0.4) !important;
}

/* 全局鼠标跟随光效 - 影响所有页面 */
body::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
    800px circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
    rgba(74, 144, 226, 0.15) 0%,
    transparent 70%
  );
  pointer-events: none;
  z-index: 1000;
  opacity: 0;
  animation: globalLightFade 2s ease-in-out forwards;
}

@keyframes globalLightFade {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 卡片样式增强 */
.ant-card {
  border: 1px solid var(--border-color) !important;
  box-shadow: 0 2px 8px rgba(74, 144, 226, 0.08) !important;
  transition: all 0.3s ease !important;
}

.ant-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(74, 144, 226, 0.2) !important;
}
</style>
