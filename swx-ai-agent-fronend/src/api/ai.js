import api from './index'

// 获取完整的API URL
const getApiBaseURL = () => {
  if (import.meta.env.DEV) {
    return '/api' // 开发环境使用相对路径
  } else {
    // 生产环境使用完整URL或者相对路径
    return window.location.origin + '/api'
  }
}

export const aiApi = {
  // 创建SSE连接进行Chat对话
  createChatSSE(message, chatId) {
    const baseURL = getApiBaseURL()
    const url = `${baseURL}/ai/chat?message=${encodeURIComponent(message)}&chatId=${chatId}`
    
    return new EventSource(url, { 
      withCredentials: true
    })
  },

  // 创建SSE连接进行Agent对话
  createAgentSSE(message) {
    const baseURL = getApiBaseURL()
    const url = `${baseURL}/ai/agent?message=${encodeURIComponent(message)}`
    
    return new EventSource(url, { 
      withCredentials: true
    })
  }
} 