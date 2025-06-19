<template>
  <div class="home">
    <div class="homeBackground">
      <!-- 英雄区域 -->
      <section class="hero-section">
        <div class="hero-content">
          <div class="hero-text">
            <h1 class="hero-title">
<!--              <span class="minecraft-text">MCompanion</span>-->
              <span class="ai-text">MCompanion</span>
            </h1>
            <p class="hero-subtitle">
              专为Minecraft玩家打造的智能AI助手Mod
            </p>
            <p class="hero-description">
              通过先进的AI技术，为您提供游戏攻略、红石教程等全方位服务
            </p>
            <div class="hero-buttons">
              <el-button type="primary" size="large" @click="$router.push('/experience')" class="mc-button-green">
                <ChatLineRound class="button-icon" />
                立即体验
              </el-button>
              <el-button size="large" @click="scrollToFeatures" class="mc-button-green">
                <InfoFilled class="button-icon" />
                了解更多
              </el-button>
              <el-button size="large" @click="openExternalLink('https://github.com/swxswxer/MCompanionMod')" class="mc-button-green">
                <Download class="button-icon" />
                立马下载
              </el-button>
            </div>
          </div>
          <div class="hero-image">
            <div class="minecraft-blocks">
              <div class="block grass-block"></div>
              <div class="block stone-block"></div>
              <div class="block wood-block"></div>
              <div class="block diamond-block"></div>
            </div>
          </div>
        </div>
      </section>

      <!-- 特性介绍 -->
      <section class="features-section" ref="featuresSection">
        <div class="container">
          <h2 class="section-title">平台特性</h2>
          <div class="features-grid">
            <div class="feature-card mc-card">
              <div class="feature-icon">
                <ChatDotRound />
              </div>
              <h3>智能问答</h3>
              <p>支持普通Chat对话和超级智能体Manus，为您提供专业的Minecraft知识问答服务</p>
            </div>
            <div class="feature-card mc-card">
              <div class="feature-icon">
                <Key />
              </div>
              <h3>API集成</h3>
              <p>生成专属API Key，轻松集成到您的Minecraft Mod中，随时随地获得AI助手服务</p>
            </div>
            <div class="feature-card mc-card">
              <div class="feature-icon">
                <Monitor />
              </div>
              <h3>在线体验</h3>
              <p>无需安装任何软件，直接在网页上体验AI助手的强大功能</p>
            </div>
            <div class="feature-card mc-card">
              <div class="feature-icon">
                <UserFilled />
              </div>
              <h3>个人中心</h3>
              <p>管理您的账户信息、API Key，查看使用记录和个性化设置</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 使用流程 -->
      <section class="process-section">
        <div class="container">
          <h2 class="section-title">使用流程</h2>
          <div class="process-timeline">
            <div class="process-step">
              <div class="step-number">1</div>
              <div class="step-content">
                <h3>注册账户</h3>
                <p>创建您的专属账户，享受个性化服务</p>
              </div>
            </div>
            <div class="process-arrow">
              <ArrowRight />
            </div>
            <div class="process-step">
              <div class="step-number">2</div>
              <div class="step-content">
                <h3>生成API Key</h3>
                <p>在个人中心生成专属的API密钥</p>
              </div>
            </div>
            <div class="process-arrow">
              <ArrowRight />
            </div>
            <div class="process-step">
              <div class="step-number">3</div>
              <div class="step-content">
                <h3>配置Mod</h3>
                <p>将API Key配置到您的Minecraft Mod中</p>
              </div>
            </div>
            <div class="process-arrow">
              <ArrowRight />
            </div>
            <div class="process-step">
              <div class="step-number">4</div>
              <div class="step-content">
                <h3>开始使用</h3>
                <p>进入游戏按下绑定按键“-”享受你的新时代MC</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 立即开始 -->
      <section class="cta-section">
        <div class="container">
          <div class="cta-content mc-card">
            <h2>准备好开始您的AI助手之旅了吗？</h2>
            <p>立即注册账户，体验前所未有的Minecraft智能助手服务</p>
            <div class="cta-buttons">
              <el-button
                  v-if="!userStore.isLoggedIn"
                  type="primary"
                  size="large"
                  @click="$router.push('/auth')"
                  class="mc-button-green"
              >
                立即注册
              </el-button>
              <el-button
                  v-else
                  type="primary"
                  size="large"
                  @click="$router.push('/profile')"
                  class="mc-button-green"
              >
                前往个人中心
              </el-button>
              <el-button
                  size="large"
                  @click="$router.push('/experience')"
                  class="mc-button-green"
              >
                在线体验
              </el-button>
            </div>
          </div>
        </div>
      </section>
    </div>
    </div>

</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../stores/user'
import { Download } from '@element-plus/icons-vue'

const userStore = useUserStore()
const featuresSection = ref(null)

const scrollToFeatures = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const openExternalLink = (url) => {
  window.open(url, '_blank')
}
</script>

<style scoped>
.home {
  width: 100%;
}

.homeBackground {

  background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%);

}
.hero-section {
  /* background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%); */
  padding: 80px 20px;
  min-height: 600px;
  display: flex;
  align-items: center;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-text {
  color: var(--mc-dark-gray);
}

.hero-title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  font-family: var(--mc-font);
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.minecraft-text {
  color: var(--mc-green);
}

.ai-text {
  color: var(--mc-blue);
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: var(--mc-brown);
  font-weight: bold;
}

.hero-description {
  font-size: 1.1rem;
  margin-bottom: 30px;
  line-height: 1.6;
  color: var(--mc-gray);
}

.hero-buttons {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.button-icon {
  margin-right: 8px;
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.minecraft-blocks {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  animation: float 3s ease-in-out infinite;
}

.block {
  width: 120px;
  height: 120px;
  border: 3px solid #2f2f2f;
  box-shadow: 4px 4px 8px rgba(0,0,0,0.3);
  position: relative;
}

.stone-block {
  background: linear-gradient(to bottom, #999999 0%, #666666 100%);
}

.wood-block {
  background: linear-gradient(to bottom, #deb887 0%, #8b7355 100%);
}

.diamond-block {
  background: linear-gradient(to bottom, #87ceeb 0%, #4682b4 100%);
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 60px;
  font-family: var(--mc-font);
  color: var(--mc-dark-gray);
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.features-section {
  padding: 80px 20px;
  /* background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%); */
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 25px;
  max-width: 900px;
  margin: 0 auto;
}

.feature-card {
  padding: 20px 15px;
  text-align: center;
  background: white;
  transition: transform 0.3s, box-shadow 0.3s;
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  max-width: 200px;
  margin: 0 auto;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 6px 6px 0px rgba(0,0,0,0.4);
}

.feature-icon {
  font-size: 3rem;
  color: var(--mc-green);
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.4rem;
  font-weight: bold;
  margin-bottom: 15px;
  font-family: var(--mc-font);
  color: var(--mc-dark-gray);
}

.feature-card p {
  color: var(--mc-gray);
  line-height: 1.6;
}

.process-section {
  padding: 80px 20px;
  /* background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%); */
}

.process-timeline {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}

.process-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  max-width: 200px;
}

.step-number {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: var(--mc-green);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  border: 3px solid var(--mc-dark-gray);
}

.step-content h3 {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
  font-family: var(--mc-font);
  color: var(--mc-dark-gray);
}

.step-content p {
  color: var(--mc-gray);
  line-height: 1.5;
}

.process-arrow {
  font-size: 2rem;
  color: var(--mc-green);
}

.cta-section {
  padding: 80px 20px;
  /* background: linear-gradient(135deg, #98fb98 0%, #87ceeb 100%); */
}

.cta-content {
  text-align: center;
  padding: 60px 40px;
  background: rgba(255,255,255,0.9);
}

.cta-content h2 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
  font-family: var(--mc-font);
  color: var(--mc-dark-gray);
}

.cta-content p {
  font-size: 1.1rem;
  color: var(--mc-gray);
  margin-bottom: 30px;
  line-height: 1.6;
}

.cta-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
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

/* 中等屏幕 */
@media (max-width: 1200px) {
  .features-grid {
    gap: 20px;
  }
  
  .feature-card {
    padding: 25px;
  }
  
  .feature-card h3 {
    font-size: 1.2rem;
  }
  
  .feature-card p {
    font-size: 0.9rem;
  }
}

/* 平板屏幕 */
@media (max-width: 992px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 25px;
  }
}

/* 手机屏幕 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 40px;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .minecraft-blocks {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .block {
    width: 80px;
    height: 80px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .feature-card {
    padding: 20px;
  }
  
  .process-timeline {
    flex-direction: column;
  }
  
  .process-arrow {
    transform: rotate(90deg);
  }
  
  .cta-content {
    padding: 40px 20px;
  }
  
  .cta-buttons {
    flex-direction: column;
    align-items: center;
  }
}
</style> 