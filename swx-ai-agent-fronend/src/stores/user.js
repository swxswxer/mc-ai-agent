import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userInfo = ref(null)

  const userStatistics = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  // 计算属性
  const isLoggedIn = computed(() => !!userInfo.value)

  // 用户登录
  const login = async (loginData) => {
    try {
      const response = await userApi.login(loginData)
      userInfo.value = response.data
      token.value = 'login_success' // 后端使用session，这里只做标记
      localStorage.setItem('token', token.value)
      ElMessage.success('登录成功')
      return true
    } catch (error) {
      console.error('登录错误:', error)
      // API拦截器已经处理了错误提示，这里不需要重复提示
      return false
    }
  }

  // 用户注册
  const register = async (registerData) => {
    try {
      const response = await userApi.register(registerData)
      ElMessage.success('注册成功，请登录')
      return true
    } catch (error) {
      console.error('注册错误:', error)
      // API拦截器已经处理了错误提示，这里不需要重复提示
      return false
    }
  }

  // 用户退出
  const logout = async () => {
    try {
      await userApi.logout()
    } catch (error) {
      console.error('退出登录错误:', error)
      // 退出登录失败不影响本地状态清除
    } finally {
      userInfo.value = null
      token.value = ''
      localStorage.removeItem('token')
      ElMessage.success('已退出登录')
    }
  }

  // 检查登录状态
  const checkLoginStatus = async () => {
    try {
      const response = await userApi.getLoginUser()
      userInfo.value = response.data
      // 如果后端显示已登录，但本地没有token标记，则设置token标记
      if (!token.value) {
        token.value = 'login_success'
        localStorage.setItem('token', token.value)
      }
      return true
    } catch (error) {
      console.error('检查登录状态错误:', error)
      // 如果是权限相关错误，清除本地状态
      if (error.message && (error.message.includes('登录') || error.message.includes('权限'))) {
        userInfo.value = null
        token.value = ''
        localStorage.removeItem('token')
      }
      return false
    }
  }

  // 获取用户完整信息（用于个人中心）
  const getUserInfo = async () => {
    try {
      const response = await userApi.getUserInfo()
      userInfo.value = response.data
      return true
    } catch (error) {
      console.error('获取用户信息错误:', error)
      // API拦截器已经处理了错误提示
      return false
    }
  }

  // 生成API Key
  const generateApiKey = async () => {
    try {
      const response = await userApi.generateApiKey()
      // 更新用户信息中的API Key
      if (userInfo.value) {
        userInfo.value.apiKey = response.data
      }
      ElMessage.success('API Key生成成功')
      return response.data
    } catch (error) {
      console.error('生成API Key错误:', error)
      // API拦截器已经处理了错误提示
      return null
    }
  }


  // 获取调用统计
  const getStatisticsByMon = async () => {
    try {
      const response = await userApi.getStatisticsByMon()
      userStatistics.value = response.data
      return response.data
    } catch (error) {
      console.error('获取调用统计错误:', error)
      // API拦截器已经处理了错误提示
      return null
    }
  }

  return {
    userInfo,
    userStatistics,
    token,
    isLoggedIn,
    login,
    register,
    logout,
    checkLoginStatus,
    getUserInfo,
    generateApiKey,
    getStatisticsByMon
  }
}, {
  persist: true
}) 