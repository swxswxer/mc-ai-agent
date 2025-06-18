<template>
  <div class="layout">
    <!-- 导航栏 -->
    <header class="header">
      <div class="nav-container">
        <!-- Logo区域 -->
        <div class="logo-section" @click="$router.push('/')">
          <div class="grass-block logo-block"></div>
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
      <div class="footer-content">
        <p>&copy; 2025 MCompanion </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { House, ChatLineRound, PriceTag, User, ArrowDown, Setting, SwitchButton } from '@element-plus/icons-vue'

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

.logo-block {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
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
  background: var(--mc-dark-gray);
  color: white;
  text-align: center;
  padding: 20px;
  border-top: 3px solid #2f2f2f;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  font-family: var(--mc-font);
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