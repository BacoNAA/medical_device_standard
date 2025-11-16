import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    host: '127.0.0.1',  // 明确使用 IPv4 地址
    port: 3000,
    open: true
  }
})
