# 错误处理弹窗完善

## 概述

根据后端自定义异常类的统一返回格式，完善了前端的错误处理机制，提供更好的用户体验和更清晰的错误提示。

## 后端错误格式

后端统一返回格式：
```json
{
  "code": 40101,
  "data": null,
  "message": "无权限"
}
```

- `code = 0`: 成功
- `code != 0`: 错误（包含具体错误码）

## 前端改进

### 1. 创建全局错误配置 (`src/utils/errorHandler.js`)

#### 错误码映射表
```javascript
export const ERROR_CODE_MAP = {
  // 权限相关 (401xx)
  40101: '无权限',
  40102: '请先登录',
  40103: 'token已过期',
  
  // 参数相关 (400xx)
  40001: '请求参数错误',
  
  // 用户相关 (501xx)
  50102: '用户名或密码错误',
  50103: '用户已存在',
  
  // 订阅相关 (502xx)
  50201: '订阅失败',
  50203: '订阅已过期',
  50205: '余额不足',
  
  // AI服务相关 (503xx)
  50302: '请求次数已用完',
  50304: '请求内容包含敏感信息',
  
  // ... 更多错误码
}
```

#### 特殊处理配置
```javascript
export const SPECIAL_ERROR_CODES = {
  // 订阅相关错误，引导用户到定价页面
  50205: { 
    type: 'redirect', 
    path: '/pricing', 
    message: '余额不足，请选择合适的订阅方案' 
  },
  50203: { 
    type: 'redirect', 
    path: '/pricing', 
    message: '订阅已过期，请续费' 
  },
  
  // AI服务错误，特殊提示
  50302: { 
    type: 'info', 
    message: '今日请求次数已用完，请明天再试或升级订阅方案' 
  }
}
```

### 2. 完善API拦截器 (`src/api/index.js`)

#### 统一业务错误处理
```javascript
const handleBusinessError = (data) => {
  const { code, message } = data
  const errorMessage = message || getErrorMessage(code)
  
  // 检查各种特殊情况
  if (isSilentError(code)) return // 静默处理
  if (isLoginRequired(code)) return // 跳转登录
  
  // 特殊处理（如跳转定价页面）
  const specialHandling = getSpecialHandling(code)
  if (specialHandling) {
    // 显示确认对话框或特殊提示
  }
  
  // 根据错误类型显示不同样式的消息
  const errorType = getErrorType(code)
  switch (errorType) {
    case ERROR_TYPES.PERMISSION:
      ElMessage.warning(errorMessage)
      break
    case ERROR_TYPES.VALIDATION:
      ElMessage.warning(errorMessage)
      break
    default:
      ElMessage.error(errorMessage)
  }
}
```

#### 网络错误处理
- 超时错误：`请求超时，请检查网络连接`
- 网络断开：`网络连接失败，请检查网络`
- HTTP状态码：`网络错误 (${status})`
- 请求过频：`请求过于频繁，请稍后重试`

### 3. 优化业务逻辑错误处理

#### 用户相关操作 (`src/stores/user.js`)
- 移除重复的错误提示（API拦截器已处理）
- 简化成功/失败判断逻辑
- 保留必要的状态清理

#### 订阅操作 (`src/views/Pricing.vue`)
- 移除重复的错误提示
- 依赖API拦截器的统一错误处理

## 用户体验改进

### 1. 错误提示类型化
- **警告(warning)**: 权限错误、参数验证错误
- **错误(error)**: 系统错误、业务错误
- **信息(info)**: 服务限制提示

### 2. 智能跳转
- **登录相关错误**: 自动跳转登录页面
- **订阅相关错误**: 引导跳转定价页面
- **权限不足**: 显示错误但不跳转

### 3. 特殊场景处理
- **余额不足/订阅过期**: 确认对话框 → 跳转定价页面
- **请求次数用完**: 信息提示 + 升级建议
- **敏感内容**: 警告提示 + 修改建议

## 错误处理流程

```
1. 后端返回错误响应
   ↓
2. API拦截器捕获
   ↓
3. 判断错误类型
   ├─ 登录相关 → 跳转登录页面
   ├─ 订阅相关 → 引导定价页面
   ├─ 参数验证 → 警告提示
   └─ 其他错误 → 错误提示
   ↓
4. 业务层处理返回值
```

## 配置扩展性

### 添加新错误码
```javascript
// 在 errorHandler.js 中添加
export const ERROR_CODE_MAP = {
  // ... 现有错误码
  50999: '新的错误类型'
}
```

### 添加特殊处理
```javascript
export const SPECIAL_ERROR_CODES = {
  // ... 现有特殊处理
  50999: { 
    type: 'redirect', 
    path: '/new-page', 
    message: '需要特殊处理的错误' 
  }
}
```

## 测试场景

### 权限错误测试
- 未登录访问需要权限的接口
- token过期后的接口调用
- 权限不足的操作

### 业务错误测试
- 参数验证失败
- 数据不存在
- 重复操作

### 订阅相关测试
- 余额不足时的订阅操作
- 订阅过期后的服务使用
- 请求次数超限

### 网络错误测试
- 网络断开情况
- 请求超时情况
- 服务器错误

## 文件修改列表

- `src/utils/errorHandler.js` - 新增：全局错误处理配置
- `src/api/index.js` - 完善：API拦截器错误处理
- `src/stores/user.js` - 优化：移除重复错误提示
- `src/views/Pricing.vue` - 优化：订阅错误处理

## 优势

1. **统一性**: 所有错误都有统一的处理逻辑
2. **可扩展**: 配置化的错误码和处理方式
3. **用户友好**: 根据错误类型提供不同的用户体验
4. **智能化**: 自动引导用户处理错误
5. **可维护**: 错误处理逻辑集中管理 