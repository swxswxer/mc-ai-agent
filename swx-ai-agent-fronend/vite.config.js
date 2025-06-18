import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig(({ command, mode }) => {
  const isDev = command === 'serve'
  
  console.log(`🚀 运行模式: ${isDev ? '开发环境' : '生产环境'}`)
  
  return {
    plugins: [
      vue(),
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
    define: {
      // 定义全局常量，在生产环境中使用相对路径，开发环境使用代理
      __API_BASE_URL__: isDev ? '"/api"' : '"/api"'
    },
    server: {
      port: 3000,
      host: '0.0.0.0', // 允许外部访问
      proxy: {
        '/api': {
          target: 'http://localhost:8123',
          changeOrigin: true,
          secure: false,
          configure: (proxy, options) => {
            proxy.on('error', (err, req, res) => {
              console.log('❌ 代理错误:', err)
            })
            proxy.on('proxyReq', (proxyReq, req, res) => {
              console.log(`🔄 代理请求: ${req.method} ${req.url} -> ${options.target}${req.url}`)
            })
            proxy.on('proxyRes', (proxyRes, req, res) => {
              console.log(`✅ 代理响应: ${req.url} - ${proxyRes.statusCode}`)
            })
          }
        }
      }
    },
    build: {
      // 生产环境构建配置
      outDir: 'dist',
      assetsDir: 'assets'
    }
  }
}) 