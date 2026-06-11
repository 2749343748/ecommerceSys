import request from './request'

// 登录
export function login(data) {
  return request({
    url: '/api/login',
    method: 'post',
    data
  })
}

// 注册
export function register(data) {
  return request({
    url: '/api/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/info',
    method: 'get'
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/api/logout',
    method: 'get'
  })
}
