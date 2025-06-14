import { createApp, nextTick } from 'vue'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { useUserStore } from './stores/user'
import './styles/global.css'

const app = createApp(App)
const pinia = createPinia()

pinia.use(createPersistedState())

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(pinia)
app.use(router)
app.use(ElementPlus)

// 应用启动后检查登录状态
app.mount('#app')

// 在应用挂载后检查登录状态
nextTick(() => {
  const userStore = useUserStore()
  userStore.checkLoginStatus().catch(error => {
    console.error('初始化登录状态检查失败:', error)
  })
}) 