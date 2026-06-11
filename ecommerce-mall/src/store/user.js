import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref(null)

  // 登录
  async function login(loginForm) {
    const res = await loginApi(loginForm)
    if (res.code === 200) {
      token.value = res.data.token
      userInfo.value = res.data.userInfo
      setToken(res.data.token)
      return true
    }
    return false
  }

  // 注册
  async function register(registerForm) {
    const res = await registerApi(registerForm)
    return res.code === 200
  }

  // 获取用户信息
  async function fetchUserInfo() {
    const res = await getUserInfo()
    if (res.code === 200) {
      userInfo.value = res.data
    }
  }

  // 退出登录
  function logout() {
    token.value = ''
    userInfo.value = null
    removeToken()
  }

  return {
    token,
    userInfo,
    login,
    register,
    fetchUserInfo,
    logout
  }
})
