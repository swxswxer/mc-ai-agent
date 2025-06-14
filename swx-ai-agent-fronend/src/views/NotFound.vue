<template>
  <div class="not-found-page">
    <div class="not-found-container">
      <div class="not-found-content">
        <div class="error-blocks">
          <div class="block grass-block">4</div>
          <div class="block stone-block">0</div>
          <div class="block diamond-block">4</div>
        </div>
        
        <h1 class="error-title">页面未找到</h1>
        <p class="error-message">
          很抱歉，您访问的页面似乎在虚空中迷失了。<br>
          就像在Minecraft中掉入虚空一样，这个页面不存在。
        </p>
        
        <div class="error-actions">
          <el-button type="primary" @click="$router.push('/')" class="mc-button primary" size="large">
            <House class="button-icon" />
            返回主页
          </el-button>
          <el-button @click="$router.go(-1)" class="mc-button" size="large">
            <Back class="button-icon" />
            返回上页
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 没有需要的逻辑
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #87ceeb 0%, #98fb98 50%, #f0e68c 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.not-found-container {
  text-align: center;
  max-width: 600px;
}

.not-found-content {
  background: rgba(255, 255, 255, 0.9);
  padding: 60px 40px;
  border-radius: 12px;
  border: 3px solid #2f2f2f;
  box-shadow: 8px 8px 0px rgba(0,0,0,0.3);
}

.error-blocks {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
}

.block {
  width: 80px;
  height: 80px;
  border: 3px solid #2f2f2f;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  font-weight: bold;
  color: white;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.8);
  font-family: var(--mc-font);
  animation: float 2s ease-in-out infinite;
}

.block:nth-child(2) {
  animation-delay: 0.3s;
}

.block:nth-child(3) {
  animation-delay: 0.6s;
}

.stone-block {
  background: linear-gradient(to bottom, #999999 0%, #666666 100%);
}

.diamond-block {
  background: linear-gradient(to bottom, #87ceeb 0%, #4682b4 100%);
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.error-title {
  font-size: 3rem;
  font-weight: bold;
  color: var(--mc-dark-gray);
  font-family: var(--mc-font);
  margin-bottom: 20px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.error-message {
  font-size: 1.2rem;
  color: var(--mc-gray);
  line-height: 1.6;
  margin-bottom: 40px;
}

.error-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.button-icon {
  margin-right: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-content {
    padding: 40px 20px;
  }
  
  .error-blocks {
    gap: 10px;
  }
  
  .block {
    width: 60px;
    height: 60px;
    font-size: 2rem;
  }
  
  .error-title {
    font-size: 2rem;
  }
  
  .error-message {
    font-size: 1rem;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
}
</style> 