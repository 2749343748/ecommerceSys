import request from '@/utils/request'

// 登录
export function login(data) {
  return request({
    url: '/api/login',
    method: 'post',
    data
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/api/logout',
    method: 'get'
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/info',
    method: 'get'
  })
}
