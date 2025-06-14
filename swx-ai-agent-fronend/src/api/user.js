import api from './index'

export const userApi = {
  // 用户注册
  register(data) {
    return api.post('/user/register', data)
  },

  // 用户登录
  login(data) {
    return api.post('/user/login', data)
  },

  // 用户退出
  logout() {
    return api.post('/user/logout')
  },

  // 获取当前登录用户信息
  getLoginUser() {
    return api.get('/user/get/login')
  },

  // 获取用户个人中心完整信息
  getUserInfo() {
    return api.get('/user/get/user/info')
  },

  // 生成API Key
  generateApiKey() {
    return api.get('/user/generate/apikey')
  },

  // 获取本月调用统计
  getStatisticsByMon() {
    return api.post('/user/get/user/statistics')
  },

  // 用户订阅会员
  subscribe(vipLevel) {
    return api.post('/user/subscribe', null, {
      params: { vipLevel }
    })
  }
} 