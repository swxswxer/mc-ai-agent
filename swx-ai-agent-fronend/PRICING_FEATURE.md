# 定价页面功能说明

## 功能概述

新增了完整的定价页面功能，包括三个订阅等级和支付流程。

## 功能特性

### 1. 三个订阅等级

#### FREE（免费版）
- **价格**: ¥0/月
- **功能**: 
  - 基础CHAT 50次/月
  - 基础Minecraft知识问答
  - 社区支持
- **按钮**: 立刻体验（跳转到体验页面）

#### PRO（专业版）- 推荐
- **价格**: ¥29/月  
- **功能**:
  - 基础CHAT 200次/月
  - 高级Minecraft知识问答
  - 优先响应速度
  - 邮件支持
- **按钮**: 立即订阅

#### ULTRA（旗舰版）
- **价格**: ¥59/月
- **功能**:
  - 基础CHAT 300次/月
  - MANUS智能体 100次/月
  - 专家级AI建议
  - 最高优先级
  - 1对1专属支持
- **按钮**: 立即订阅

### 2. 支付流程

1. **选择方案**: 点击PRO或ULTRA的"立即订阅"按钮
2. **确认订阅**: 弹出支付确认弹窗，显示方案详情
3. **选择支付方式**: 微信支付或支付宝（UI准备，实际支付逻辑待完善）
4. **确认支付**: 模拟支付成功后调用后端API
5. **订阅成功**: 显示成功信息并跳转到个人中心

### 3. 后端API集成

#### 订阅接口
- **接口**: `POST /user/subscribe`
- **参数**: `vipLevel` (1=FREE, 2=PRO, 3=ULTRA)
- **返回格式**:
```json
{
  "code": 0,
  "data": true,
  "message": "success"
}
```

### 4. 页面导航

- 在顶部导航栏新增"定价"菜单项
- 使用价格标签图标
- 支持路由跳转到 `/pricing`

### 5. 样式设计

- 保持与整站一致的Minecraft主题风格
- 响应式设计，支持移动端浏览
- 推荐方案突出显示（PRO方案）
- Minecraft风格的按钮和弹窗

### 6. 用户体验

- FREE方案直接跳转体验页面
- 付费方案展示支付流程
- 支付过程中的加载状态和防重复提交
- 支付成功后的用户反馈和页面跳转

## 技术实现

### 新增文件
- `src/views/Pricing.vue` - 定价页面组件
- 更新 `src/api/user.js` - 添加订阅API
- 更新 `src/router/index.js` - 添加定价页面路由
- 更新 `src/components/Layout.vue` - 添加导航菜单

### 支付功能扩展准备

当前支付功能是模拟实现，为未来真实支付系统预留了接口：

1. **支付方式选择**: 已准备微信支付和支付宝选项
2. **支付状态管理**: 完整的加载状态和错误处理
3. **订单信息**: 完整的订单详情显示
4. **回调处理**: 支付成功后的业务逻辑处理

### 扩展建议

未来可以扩展的功能：
1. 真实支付接口集成（微信支付/支付宝）
2. 订单历史记录
3. 自动续费功能
4. 优惠券系统
5. 用户订阅状态实时更新

## 访问方式

1. 直接访问: `http://localhost:3002/pricing`
2. 通过导航栏: 点击顶部导航的"定价"按钮
3. 从首页入口: 可添加定价页面入口链接

## 测试要点

1. 页面样式是否与整站风格一致
2. 三个方案的功能描述是否准确
3. FREE方案是否正确跳转到体验页面
4. 付费方案的支付流程是否完整
5. 支付成功后是否正确调用后端API
6. 响应式设计在不同设备上的表现 