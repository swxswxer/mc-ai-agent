<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card mc-card">
        <div class="auth-header">
          <div class="logo-section">
            <svg width="80" height="80" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" class="logo-icon">
              <defs>
                <pattern id="authGrass" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                  <rect width="4" height="4" fill="#5CB85C"/>
                  <rect x="0" y="0" width="1" height="1" fill="#4A994A"/>
                  <rect x="2" y="1" width="1" height="1" fill="#6BC46B"/>
                  <rect x="1" y="2" width="1" height="1" fill="#4A994A"/>
                  <rect x="3" y="3" width="1" height="1" fill="#4A994A"/>
                </pattern>
                <pattern id="authDirt" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                  <rect width="4" height="4" fill="#8B4513"/>
                  <rect x="1" y="0" width="1" height="1" fill="#A0522D"/>
                  <rect x="0" y="2" width="1" height="1" fill="#654321"/>
                  <rect x="3" y="1" width="1" height="1" fill="#A0522D"/>
                  <rect x="2" y="3" width="1" height="1" fill="#654321"/>
                </pattern>
              </defs>
              <g transform="translate(4, 4)">
                <polygon points="0,8 12,2 24,8 12,14" fill="url(#authGrass)" stroke="#2F2F2F" stroke-width="1"/>
                <polygon points="0,8 12,14 12,26 0,20" fill="url(#authDirt)" stroke="#2F2F2F" stroke-width="1"/>
                <polygon points="12,14 24,8 24,20 12,26" fill="url(#authDirt)" stroke="#2F2F2F" stroke-width="1" opacity="0.7"/>
              </g>
            </svg>
            <span class="logo-text">MC AI</span>
            <h1 class="auth-title">
              {{ isLogin ? '用户登录' : '用户注册' }}
            </h1>
          </div>
        </div>

        <div class="auth-form">
          <!-- 登录表单 -->
          <el-form 
            v-if="isLogin"
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            @submit.prevent="handleLogin"
          >
            <el-form-item prop="userAccount">
              <el-input
                v-model="loginForm.userAccount"
                placeholder="请输入用户账号"
                size="large"
                class="mc-input"
                prefix-icon="User"
              />
            </el-form-item>
            <el-form-item prop="userPassword">
              <el-input
                v-model="loginForm.userPassword"
                type="password"
                placeholder="请输入密码"
                size="large"
                class="mc-input"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                style="width: 100%"
                :loading="loading"
                @click="handleLogin"
                class="mc-button primary"
              >
                {{ loading ? '登录中...' : '登录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 注册表单 -->
          <el-form 
            v-else
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            @submit.prevent="handleRegister"
          >
            <el-form-item prop="userAccount">
              <el-input
                v-model="registerForm.userAccount"
                placeholder="请输入用户账号"
                size="large"
                class="mc-input"
                prefix-icon="User"
              />
            </el-form-item>
            
            <el-form-item prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="请输入邮箱地址"
                size="large"
                class="mc-input"
                prefix-icon="Message"
              />
            </el-form-item>
            
            <el-form-item prop="verificationCode" class="verification-code-item">
              <div class="verification-code-container">
                <div class="verification-input-wrapper">
                  <el-input
                    v-model="registerForm.verificationCode"
                    placeholder="请输入验证码"
                    size="large"
                    class="mc-input verification-input"
                    prefix-icon="Key"
                  />
                </div>
                <div class="send-code-btn-wrapper">
                  <el-button
                    type="primary"
                    size="large"
                    :disabled="countdown > 0 || !isValidEmail(registerForm.email)"
                    :loading="sendingCode"
                    @click="sendVerificationCode"
                    class="send-code-btn"
                  >
                    {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
                  </el-button>
                </div>
              </div>
            </el-form-item>
            
            <el-form-item prop="userPassword">
              <el-input
                v-model="registerForm.userPassword"
                type="password"
                placeholder="请输入密码"
                size="large"
                class="mc-input"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            <el-form-item prop="checkPassword">
              <el-input
                v-model="registerForm.checkPassword"
                type="password"
                placeholder="请确认密码"
                size="large"
                class="mc-input"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                style="width: 100%"
                :loading="loading"
                @click="handleRegister"
                class="mc-button primary"
              >
                {{ loading ? '注册中...' : '注册' }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="auth-footer">
          <el-divider>
            <span style="color: #999;">{{ isLogin ? '还没有账号？' : '已有账号？' }}</span>
          </el-divider>
          <el-button 
            type="info" 
            text 
            size="large"
            @click="toggleMode"
            class="toggle-button"
          >
            {{ isLogin ? '立即注册' : '立即登录' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { userApi } from '../api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const loading = ref(false)
const sendingCode = ref(false)
const countdown = ref(0)
const loginFormRef = ref(null)
const registerFormRef = ref(null)

// 登录表单
const loginForm = reactive({
  userAccount: '',
  userPassword: ''
})

// 注册表单
const registerForm = reactive({
  userAccount: '',
  email: '',
  verificationCode: '',
  userPassword: '',
  checkPassword: ''
})

// 邮箱格式验证
const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// 登录表单验证规则
const loginRules = {
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, message: '账号长度至少4位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, message: '账号长度至少4位', trigger: 'blur' },
    { max: 20, message: '账号长度不能超过20位', trigger: 'blur' },
    { 
      pattern: /^[a-zA-Z0-9_]+$/, 
      message: '账号只能包含字母、数字和下划线', 
      trigger: 'blur' 
    }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (!isValidEmail(value)) {
          callback(new Error('请输入有效的邮箱地址'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  verificationCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码长度为4位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度至少8位', trigger: 'blur' },
    { max: 20, message: '密码长度不能超过20位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.userPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

// 发送验证码
const sendVerificationCode = async () => {
  if (!registerForm.email || !isValidEmail(registerForm.email)) {
    ElMessage.error('请输入有效的邮箱地址')
    return
  }

  sendingCode.value = true
  try {
    const response = await userApi.sendRegisterCode(registerForm.email)
    if (response.code === 0) {
      ElMessage.success('验证码发送成功，请查收邮件')
      startCountdown()
    } else {
      ElMessage.error(response.message || '发送验证码失败')
    }
  } catch (error) {
    console.error('发送验证码失败:', error)
    ElMessage.error('发送验证码失败，请稍后重试')
  } finally {
    sendingCode.value = false
  }
}

// 开始倒计时
const startCountdown = () => {
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

// 切换登录/注册模式
const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 清空表单
  if (isLogin.value) {
    Object.assign(loginForm, { userAccount: '', userPassword: '' })
    loginFormRef.value?.clearValidate()
  } else {
    Object.assign(registerForm, { 
      userAccount: '', 
      email: '', 
      verificationCode: '', 
      userPassword: '', 
      checkPassword: '' 
    })
    registerFormRef.value?.clearValidate()
    countdown.value = 0
  }
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const success = await userStore.login(loginForm)
    if (success) {
      // 登录成功，跳转到首页
      router.push('/')
    }
  } finally {
    loading.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const success = await userStore.register(registerForm)
    if (success) {
      // 注册成功，切换到登录模式
      isLogin.value = true
      // 预填充账号
      loginForm.userAccount = registerForm.userAccount
      // 清空注册表单
      Object.assign(registerForm, { 
        userAccount: '', 
        email: '', 
        verificationCode: '', 
        userPassword: '', 
        checkPassword: '' 
      })
      countdown.value = 0
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 450px;
}

.auth-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px;
  border-radius: 8px;
  box-shadow: 8px 8px 0px rgba(0,0,0,0.3);
}

.auth-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  display: block;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.3));
}

.logo-text {
  color: white;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.8);
  font-size: 18px;
}

.auth-title {
  font-size: 2rem;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin: 0;
}

.auth-form {
  margin-bottom: 30px;
}

.auth-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.auth-form :deep(.el-form-item:not(.verification-code-item) .el-input__wrapper) {
  border-radius: 4px;
  border: 2px solid #ddd;
  box-shadow: none;
  transition: border-color 0.3s;
  height: 48px !important;
}

.auth-form :deep(.el-form-item:not(.verification-code-item) .el-input__wrapper:hover) {
  border-color: var(--mc-green);
}

.auth-form :deep(.el-form-item:not(.verification-code-item) .el-input__wrapper.is-focus) {
  border-color: var(--mc-blue);
  box-shadow: 0 0 0 2px rgba(79, 148, 205, 0.2);
}

.auth-form :deep(.el-input__inner) {
  font-family: var(--mc-font);
}

.verification-code-container {
  display: flex;
  gap: 12px;
  width: 100%;
  align-items: center;
}

.verification-input-wrapper {
  flex: 1;
}

.verification-input-wrapper :deep(.el-input__wrapper) {
  height: 48px !important;
  border-radius: 4px;
  border: 2px solid #ddd;
  box-shadow: none;
  transition: border-color 0.3s;
}

.verification-input-wrapper :deep(.el-input__wrapper:hover) {
  border-color: var(--mc-green);
}

.verification-input-wrapper :deep(.el-input__wrapper.is-focus) {
  border-color: var(--mc-blue);
  box-shadow: 0 0 0 2px rgba(79, 148, 205, 0.2);
}

.send-code-btn-wrapper {
  min-width: 120px;
}

.send-code-btn {
  width: 100%;
  height: 48px !important;
  font-family: var(--mc-font);
  font-weight: bold;
  background-color: var(--mc-green) !important;
  border-color: var(--mc-green) !important;
  color: white !important;
  padding: 0 15px !important;
  border-radius: 4px;
}

.send-code-btn:hover:not(:disabled) {
  background-color: #45a049 !important;
  border-color: #45a049 !important;
}

.send-code-btn:disabled {
  color: #999 !important;
  background-color: #f5f5f5 !important;
  border-color: #ddd !important;
}

.auth-footer {
  text-align: center;
}

.toggle-button {
  font-family: var(--mc-font);
  font-weight: bold;
  color: var(--mc-blue) !important;
  font-size: 16px;
}

.toggle-button:hover {
  color: var(--mc-green) !important;
}

:deep(.el-divider__text) {
  font-family: var(--mc-font);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .auth-card {
    padding: 30px 20px;
  }
  
  .auth-title {
    font-size: 1.5rem;
  }
  
  .verification-code-container {
    flex-direction: column;
    gap: 8px;
    align-items: stretch;
  }
  
  .send-code-btn-wrapper {
    min-width: auto;
  }
}
</style> 