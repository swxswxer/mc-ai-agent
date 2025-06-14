// 全局错误处理配置

// 错误码映射表
export const ERROR_CODE_MAP = {
  // 权限相关 (401xx)
  40101: '无权限',
  40102: '请先登录',
  40103: 'token已过期',
  40104: '账号被禁用',
  40105: '登录已失效，请重新登录',
  
  // 参数相关 (400xx)
  40001: '请求参数错误',
  40002: '请求参数为空',
  40003: '请求参数类型错误',
  40004: '请求参数格式不正确',
  40005: '请求参数超出范围',
  
  // 业务相关 (500xx)
  50001: '系统繁忙，请稍后重试',
  50002: '操作失败',
  50003: '数据不存在',
  50004: '数据已存在',
  50005: '数据状态异常',
  
  // 用户相关 (501xx)
  50101: '用户不存在',
  50102: '用户名或密码错误',
  50103: '用户已存在',
  50104: '验证码错误',
  50105: '用户名格式不正确',
  50106: '密码强度不够',
  50107: '邮箱格式不正确',
  50108: '手机号格式不正确',
  
  // 订阅相关 (502xx)
  50201: '订阅失败',
  50202: '订阅已存在',
  50203: '订阅已过期',
  50204: '订阅类型不支持',
  50205: '余额不足',
  50206: '支付失败',
  
  // AI服务相关 (503xx)
  50301: 'AI服务暂时不可用',
  50302: '请求次数已用完',
  50303: '请求内容过长',
  50304: '请求内容包含敏感信息',
  50305: 'AI服务响应超时',
  
  // 文件相关 (504xx)
  50401: '文件上传失败',
  50402: '文件格式不支持',
  50403: '文件大小超出限制',
  50404: '文件不存在',
  
  // 其他
  50000: '系统错误',
  50999: '未知错误'
}

// 需要跳转登录的错误码
export const LOGIN_REQUIRED_CODES = [40102, 40103, 40105]

// 需要静默处理的错误码（不显示错误提示）
export const SILENT_ERROR_CODES = []

// 需要特殊处理的错误码
export const SPECIAL_ERROR_CODES = {
  // 订阅相关错误，可能需要跳转到定价页面
  50205: { type: 'redirect', path: '/pricing', message: '余额不足，请选择合适的订阅方案' },
  50203: { type: 'redirect', path: '/pricing', message: '订阅已过期，请续费' },
  
  // AI服务错误，可能需要特殊提示
  50302: { type: 'info', message: '今日请求次数已用完，请明天再试或升级订阅方案' },
  50304: { type: 'warning', message: '请求内容包含敏感信息，请修改后重试' },
}

// 根据错误码获取错误信息
export function getErrorMessage(code, defaultMessage = '操作失败') {
  return ERROR_CODE_MAP[code] || defaultMessage
}

// 判断是否需要跳转登录
export function isLoginRequired(code) {
  return LOGIN_REQUIRED_CODES.includes(code)
}

// 判断是否需要静默处理
export function isSilentError(code) {
  return SILENT_ERROR_CODES.includes(code)
}

// 获取特殊处理配置
export function getSpecialHandling(code) {
  return SPECIAL_ERROR_CODES[code]
}

// 错误类型枚举
export const ERROR_TYPES = {
  NETWORK: 'network',      // 网络错误
  BUSINESS: 'business',    // 业务错误
  PERMISSION: 'permission', // 权限错误
  VALIDATION: 'validation', // 参数验证错误
  SYSTEM: 'system'         // 系统错误
}

// 根据错误码判断错误类型
export function getErrorType(code) {
  if (code >= 40100 && code < 40200) return ERROR_TYPES.PERMISSION
  if (code >= 40000 && code < 40100) return ERROR_TYPES.VALIDATION
  if (code >= 50000 && code < 60000) return ERROR_TYPES.BUSINESS
  return ERROR_TYPES.SYSTEM
} 