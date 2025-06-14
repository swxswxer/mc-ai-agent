<template>
  <div class="experience-page">
    <div class="experience-container">
      <!-- 页面头部 -->
      <div class="experience-header">
        <h1 class="page-title">AI 助手在线体验</h1>
        <p class="page-subtitle">体验强大的Minecraft AI助手功能</p>
        
        <!-- 模式切换 -->
        <div class="mode-switcher">
          <el-segmented v-model="currentMode" :options="modeOptions" size="large" />
        </div>
      </div>

      <!-- 聊天界面 -->
      <div class="chat-interface">
        <div class="chat-container mc-card">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <div class="chat-info">
              <div class="mode-icon">
                <el-icon :size="20"><ChatLineRound v-if="currentMode === 'chat'" /><Service v-else /></el-icon>
              </div>
              <div class="mode-details">
                <h3>{{ currentModeInfo.title }}</h3>
                <p>{{ currentModeInfo.description }}</p>
              </div>
            </div>
            <div class="chat-actions">
              <el-button @click="clearMessages" class="mc-button-green" :disabled="messages.length === 0">
                <el-icon class="button-icon"><Delete /></el-icon>
                清空对话
              </el-button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div class="messages-container" :class="{ 'has-messages': messages.length > 0 }" ref="messagesContainer">
            <div class="messages-list">
              <!-- 欢迎消息 -->
              <div v-if="messages.length === 0" class="welcome-message">
                <div class="welcome-icon">
                  <el-icon :size="48"><ChatLineRound v-if="currentMode === 'chat'" /><Service v-else /></el-icon>
                </div>
                <div class="welcome-text">
                  <h3>{{ currentModeInfo.welcomeTitle }}</h3>
                  <p>{{ currentModeInfo.welcomeText }}</p>
                  <div class="example-questions">
                    <p>您可以尝试问这些问题：</p>
                    <div class="question-chips">
                      <el-button 
                        v-for="question in currentModeInfo.examples" 
                        :key="question"
                        size="small" 
                        type="info" 
                        plain
                        @click="sendMessage(question)"
                        class="question-chip"
                        :disabled="isLoading || sseState.isActive"
                      >
                        {{ question }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 对话消息 -->
              <div v-for="(message, index) in messages" :key="index" class="message-item">
                <div v-if="message.type === 'user'" class="message user-message">
                  <div class="message-content">
                    <div class="message-text">{{ message.content }}</div>
                  </div>
                  <div class="message-avatar">
                    <el-avatar :size="40">
                      <el-icon :size="18"><User /></el-icon>
                    </el-avatar>
                  </div>
                </div>
                
                <div v-else class="message ai-message">
                  <div class="message-avatar">
                    <el-avatar :size="40" class="ai-avatar">
                      <el-icon :size="18"><ChatLineRound v-if="currentMode === 'chat'" /><Service v-else /></el-icon>
                    </el-avatar>
                  </div>
                  <div class="message-content">
                    <div class="message-text" v-html="formatMessage(message.content)"></div>
                    <div v-if="message.loading" class="typing-indicator">
                      <span></span>
                      <span></span>
                      <span></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="input-container">
            <div class="input-wrapper">
              <el-input
                v-model="currentInput"
                type="textarea"
                :rows="2"
                :maxlength="1000"
                show-word-limit
                placeholder="请输入您的问题..."
                class="mc-input message-input"
                @keydown.enter.exact.prevent="handleSendMessage"
                @keydown.enter.shift.exact="handleNewLine"
                :disabled="isLoading"
              />
              <div class="input-actions">
                <el-button 
                  type="primary" 
                  @click="handleSendMessage"
                  :loading="isLoading"
                  :disabled="!currentInput.trim()"
                  class="mc-button-green send-button"
                >
                  <el-icon class="button-icon"><Promotion /></el-icon>
                  {{ isLoading ? '发送中...' : '发送' }}
                </el-button>
              </div>
            </div>
            <div class="input-tips">
              <span><strong>提示:</strong> 按 Enter 发送，Shift+Enter 换行</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onUnmounted } from 'vue'
import { aiApi } from '../api/ai'
import { ElMessage } from 'element-plus'
import { 
  ChatLineRound, 
  Service, 
  User, 
  Delete, 
  Promotion 
} from '@element-plus/icons-vue'

// 当前模式
const currentMode = ref('chat')

// 模式选项
const modeOptions = [
  { label: '智能对话', value: 'chat' },
  { label: 'Manus智能体', value: 'agent' }
]

// 当前模式信息
const currentModeInfo = computed(() => {
  if (currentMode.value === 'chat') {
    return {
      title: '基础Chat对话',
      description: '基于强大的AI模型，为您提供Minecraft相关的智能问答服务',
      welcomeTitle: '欢迎使用基础Chat对话！',
      welcomeText: '我是您的Minecraft AI助手，可以回答关于游戏的各种问题。',
      examples: [
        '如何制作钻石镐？',
        '红石电路基础知识',
        '末影龙的击败方法',
        '自动农场制作教程'
      ]
    }
  } else {
    return {
      title: 'Manus超级智能体',
      description: '具备高级工具调用能力的智能体，提供更专业的Minecraft服务',
      welcomeTitle: '欢迎使用Manus超级智能体！',
      welcomeText: '我是Manus，具备更强大的能力，可以为您提供更专业的Minecraft指导和建议。',
      examples: [
        '分析我的建筑设计',
        '推荐适合的模组组合',
        '优化服务器性能建议',
        '创建复杂的红石装置'
      ]
    }
  }
})

// 消息列表
const messages = ref([])

// 当前输入
const currentInput = ref('')

// 加载状态
const isLoading = ref(false)

// 消息容器引用
const messagesContainer = ref(null)

// SSE连接状态管理
const sseState = ref({
  connection: null,
  isActive: false,
  isCompleted: false,
  connectionId: null,
  lastMessage: '',
  lastMessageTime: 0
})

// 生成聊天ID
const generateChatId = () => {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 格式化消息（支持简单的markdown渲染）
const formatMessage = (content) => {
  if (!content) return ''
  
  // 简单的markdown处理
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
    .replace(/\n/g, '<br>')
}

// 发送消息
const sendMessage = (text) => {
  console.log('sendMessage 被调用，文本:', text)
  console.log('当前SSE状态:', sseState.value)
  currentInput.value = text
  handleSendMessage()
}

// 清理SSE连接
const cleanupSSE = () => {
  if (sseState.value.connection) {
    console.log('清理SSE连接:', sseState.value.connectionId)
    sseState.value.connection.onerror = null
    sseState.value.connection.onmessage = null
    sseState.value.connection.onopen = null
    sseState.value.connection.close()
  }
  sseState.value.connection = null
  sseState.value.isActive = false
  sseState.value.connectionId = null
}

// 处理发送消息
const handleSendMessage = () => {
  const message = currentInput.value.trim()
  const currentTime = Date.now()
  
  // 基础检查
  if (!message || isLoading.value) {
    console.log('请求被阻止:', { message: !!message, isLoading: isLoading.value })
    return
  }
  
  // 如果有活跃的SSE连接，禁止新请求
  if (sseState.value.isActive) {
    console.log('SSE连接活跃中，阻止新请求')
    return
  }
  
  // 检查是否是重复发送（相同消息在3秒内）
  if (message === sseState.value.lastMessage && (currentTime - sseState.value.lastMessageTime) < 3000) {
    console.log('重复消息被阻止:', message, '时间差:', currentTime - sseState.value.lastMessageTime)
    return
  }
  
  // 清理可能存在的旧连接
  cleanupSSE()
  
  // 记录当前发送的消息和时间
  sseState.value.lastMessage = message
  sseState.value.lastMessageTime = currentTime
  sseState.value.isCompleted = false
  
  console.log('开始新的请求，消息:', message)

  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: message,
    timestamp: new Date()
  })

  // 添加AI消息占位符
  const aiMessageIndex = messages.value.length
  messages.value.push({
    type: 'ai',
    content: '',
    loading: true,
    timestamp: new Date()
  })

  // 清空输入框
  currentInput.value = ''

  // 开始加载
  isLoading.value = true
  
  // 滚动到底部
  scrollToBottom()

  // 生成连接ID并设置状态
  const connectionId = Date.now() + '_' + Math.random().toString(36).substr(2, 9)
  sseState.value.connectionId = connectionId
  sseState.value.isActive = true

  // 创建SSE连接
  if (currentMode.value === 'chat') {
    const chatId = generateChatId()
    sseState.value.connection = aiApi.createChatSSE(message, chatId)
  } else {
    sseState.value.connection = aiApi.createAgentSSE(message)
  }

  // 处理SSE事件
  sseState.value.connection.onmessage = (event) => {
    // 检查连接是否仍然有效
    if (!sseState.value.isActive || sseState.value.connectionId !== connectionId) {
      console.log('忽略来自无效连接的消息')
      return
    }
    try {
      const data = event.data
      
      if (data === '[DONE]') {
        // 流结束
        sseState.value.isCompleted = true
        messages.value[aiMessageIndex].loading = false
        isLoading.value = false
        console.log('SSE流完成')
        
        // 清理连接
        cleanupSSE()
        
        scrollToBottom()
        return
      }

      // 累积消息内容
      if (data && data.trim()) {
        messages.value[aiMessageIndex].content += data
        scrollToBottom()
      }
    } catch (error) {
      console.error('处理SSE消息错误:', error)
      
      // 如果已经完成，不处理错误
      if (sseState.value.isCompleted) {
        return
      }
      
      messages.value[aiMessageIndex].loading = false
      isLoading.value = false
      cleanupSSE()
    }
  }

  sseState.value.connection.onerror = (error) => {
    // 检查连接是否仍然有效
    if (!sseState.value.isActive || sseState.value.connectionId !== connectionId) {
      console.log('忽略来自无效连接的错误')
      return
    }
    
    // 如果已经完成，忽略错误
    if (sseState.value.isCompleted) {
      console.log('SSE已完成，忽略关闭时的error事件')
      return
    }
    
    // 检查是否已经收到内容
    if (messages.value[aiMessageIndex].content && messages.value[aiMessageIndex].content.trim()) {
      console.log('SSE流正常结束')
      messages.value[aiMessageIndex].loading = false
      isLoading.value = false
      sseState.value.isCompleted = true
      cleanupSSE()
      return
    }
    
    // 真正的错误情况 - 没有收到内容的连接失败
    console.error('SSE连接失败:', error)
    console.log('真正的连接错误')
    messages.value[aiMessageIndex].loading = false
    messages.value[aiMessageIndex].content = '抱歉，发生了错误，请稍后重试。'
    isLoading.value = false
    cleanupSSE()
    ElMessage.error('连接失败，请检查网络连接')
  }

  sseState.value.connection.onopen = () => {
    // 只有当前有效连接才记录
    if (sseState.value.isActive && sseState.value.connectionId === connectionId) {
      console.log('SSE连接已建立，ID:', connectionId)
    }
  }
}

// 换行处理
const handleNewLine = () => {
  currentInput.value += '\n'
}

// 清空对话
const clearMessages = () => {
  messages.value = []
  cleanupSSE()
  isLoading.value = false
  
  // 重置SSE状态
  sseState.value.isCompleted = false
  sseState.value.lastMessage = ''
  sseState.value.lastMessageTime = 0
}

// 组件销毁时清理
onUnmounted(() => {
  cleanupSSE()
})


</script>

<style scoped>
.experience-page {
  min-height: calc(100vh - 120px);
  padding: 20px;
  background: #f8f9fa;
}

.experience-container {
  max-width: 1000px;
  margin: 0 auto;
}

.experience-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin-bottom: 3px;
}

.page-subtitle {
  font-size: 1.1rem;
  color: var(--mc-gray);
  margin-bottom: 10px;
}

.mode-switcher {
  display: flex;
  justify-content: center;
}

.chat-interface {
  height: 70vh;
  display: flex;
  flex-direction: column;
}

.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  padding: 0;
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 2px solid #eee;
  background: #f8f9fa;
}

.chat-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.mode-icon {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: var(--mc-green);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.mode-icon .el-icon {
  color: white;
}

.mode-details h3 {
  margin: 0 0 5px 0;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
}

.mode-details p {
  margin: 0;
  color: var(--mc-gray);
  font-size: 0.9rem;
}

.button-icon {
  margin-right: 5px;
  font-size: 16px;
}

.messages-container {
  flex: 1;
  padding: 20px;
  background: #fafafa;
  overflow-y: hidden; /* 默认隐藏滚动 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

/* 当有消息时启用滚动 */
.messages-container.has-messages {
  overflow-y: auto;
}

.messages-container::-webkit-scrollbar {
  width: 0;
  background: transparent;
}

/* 当鼠标悬停且有消息时显示滚动条 */
.messages-container.has-messages:hover::-webkit-scrollbar {
  width: 6px;
}

.messages-container.has-messages:hover::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.messages-container.has-messages:hover::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.messages-container.has-messages:hover::-webkit-scrollbar-thumb:hover {
  background: #a1a1a1;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.welcome-message {
  text-align: center;
  padding: 20px;
  color: var(--mc-gray);
}

.welcome-icon {
  color: var(--mc-green);
  margin-bottom: 20px;
}

.welcome-icon .el-icon {
  color: var(--mc-green);
}

.welcome-text h3 {
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin-bottom: 10px;
}

.welcome-text p {
  margin-bottom: 30px;
  line-height: 1.6;
}

.example-questions p {
  font-weight: bold;
  margin-bottom: 15px;
}

.question-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.question-chip {
  font-family: var(--mc-font);
  cursor: pointer;
}

.message-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message {
  display: flex;
  gap: 15px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-content {
  flex: 1;
  background: white;
  padding: 15px;
  border-radius: 12px;
  border: 2px solid #eee;
  position: relative;
}

.user-message .message-content {
  background: var(--mc-green);
  color: white;
  border-color: var(--mc-green);
}

.ai-message .message-content {
  background: white;
  border-color: #ddd;
}

.message-text {
  line-height: 1.6;
  word-wrap: break-word;
}

.message-text :deep(code) {
  background: rgba(0,0,0,0.1);
  padding: 2px 4px;
  border-radius: 3px;
  font-family: var(--mc-font);
}

.message-avatar {
  flex-shrink: 0;
}

.message-avatar :deep(.el-avatar .el-icon) {
  font-size: 18px;
}

.ai-avatar {
  background: var(--mc-blue) !important;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  margin-top: 10px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--mc-gray);
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: scale(1);
    opacity: 0.5;
  }
  30% {
    transform: scale(1.2);
    opacity: 1;
  }
}

.input-container {
  padding: 20px;
  border-top: 2px solid #eee;
  background: white;
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
}

.message-input :deep(.el-textarea__inner) {
  resize: none;
  font-family: inherit;
  border-radius: 8px;
  border: 2px solid #ddd;
  height: 60px;
  min-height: 60px;
  max-height: 60px;
  padding: 16px;
  line-height: 1.4;
  box-sizing: border-box;
}

.message-input :deep(.el-textarea__inner:focus) {
  border-color: var(--mc-blue);
}

.send-button {
  height: 60px;
  padding: 0 24px;
  border-radius: 8px;
}

.input-tips {
  margin-top: 8px;
  font-size: 0.85rem;
  color: var(--mc-gray);
  text-align: center;
}

/* 自定义绿色按钮样式 */
:deep(.mc-button-green) {
  background: var(--mc-green) !important;
  border: 2px solid var(--mc-dark-gray) !important;
  color: white !important;
  font-family: var(--mc-font) !important;
  font-weight: bold !important;
  border-radius: 4px !important;
  padding: 12px 24px !important;
  box-shadow: 3px 3px 0px rgba(0,0,0,0.3) !important;
  transition: all 0.2s ease !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 8px !important;
  min-width: 120px !important;
}

:deep(.mc-button-green:hover) {
  background: #32cd32 !important;
  transform: translateY(-2px) !important;
  box-shadow: 4px 4px 0px rgba(0,0,0,0.4) !important;
}

:deep(.mc-button-green:active) {
  transform: translateY(1px) !important;
  box-shadow: 2px 2px 0px rgba(0,0,0,0.3) !important;
}

:deep(.mc-button-green:disabled) {
  background: #cccccc !important;
  border-color: #999999 !important;
  color: #666666 !important;
  transform: none !important;
  box-shadow: 2px 2px 0px rgba(0,0,0,0.2) !important;
  cursor: not-allowed !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .experience-page {
    padding: 10px;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .chat-interface {
    height: 80vh;
  }
  
  .chat-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .chat-info {
    width: 100%;
  }
  
  .message {
    max-width: 90%;
  }
  
  .question-chips {
    flex-direction: column;
    align-items: center;
  }
  
  .input-wrapper {
    flex-direction: column;
    align-items: stretch;
  }
  
  .send-button {
    height: 45px;
  }
}


</style> 