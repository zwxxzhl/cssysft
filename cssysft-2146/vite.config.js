import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
const path = require('path');

// https://vitejs.dev/config/
export default defineConfig({
  // optimizeDeps: {
  //   include: ['bpmn-js-properties-panel','inherits']
  // },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    }
  },
  // 与全局变量相同 VITE_PRE_URL
  base: '/dispatchweb/'
})
