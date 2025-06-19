<template>
  <div class="layout">
    <!-- 导航栏 -->
    <header class="header">
      <div class="nav-container">
        <!-- Logo区域 -->
        <div class="logo-section" @click="$router.push('/')">
          <svg width="40" height="40" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" class="logo-icon">
            <defs>
              <pattern id="simpleGrass" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                <rect width="4" height="4" fill="#5CB85C"/>
                <rect x="0" y="0" width="1" height="1" fill="#4A994A"/>
                <rect x="2" y="1" width="1" height="1" fill="#6BC46B"/>
                <rect x="1" y="2" width="1" height="1" fill="#4A994A"/>
                <rect x="3" y="3" width="1" height="1" fill="#4A994A"/>
              </pattern>
              <pattern id="simpleDirt" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                <rect width="4" height="4" fill="#8B4513"/>
                <rect x="1" y="0" width="1" height="1" fill="#A0522D"/>
                <rect x="0" y="2" width="1" height="1" fill="#654321"/>
                <rect x="3" y="1" width="1" height="1" fill="#A0522D"/>
                <rect x="2" y="3" width="1" height="1" fill="#654321"/>
              </pattern>
            </defs>
            <g transform="translate(4, 4)">
              <polygon points="0,8 12,2 24,8 12,14" fill="url(#simpleGrass)" stroke="#2F2F2F" stroke-width="1"/>
              <polygon points="0,8 12,14 12,26 0,20" fill="url(#simpleDirt)" stroke="#2F2F2F" stroke-width="1"/>
              <polygon points="12,14 24,8 24,20 12,26" fill="url(#simpleDirt)" stroke="#2F2F2F" stroke-width="1" opacity="0.7"/>
            </g>
          </svg>
          <span class="logo-text">MCompanion</span>
        </div>

        <!-- 导航菜单 -->
        <nav class="nav-menu">
          <div class="nav-items">
            <div 
              class="nav-item" 
              :class="{ active: $route.path === '/' }"
              @click="$router.push('/')"
            >
              <el-icon class="menu-icon"><House /></el-icon>
              <span>主页</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: $route.path === '/experience' }"
              @click="$router.push('/experience')"
            >
              <el-icon class="menu-icon"><ChatLineRound /></el-icon>
              <span>体验</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: $route.path === '/pricing' }"
              @click="$router.push('/pricing')"
            >
              <el-icon class="menu-icon"><PriceTag /></el-icon>
              <span>定价</span>
            </div>
            <div 
              class="nav-item" 
              @click="openExternalLink('https://github.com/swxswxer/MCompanionMod')"
            >
              <el-icon class="menu-icon"><Download /></el-icon>
              <span>下载</span>
            </div>
            <div 
              class="nav-item" 
              @click="openExternalLink('https://github.com/swxswxer/MCompanionMod/issues')"
            >
              <el-icon class="menu-icon"><Warning /></el-icon>
              <span>问题反馈</span>
            </div>
          </div>
        </nav>

        <!-- 用户区域 -->
        <div class="user-section">
          <div v-if="!userStore.isLoggedIn" class="login-buttons">
            <el-button type="primary" @click="$router.push('/auth')" class="mc-button primary">
              登录/注册
            </el-button>
          </div>
          <div v-else class="user-info">
            <el-dropdown trigger="click" @command="handleUserAction">
              <div class="user-avatar">
                <el-avatar :size="32" :src="userStore.userInfo?.userAvatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="username">{{ userStore.userInfo?.userName || userStore.userInfo?.userAccount }}</span>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="mc-dropdown">
<!--                  <el-dropdown-item disabled class="user-detail">-->
<!--                    <div>{{ userStore.userInfo?.userName || userStore.userInfo?.userAccount }}</div>-->
<!--                    <div style="color: #999; font-size: 12px;">{{ userStore.userInfo?.email || '未设置邮箱' }}</div>-->
<!--                  </el-dropdown-item>-->
                  <el-dropdown-item divided command="profile">
                    <el-icon class="dropdown-menu-icon"><Setting /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="logout">
                    <el-icon class="dropdown-menu-icon"><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-container">
        <!-- 左侧网站信息 -->
        <div class="footer-section">
          <div class="footer-logo">
            <svg width="40" height="40" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" class="logo-icon">
              <defs>
                <pattern id="footerGrass" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                  <rect width="4" height="4" fill="#5CB85C"/>
                  <rect x="0" y="0" width="1" height="1" fill="#4A994A"/>
                  <rect x="2" y="1" width="1" height="1" fill="#6BC46B"/>
                  <rect x="1" y="2" width="1" height="1" fill="#4A994A"/>
                  <rect x="3" y="3" width="1" height="1" fill="#4A994A"/>
                </pattern>
                <pattern id="footerDirt" x="0" y="0" width="4" height="4" patternUnits="userSpaceOnUse">
                  <rect width="4" height="4" fill="#8B4513"/>
                  <rect x="1" y="0" width="1" height="1" fill="#A0522D"/>
                  <rect x="0" y="2" width="1" height="1" fill="#654321"/>
                  <rect x="3" y="1" width="1" height="1" fill="#A0522D"/>
                  <rect x="2" y="3" width="1" height="1" fill="#654321"/>
                </pattern>
              </defs>
              <g transform="translate(4, 4)">
                <polygon points="0,8 12,2 24,8 12,14" fill="url(#footerGrass)" stroke="#2F2F2F" stroke-width="1"/>
                <polygon points="0,8 12,14 12,26 0,20" fill="url(#footerDirt)" stroke="#2F2F2F" stroke-width="1"/>
                <polygon points="12,14 24,8 24,20 12,26" fill="url(#footerDirt)" stroke="#2F2F2F" stroke-width="1" opacity="0.7"/>
              </g>
            </svg>
            <span class="footer-logo-text">MCompanion</span>
          </div>
          <p class="footer-description">
            专为Minecraft玩家打造的智能AI助手Mod，提升你的游戏体验
          </p>
          <div class="footer-contact">
            <p>联系我们：QQ 3235765007</p>
            <p>
              <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank" class="beian-link">
                粤ICP备2025431446号-1
              </a>
            </p>
          </div>
          <p class="copyright">&copy; 2025 MCompanion</p>
        </div>

        <!-- 资源栏目 -->
        <div class="footer-section">
          <h3 class="footer-title">资源</h3>
          <ul class="footer-links">
            <li>
              <a href="https://github.com/swxswxer/MCompanionMod" target="_blank" class="footer-link">
                Releases
              </a>
            </li>
            <li>
              <a href="https://github.com/swxswxer/MCompanionMod/issues" target="_blank" class="footer-link">
                问题反馈
              </a>
            </li>
            <li>
              <a href="#" @click.prevent="$router.push('/experience')" class="footer-link">
                在线体验
              </a>
            </li>
            <li>
              <a href="#" @click.prevent="scrollToFeatures" class="footer-link">
                如何使用
              </a>
            </li>
            <li>
              <a href="#" @click.prevent="$router.push('/404')" class="footer-link">
                用户协议
              </a>
            </li>
            <li>
              <a href="#" @click.prevent="$router.push('/404')" class="footer-link">
                隐私协议
              </a>
            </li>
          </ul>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { House, ChatLineRound, PriceTag, User, ArrowDown, Setting, SwitchButton, Download, Warning } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()

const handleUserAction = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}

const openExternalLink = (url) => {
  window.open(url, '_blank')
}

const scrollToFeatures = () => {
  // 如果当前不在首页，先导航到首页
  if (router.currentRoute.value.path !== '/') {
    router.push('/').then(() => {
      // 等待页面加载后再滚动
      setTimeout(() => {
        const featuresSection = document.querySelector('.features-section')
        if (featuresSection) {
          featuresSection.scrollIntoView({ behavior: 'smooth' })
        }
      }, 100)
    })
  } else {
    const featuresSection = document.querySelector('.features-section')
    if (featuresSection) {
      featuresSection.scrollIntoView({ behavior: 'smooth' })
    }
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(to bottom, #8b8b8b 0%, #666666 100%);
  border-bottom: 3px solid #2f2f2f;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 70px;
}

.logo-section {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.logo-section:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 40px;
  height: 40px;
  display: block;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.3));
}

.logo-text {
  color: white;
  margin-left: 10px;
  font-weight: bold;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.8);
  font-size: 20px;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.nav-items {
  display: flex;
  align-items: center;
  gap: 20px;
  height: 100%;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  font-family: var(--mc-font);
  font-weight: bold;
  padding: 12px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  height: 45px;
  box-sizing: border-box;
}

.nav-item:hover {
  background: rgba(255,255,255,0.1);
  color: var(--mc-yellow);
}

.nav-item.active {
  background: rgba(255,255,255,0.2);
  color: var(--mc-yellow);
}

.menu-icon {
  font-size: 18px;
}

.user-section {
  min-width: 150px;
  display: flex;
  justify-content: flex-end;
}

.user-avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background 0.2s;
  gap: 8px;
  height: 45px;
}

.user-avatar:hover {
  background: rgba(255,255,255,0.1);
}

.username {
  font-family: var(--mc-font);
  font-weight: bold;
  font-size: 14px;
}

.dropdown-icon {
  font-size: 14px;
}

.dropdown-menu-icon {
  font-size: 16px;
  margin-right: 8px;
}

.mc-dropdown {
  background: #f0f0f0;
  border: 2px solid #c0c0c0;
  border-radius: 0;
  box-shadow: 4px 4px 0px rgba(0,0,0,0.3);
}

.mc-dropdown :deep(.el-dropdown-menu__item) {
  font-family: var(--mc-font);
  padding: 8px 15px;
}

.user-detail {
  border-bottom: 1px solid #ddd;
  margin-bottom: 5px;
}

.main-content {
  flex: 1;
  min-height: calc(100vh - 120px);
}

.footer {
  background: #3d4147;
  color: #e0e0e0;
  padding: 40px 20px 20px;
  border-top: 3px solid #2f2f2f;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 200px;
  gap: 60px;
  font-family: var(--mc-font);
}

.footer-section {
  display: flex;
  flex-direction: column;
}

.footer-logo {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.footer-logo-text {
  color: white;
  margin-left: 10px;
  font-weight: bold;
  font-size: 18px;
}

.footer-description {
  color: #b0b0b0;
  line-height: 1.6;
  margin-bottom: 20px;
  font-size: 14px;
}

.footer-contact {
  margin-bottom: 20px;
}

.footer-contact p {
  margin: 8px 0;
  font-size: 14px;
  color: #b0b0b0;
}

.beian-link {
  color: #b0b0b0;
  text-decoration: none;
  transition: color 0.2s;
}

.beian-link:hover {
  color: var(--mc-yellow);
}

.copyright {
  color: #888;
  font-size: 14px;
  margin-top: auto;
}

.footer-title {
  color: white;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
  font-family: var(--mc-font);
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 12px;
}

.footer-link {
  color: #b0b0b0;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
  cursor: pointer;
}

.footer-link:hover {
  color: var(--mc-yellow);
}

@media (max-width: 768px) {
  .nav-container {
    padding: 0 10px;
  }
  
  .username {
    display: none;
  }
  
  .nav-menu {
    flex: none;
  }
  
  .footer-container {
    grid-template-columns: 1fr;
    gap: 30px;
    text-align: left;
  }
  
  .footer-logo {
    justify-content: flex-start;
  }
}

/* 只在非常小的屏幕上隐藏菜单文字 */
@media (max-width: 480px) {
  .mc-menu :deep(.el-menu-item span) {
    display: none;
  }
  
  .menu-icon {
    margin-right: 0;
  }
}
</style> 