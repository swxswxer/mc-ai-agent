<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card mc-card">
        <div class="auth-header">
          <div class="logo-section">
            <div class="grass-block logo-block">
              <span class="logo-text">MC AI</span>
            </div>
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

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const loading = ref(false)
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
  userPassword: '',
  checkPassword: ''
})

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
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
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

// 切换登录/注册模式
const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 清空表单
  if (isLogin.value) {
    Object.assign(loginForm, { userAccount: '', userPassword: '' })
    loginFormRef.value?.clearValidate()
  } else {
    Object.assign(registerForm, { userAccount: '', userPassword: '', checkPassword: '' })
    registerFormRef.value?.clearValidate()
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
      Object.assign(registerForm, { userAccount: '', userPassword: '', checkPassword: '' })
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

.logo-block {
  width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
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

.auth-form :deep(.el-input__wrapper) {
  border-radius: 4px;
  border: 2px solid #ddd;
  box-shadow: none;
  transition: border-color 0.3s;
}

.auth-form :deep(.el-input__wrapper:hover) {
  border-color: var(--mc-green);
}

.auth-form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--mc-blue);
  box-shadow: 0 0 0 2px rgba(79, 148, 205, 0.2);
}

.auth-form :deep(.el-input__inner) {
  font-family: var(--mc-font);
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
}
</style> 