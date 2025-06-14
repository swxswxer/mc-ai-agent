# Minecraft AI Assistant Frontend

这是一个专为Minecraft玩家打造的AI助手平台前端项目，使用Vue3 + Element Plus构建，具有Minecraft主题风格。

## 项目特性

- 🎮 **Minecraft主题设计** - 采用像素风格和方块元素设计
- 🤖 **双模式AI对话** - 支持基础Chat和Manus智能体两种对话模式
- 🔑 **API Key管理** - 用户可生成和管理专属API密钥
- 👤 **用户系统** - 完整的登录注册和个人中心功能
- 📱 **响应式设计** - 支持多种设备尺寸
- 🌊 **SSE流式对话** - 实时流式接收AI回复内容

## 技术栈

- **Vue 3** - 渐进式JavaScript框架
- **Vue Router** - 官方路由管理
- **Pinia** - 状态管理
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **Vite** - 构建工具

## 项目结构

```
src/
├── api/                 # API接口层
│   ├── index.js        # axios配置
│   ├── user.js         # 用户相关接口
│   └── ai.js           # AI对话接口
├── components/         # 公共组件
│   └── Layout.vue      # 主布局组件
├── router/             # 路由配置
│   └── index.js        # 路由定义
├── stores/             # 状态管理
│   └── user.js         # 用户状态
├── styles/             # 样式文件
│   └── global.css      # 全局样式
├── views/              # 页面组件
│   ├── Home.vue        # 主页
│   ├── Auth.vue        # 登录注册
│   ├── Profile.vue     # 个人中心
│   ├── Experience.vue  # AI体验
│   └── NotFound.vue    # 404页面
├── App.vue             # 根组件
└── main.js             # 入口文件
```

## 功能模块

### 1. 用户系统
- 用户注册/登录
- 会话管理
- 个人信息展示
- API Key生成和管理

### 2. AI对话体验
- **基础Chat**: 标准的Minecraft知识问答
- **Manus智能体**: 具备工具调用能力的高级AI助手
- 实时流式对话
- 消息历史管理

### 3. 平台介绍
- 功能特性展示
- 使用流程说明
- 响应式设计

## 安装和运行

### 前置要求
- Node.js >= 16
- npm 或 yarn

### 安装依赖
```bash
npm install
```

### 开发模式
```bash
npm run dev
```
项目将在 http://localhost:3000 启动

### 生产构建
```bash
npm run build
```

### 预览构建结果
```bash
npm run preview
```

## 后端接口

项目需要配合后端服务使用，后端接口地址配置在 `vite.config.js` 中：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8123',
    changeOrigin: true
  }
}
```

### 主要接口：

#### 用户相关
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `POST /api/user/logout` - 用户退出
- `GET /api/user/get/login` - 获取当前用户
- `GET /api/user/generate/apikey` - 生成API Key

#### AI对话
- `GET /api/ai/chat` - Chat模式对话 (SSE)
- `GET /api/ai/agent` - Agent模式对话 (SSE)

## 环境配置

### 代理配置
开发环境下会自动代理 `/api` 请求到后端服务器 `http://localhost:8123`

### 浏览器支持
- Chrome >= 87
- Firefox >= 78
- Safari >= 13
- Edge >= 88

## 主要特性说明

### Minecraft主题样式
- 使用CSS变量定义Minecraft风格颜色
- 方块风格的UI元素
- 像素字体和8位风格动画
- 草方块、石头、钻石等材质效果

### SSE流式对话
支持Server-Sent Events实现实时流式对话：
```javascript
const eventSource = new EventSource('/api/ai/chat?message=hello')
eventSource.onmessage = (event) => {
  // 处理流式数据
}
```

### 状态持久化
使用 Pinia 和 localStorage 实现用户状态持久化，页面刷新后保持登录状态。

## 开发指南

### 代码风格
- 使用 Vue 3 Composition API
- 遵循 ESLint 规则
- 组件名使用 PascalCase
- 文件名使用 kebab-case

### 组件开发
- 优先使用 `<script setup>` 语法
- 使用 Element Plus 组件和图标
- 添加适当的响应式设计
- 保持Minecraft主题风格一致性

## 故障排除

### 常见问题

1. **安装依赖失败**
   ```bash
   # 清除缓存重新安装
   rm -rf node_modules package-lock.json
   npm install
   ```

2. **代理请求失败**
   - 确保后端服务在 `localhost:8123` 运行
   - 检查 `vite.config.js` 中的代理配置

3. **构建失败**
   - 检查Node.js版本是否 >= 16
   - 确保所有依赖正确安装

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 项目Issues
- 邮箱: [您的邮箱]

---

**享受您的Minecraft AI助手之旅！** 🎮✨ 