import api from './index'

export const aiApi = {
  // 创建SSE连接进行Chat对话
  createChatSSE(message, chatId) {
    const url = `/api/ai/chat?message=${encodeURIComponent(message)}&chatId=${chatId}`
    return new EventSource(url, { withCredentials: true })
  },

  // 创建SSE连接进行Agent对话
  createAgentSSE(message) {
    const url = `/api/ai/agent?message=${encodeURIComponent(message)}`
    return new EventSource(url, { withCredentials: true })
  }
} 