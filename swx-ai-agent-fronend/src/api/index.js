import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '../router'
import { 
  getErrorMessage, 
  isLoginRequired, 
  isSilentError, 
  getSpecialHandling,
  getErrorType,
  ERROR_TYPES 
} from '../utils/errorHandler'

// 根据环境获取API基础URL
const getBaseURL = () => {
  // 开发环境：使用代理到 localhost:8123
  // 生产环境：使用相对路径，通过nginx代理
  if (import.meta.env.DEV) {
    return '/api' // 开发环境通过 vite proxy 代理到 localhost:8123
  } else {
    return '/api' // 生产环境通过 nginx 代理到后端服务器
  }
}

// 创建axios实例
const api = axios.create({
  baseURL: getBaseURL(),
  timeout: 30000,
  withCredentials: true // 支持携带cookie，用于session认证
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等认证信息
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

// 处理业务错误的函数
const handleBusinessError = (data) => {
  const { code, message } = data
  const errorMessage = message || getErrorMessage(code)
  
  // 检查是否需要静默处理
  if (isSilentError(code)) {
    return Promise.reject(new Error(errorMessage))
  }
  
  // 检查是否需要跳转登录
  if (isLoginRequired(code)) {
    ElMessage.warning(errorMessage)
    localStorage.removeItem('token')
    router.push('/auth')
    return Promise.reject(new Error(errorMessage))
  }
  
  // 检查是否需要特殊处理
  const specialHandling = getSpecialHandling(code)
  if (specialHandling) {
    switch (specialHandling.type) {
      case 'redirect':
        ElMessageBox.confirm(
          specialHandling.message,
          '提示',
          {
            confirmButtonText: '去处理',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          router.push(specialHandling.path)
        }).catch(() => {
          // 用户取消，不做处理
        })
        break
      case 'info':
        ElMessage.info(specialHandling.message)
        break
      case 'warning':
        ElMessage.warning(specialHandling.message)
        break
      default:
        ElMessage.error(errorMessage)
    }
    return Promise.reject(new Error(errorMessage))
  }
  
  // 根据错误类型显示不同类型的消息
  const errorType = getErrorType(code)
  switch (errorType) {
    case ERROR_TYPES.PERMISSION:
      ElMessage.warning(errorMessage)
      break
    case ERROR_TYPES.VALIDATION:
      ElMessage.warning(errorMessage)
      break
    default:
      ElMessage.error(errorMessage)
  }
  
  console.error('业务错误:', data)
  return Promise.reject(new Error(errorMessage))
}

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    const data = response.data
    
    // 如果响应数据符合后端统一格式
    if (data && typeof data.code !== 'undefined') {
      // 成功响应 (code = 0)
      if (data.code === 0) {
        return data
      }
      
      // 业务错误 (code != 0)
      return handleBusinessError(data)
    }
    
    // 如果不是统一格式，直接返回
    return data
  },
  (error) => {
    console.error('响应错误:', error)
    
    // 网络错误或HTTP状态码错误
    if (error.response) {
      const { status, data } = error.response
      
      // 如果后端返回了统一格式的错误
      if (data && typeof data.code !== 'undefined') {
        handleBusinessError(data)
        return Promise.reject(error)
      }
      
      // HTTP状态码错误处理
      switch (status) {
        case 401:
          ElMessage.warning('请先登录')
          localStorage.removeItem('token')
          router.push('/auth')
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 429:
          ElMessage.error('请求过于频繁，请稍后重试')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        case 502:
          ElMessage.error('网关错误')
          break
        case 503:
          ElMessage.error('服务暂时不可用')
          break
        default:
          ElMessage.error(`网络错误 (${status})`)
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.message === 'Network Error') {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error('网络错误')
    }
    
    return Promise.reject(error)
  }
)

export default api 