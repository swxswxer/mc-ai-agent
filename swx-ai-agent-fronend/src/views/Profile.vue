<template>
  <div class="profile-page">
    <div class="container">
      <div class="profile-header">
        <h1 class="page-title">个人中心</h1>
        <p class="page-subtitle">管理您的账户信息和API配置</p>
      </div>

      <div class="profile-content">
        <!-- 用户信息卡片 -->
        <div class="profile-card mc-card">
          <div class="card-header">
            <h2 class="card-title">
              <el-icon class="title-icon"><User /></el-icon>
              用户信息
            </h2>
          </div>
          <div class="card-content">
            <div class="user-info">
              <div class="avatar-section">
                <el-avatar :size="80" :src="userStore.userInfo?.userAvatar">
                  <el-icon :size="32"><User /></el-icon>
                </el-avatar>
              </div>
              <div class="info-section">
                <div class="info-item">
                  <label>用户昵称:</label>
                  <span>{{ userStore.userInfo?.userName || '未设置' }}</span>
                </div>
                <div class="info-item">
                  <label>用户账号:</label>
                  <span>{{ userStore.userInfo?.userAccount }}</span>
                </div>
                <div class="info-item">
                  <label>邮箱地址:</label>
                  <span>{{ userStore.userInfo?.email || '未设置' }}</span>
                </div>
                <div class="info-item">
                  <label>会员等级:</label>
                  <el-tag :type="getRoleType(userStore.userInfo?.currentVipLevelId)">
                    {{ getRoleText(userStore.userInfo?.currentVipLevelId) }}
                  </el-tag>
                </div>
                <div class="info-item">
                  <label>注册时间:</label>
                  <span>{{ formatDate(userStore.userInfo?.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- API Key管理卡片 -->
        <div class="api-card mc-card">
          <div class="card-header">
            <h2 class="card-title">
              <el-icon class="title-icon"><Key /></el-icon>
              API Key 管理
            </h2>
          </div>
          <div class="card-content">
            <div class="api-description">
              <p>API Key 用于在 Minecraft Mod 中访问 AI 助手服务。请妥善保管您的 API Key，不要泄露给他人。</p>
            </div>
            
            <div class="api-key-section">
              <div class="api-key-display">
                <label>当前 API Key:</label>
                <div class="api-key-input">
                  <el-input
                    :value="displayApiKey"
                    readonly
                    size="large"
                    class="mc-input"
                    :type="showApiKey ? 'text' : 'password'"
                  >
                    <template #append>
                      <el-button 
                        @click="toggleApiKeyVisibility" 
                        :icon="showApiKey ? 'Hide' : 'View'"
                        class="visibility-btn"
                      />
                      <el-button 
                        @click="copyApiKey" 
                        icon="DocumentCopy"
                        class="copy-btn"
                        :disabled="!userStore.userInfo?.apiKey"
                      />
                    </template>
                  </el-input>
                </div>
              </div>

              <div class="api-actions">
                                  <el-button 
                  type="primary" 
                  @click="handleGenerateApiKey"
                  :loading="generating"
                  class="mc-button-green"
                  size="large"
                >
                  <el-icon class="button-icon"><Refresh /></el-icon>
                  {{ userStore.userInfo?.apiKey ? '重新生成' : '生成 API Key' }}
                </el-button>
                <el-button 
                  v-if="userStore.userInfo?.apiKey"
                  @click="showUsageDialog = true"
                  class="mc-button-green"
                  size="large"
                >
                  <el-icon class="button-icon"><InfoFilled /></el-icon>
                  使用说明
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 使用统计卡片 -->
        <div class="stats-card mc-card">
          <div class="card-header">
            <h2 class="card-title">
              <el-icon class="title-icon"><TrendCharts /></el-icon>
              本月统计
            </h2>
            <el-button 
              @click="refreshStatistics" 
              :loading="loadingStats"
              :icon="loadingStats ? '' : 'Refresh'"
              circle
              size="small"
              class="refresh-btn"
            />
          </div>
          <div class="card-content">
            <div class="stats-grid" v-loading="loadingStats">
              <div class="stat-item">
                <div class="stat-value">{{ userStore.userStatistics?.todayCall ?? '--' }}</div>
                <div class="stat-label">今日调用</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userStore.userStatistics?.totalNumber ?? '--' }}</div>
                <div class="stat-label">总调用次数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value" :class="getRemainingAmountClass">{{ userStore.userStatistics?.remainingAmount ?? '--' }}</div>
                <div class="stat-label">剩余额度</div>
              </div>
            </div>
            <el-alert
              v-if="!userStore.userStatistics && !loadingStats"
              title="暂无统计数据"
              type="info"
              :closable="false"
              style="margin-top: 20px;"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- API Key使用说明对话框 -->
    <el-dialog 
      v-model="showUsageDialog" 
      title="API Key 使用说明"
      width="600px"
      class="usage-dialog"
    >
      <div class="usage-content">
        <h3>如何在 Minecraft Mod 中使用 API Key</h3>
        <ol>
          <li>复制您的 API Key</li>
          <li>打开 Minecraft 并加载 AI 助手 Mod</li>
          <li>在 Mod 配置界面中找到 "API Key" 设置项</li>
          <li>粘贴您的 API Key 并保存</li>
          <li>重启游戏或重新加载 Mod 配置</li>
        </ol>
        
        <h3>注意事项</h3>
        <ul>
          <li>请勿将 API Key 分享给他人</li>
          <li>如果 API Key 泄露，请立即重新生成</li>
          <li>API Key 具有访问限制，请合理使用</li>
          <li>如遇问题，请联系技术支持</li>
        </ul>
      </div>
      <template #footer>
        <el-button @click="showUsageDialog = false" class="mc-button-green">
          知道了
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const generating = ref(false)
const showApiKey = ref(false)
const showUsageDialog = ref(false)
const loadingStats = ref(false)

// 组件挂载时获取用户完整信息
onMounted(async () => {
  await userStore.getUserInfo()
  await loadStatistics()
})

// 加载统计数据
const loadStatistics = async () => {
  loadingStats.value = true
  try {
    await userStore.getStatisticsByMon()
  } finally {
    loadingStats.value = false
  }
}

// 刷新统计数据
const refreshStatistics = async () => {
  await loadStatistics()
  ElMessage.success('统计数据已刷新')
}

// 根据剩余额度获取颜色样式
const getRemainingAmountClass = computed(() => {
  const remaining = userStore.userStatistics?.remainingAmount
  if (remaining === null || remaining === undefined) return ''
  
  if (remaining <= 10) return 'stat-value-danger'
  if (remaining <= 50) return 'stat-value-warning'
  return 'stat-value-success'
})

// 显示的API Key（隐藏部分字符）
const displayApiKey = computed(() => {
  const apiKey = userStore.userInfo?.apiKey
  if (!apiKey) return '暂未生成'
  
  if (showApiKey.value) {
    return apiKey
  } else {
    // 只显示前4位和后4位，中间用*号代替
    if (apiKey.length <= 8) return '*'.repeat(apiKey.length)
    return apiKey.slice(0, 4) + '*'.repeat(apiKey.length - 8) + apiKey.slice(-4)
  }
})

// 切换API Key显示/隐藏
const toggleApiKeyVisibility = () => {
  showApiKey.value = !showApiKey.value
}

// 复制API Key
const copyApiKey = async () => {
  const apiKey = userStore.userInfo?.apiKey
  if (!apiKey) {
    ElMessage.warning('暂无 API Key 可复制')
    return
  }

  try {
    await navigator.clipboard.writeText(apiKey)
    ElMessage.success('API Key 已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败，请手动选择复制')
  }
}

// 生成API Key
const handleGenerateApiKey = async () => {
  generating.value = true
  try {
    const apiKey = await userStore.generateApiKey()
    if (apiKey) {
      // 生成成功后显示API Key
      showApiKey.value = true
      setTimeout(() => {
        showApiKey.value = false
      }, 5000) // 5秒后自动隐藏
    }
  } finally {
    generating.value = false
  }
}

// 获取角色类型（用于标签颜色）
const getRoleType = (role) => {
  switch (role) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return 'info'
  }
}

// 获取角色文本
const getRoleText = (role) => {
  switch (role) {
    case 1: return 'FREE'
    case 2: return 'PRO'
    case 3: return 'ULTRA'
    default: return '未知'
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '未知'
  try {
    return new Date(dateStr).toLocaleString('zh-CN')
  } catch {
    return '格式错误'
  }
}
</script>

<style scoped>
.profile-page {
  min-height: calc(100vh - 120px);
  padding: 40px 20px;
  background: #f8f9fa;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
}

.profile-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin-bottom: 10px;
}

.page-subtitle {
  font-size: 1.1rem;
  color: var(--mc-gray);
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.profile-card,
.api-card,
.stats-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
}

.card-header {
  margin-bottom: 25px;
  border-bottom: 2px solid #eee;
  padding-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0;
}

.title-icon {
  color: var(--mc-green);
  font-size: 20px;
}

.user-info {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 30px;
  align-items: start;
}

.avatar-section {
  display: flex;
  justify-content: center;
}

.avatar-section :deep(.el-avatar .el-icon) {
  font-size: 32px;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 15px;
  align-items: center;
}

.info-item label {
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
}

.info-item span {
  color: var(--mc-gray);
}

.api-description {
  background: #f0f8ff;
  padding: 15px;
  border-radius: 4px;
  border-left: 4px solid var(--mc-blue);
  margin-bottom: 25px;
}

.api-description p {
  margin: 0;
  color: var(--mc-gray);
  line-height: 1.6;
}

.api-key-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.api-key-display label {
  display: block;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin-bottom: 10px;
}

.api-key-input {
  max-width: 500px;
}

.api-key-input :deep(.el-input-group__append) {
  background: white;
  border-left: 1px solid #ddd;
}

.visibility-btn,
.copy-btn {
  border: none;
  background: transparent;
  color: var(--mc-blue);
}

.visibility-btn:hover,
.copy-btn:hover {
  color: var(--mc-green);
}

.api-actions {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.button-icon {
  margin-right: 5px;
  font-size: 16px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px solid #eee;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: var(--mc-green);
  font-family: var(--mc-font);
  margin-bottom: 5px;
}

.stat-label {
  color: var(--mc-gray);
  font-size: 0.9rem;
}

.stat-value-success {
  color: var(--mc-green) !important;
}

.stat-value-warning {
  color: #ff9800 !important;
}

.stat-value-danger {
  color: #f44336 !important;
}

.usage-content {
  line-height: 1.6;
  color: var(--mc-gray);
}

.usage-content h3 {
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin: 20px 0 10px 0;
}

.usage-content h3:first-child {
  margin-top: 0;
}

.usage-content ol,
.usage-content ul {
  padding-left: 20px;
}

.usage-content li {
  margin-bottom: 8px;
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
  .profile-page {
    padding: 20px 10px;
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .profile-card,
  .api-card,
  .stats-card {
    padding: 20px;
  }
  
  .user-info {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .info-item {
    grid-template-columns: 1fr;
    text-align: left;
    gap: 5px;
  }
  
  .api-actions {
    flex-direction: column;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
}

.refresh-btn {
  background: var(--mc-green) !important;
  border: 2px solid var(--mc-dark-gray) !important;
  color: white !important;
  font-size: 14px !important;
  width: 32px !important;
  height: 32px !important;
  box-shadow: 2px 2px 0px rgba(0,0,0,0.3) !important;
  transition: all 0.2s ease !important;
}

.refresh-btn:hover {
  background: #32cd32 !important;
  transform: translateY(-1px) !important;
  box-shadow: 3px 3px 0px rgba(0,0,0,0.4) !important;
}

.refresh-btn:active {
  transform: translateY(1px) !important;
  box-shadow: 1px 1px 0px rgba(0,0,0,0.3) !important;
}
</style> 