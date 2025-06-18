<template>
  <div class="pricing-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>选择您的方案</h1>
      <p>选择最适合您的AI助手服务方案</p>
    </div>

    <!-- 定价卡片 -->
    <div class="pricing-cards">
      <!-- FREE 方案 -->
      <div class="pricing-card" :class="{ featured: false }">
        <div class="card-header">
          <h3 class="plan-name">FREE</h3>
          <div class="plan-price">
            <span class="currency">¥</span>
            <span class="amount">0</span>
            <span class="period">/月</span>
          </div>
        </div>
        <div class="card-body">
          <ul class="features-list">
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>基础CHAT 50次/月</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>基础Minecraft知识问答</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>社区支持</span>
            </li>
          </ul>
        </div>
        <div class="card-footer">
          <el-button 
            type="primary" 
            class="mc-button primary subscribe-btn" 
            size="large"
            @click="goToExperience"
          >
            {{ userStore.isLoggedIn ? '立刻体验' : '登录后体验' }}
          </el-button>
        </div>
      </div>

      <!-- PRO 方案 -->
      <div class="pricing-card" :class="{ featured: true }">
        <div class="featured-badge">推荐</div>
        <div class="card-header">
          <h3 class="plan-name">PRO</h3>
          <div class="plan-price">
            <span class="currency">¥</span>
            <span class="amount">9.9</span>
            <span class="period">/月</span>
          </div>
        </div>
        <div class="card-body">
          <ul class="features-list">
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>基础CHAT 200次/月</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>高级Minecraft知识问答</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>优先响应速度</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>邮件支持</span>
            </li>
          </ul>
        </div>
        <div class="card-footer">
          <el-button 
            type="primary" 
            class="mc-button primary subscribe-btn" 
            size="large"
            @click="subscribe(2)"
            :loading="subscribing"
          >
            {{ userStore.isLoggedIn ? '立即订阅' : '登录后订阅' }}
          </el-button>
        </div>
      </div>

      <!-- ULTRA 方案 -->
      <div class="pricing-card" :class="{ featured: false }">
        <div class="card-header">
          <h3 class="plan-name">ULTRA</h3>
          <div class="plan-price">
            <span class="currency">¥</span>
            <span class="amount">19.9</span>
            <span class="period">/月</span>
          </div>
        </div>
        <div class="card-body">
          <ul class="features-list">
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>基础CHAT 300次/月</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>MANUS智能体 100次/月</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>专家级AI建议</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>最高优先级</span>
            </li>
            <li class="feature-item">
              <el-icon class="feature-icon"><Check /></el-icon>
              <span>1对1专属支持</span>
            </li>
          </ul>
        </div>
        <div class="card-footer">
          <el-button 
            type="primary"
            class="mc-button primary subscribe-btn"
            size="large"
            disabled
          >
            暂未开放
          </el-button>
        </div>
      </div>
    </div>

    <!-- 支付弹窗 -->
    <el-dialog
      v-model="paymentDialogVisible"
      title="订阅确认"
      width="400px"
      :before-close="handlePaymentClose"
      class="mc-dialog"
    >
      <div class="payment-content">
        <div class="payment-info">
          <h4>订阅方案：{{ selectedPlan.name }}</h4>
          <p class="payment-price">
            <span class="currency">¥</span>
            <span class="amount">{{ selectedPlan.price }}</span>
            <span class="period">/月</span>
          </p>
          <div class="payment-features">
            <h5>包含服务：</h5>
            <ul>
              <li v-for="feature in selectedPlan.features" :key="feature">{{ feature }}</li>
            </ul>
          </div>
        </div>
        
        <div class="payment-method">
          <h5>支付方式：</h5>
          <el-radio-group v-model="paymentMethod" class="payment-options">
            <el-radio label="wechat" class="payment-radio">
              <div class="payment-option">
                <span class="payment-icon">💚</span>
                <span>微信支付</span>
              </div>
            </el-radio>
            <el-radio label="alipay" class="payment-radio">
              <div class="payment-option">
                <span class="payment-icon">💙</span>
                <span>支付宝</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="paymentDialogVisible = false" class="mc-button">取消</el-button>
          <el-button 
            type="primary" 
            @click="confirmPayment" 
            :loading="paying"
            class="mc-button primary"
          >
            确认支付
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import { userApi } from '../api/user'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const subscribing = ref(false)
const paymentDialogVisible = ref(false)
const paying = ref(false)
const paymentMethod = ref('wechat')

// 选中的方案
const selectedPlan = reactive({
  vipLevel: 0,
  name: '',
  price: 0,
  features: []
})

// 方案配置
const plans = {
  2: {
    name: 'PRO',
    price: 9.9,
    features: ['基础CHAT 200次/月', '高级Minecraft知识问答', '优先响应速度', '邮件支持']
  },
  3: {
    name: 'ULTRA',
    price: 19.9,
    features: ['基础CHAT 300次/月', 'MANUS智能体 100次/月', '专家级AI建议', '最高优先级', '1对1专属支持']
  }
}

// 跳转到体验页面
const goToExperience = () => {
  // 检查登录状态
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/auth')
    return
  }
  
  router.push('/experience')
}

// 订阅处理
const subscribe = (vipLevel) => {
  // 检查登录状态
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/auth')
    return
  }

  const plan = plans[vipLevel]
  if (!plan) return
  
  // 设置选中的方案
  selectedPlan.vipLevel = vipLevel
  selectedPlan.name = plan.name
  selectedPlan.price = plan.price
  selectedPlan.features = plan.features
  
  // 显示支付弹窗
  paymentDialogVisible.value = true
}

// 确认支付
const confirmPayment = async () => {
  paying.value = true
  
  try {
    // 模拟支付延迟
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 调用后端订阅接口
    const response = await userApi.subscribe(selectedPlan.vipLevel)
    
    ElMessage.success('订阅成功！欢迎使用 ' + selectedPlan.name + ' 方案')
    paymentDialogVisible.value = false
    
    // 跳转到个人中心查看订阅状态
    setTimeout(() => {
      router.push('/profile')
    }, 1500)
  } catch (error) {
    console.error('订阅失败:', error)
    // API拦截器已经处理了错误提示，这里不需要重复提示
  } finally {
    paying.value = false
  }
}

// 关闭支付弹窗
const handlePaymentClose = (done) => {
  if (paying.value) {
    ElMessage.warning('支付正在进行中，请稍候...')
    return
  }
  done()
}
</script>

<style scoped>
.pricing-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 60px;
}

.page-header h1 {
  color: var(--mc-brown);
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
}

.page-header p {
  color: #666;
  font-size: 18px;
  margin: 0;
}

.pricing-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1000px;
  margin: 0 auto;
  align-items: stretch;
}

.pricing-card {
  background: white;
  border: 3px solid #ddd;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s ease;
  position: relative;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.pricing-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.pricing-card.featured {
  border-color: var(--mc-yellow);
  background: linear-gradient(to bottom, #fffdf7 0%, #ffffff 100%);
  box-shadow: 0 8px 25px rgba(255, 215, 0, 0.3);
}

.featured-badge {
  position: absolute;
  top: -15px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--mc-yellow);
  color: var(--mc-brown);
  padding: 8px 20px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 14px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.card-header {
  margin-bottom: 30px;
}

.plan-name {
  color: var(--mc-brown);
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.plan-price {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
}

.currency {
  font-size: 20px;
  color: #666;
}

.amount {
  font-size: 48px;
  font-weight: bold;
  color: var(--mc-brown);
}

.period {
  font-size: 16px;
  color: #666;
}

.card-body {
  margin-bottom: 40px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-item {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-bottom: 16px;
  padding: 8px 0;
}

.feature-icon {
  color: var(--mc-green);
  font-size: 18px;
  margin-right: 12px;
  flex-shrink: 0;
}

.feature-item span {
  font-size: 16px;
  color: #333;
  text-align: left;
}

.card-footer {
  margin-top: auto;
}

.subscribe-btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
}

/* 支付弹窗样式 */
.payment-content {
  padding: 20px 0;
}

.payment-info {
  margin-bottom: 30px;
  text-align: center;
}

.payment-info h4 {
  color: var(--mc-brown);
  font-size: 24px;
  margin-bottom: 16px;
}

.payment-price {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
  margin-bottom: 20px;
}

.payment-price .currency {
  font-size: 16px;
  color: #666;
}

.payment-price .amount {
  font-size: 36px;
  font-weight: bold;
  color: var(--mc-brown);
}

.payment-price .period {
  font-size: 14px;
  color: #666;
}

.payment-features {
  text-align: left;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  border: 2px solid #eee;
}

.payment-features h5 {
  margin: 0 0 12px 0;
  color: var(--mc-brown);
}

.payment-features ul {
  margin: 0;
  padding-left: 20px;
}

.payment-features li {
  margin-bottom: 8px;
  color: #333;
}

.payment-method {
  margin-top: 30px;
}

.payment-method h5 {
  margin-bottom: 16px;
  color: var(--mc-brown);
}

.payment-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.payment-radio {
  border: 2px solid #eee;
  border-radius: 8px;
  padding: 16px;
  margin: 0;
  transition: all 0.2s;
}

.payment-radio:hover {
  border-color: var(--mc-yellow);
  background: #fffdf7;
}

.payment-radio.is-checked {
  border-color: var(--mc-yellow);
  background: #fffdf7;
}

.payment-option {
  display: flex;
  align-items: center;
  gap: 12px;
}

.payment-icon {
  font-size: 24px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .pricing-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .page-header h1 {
    font-size: 36px;
  }
  
  .page-header p {
    font-size: 16px;
  }
  
  .pricing-card {
    padding: 20px;
  }
}
</style> 