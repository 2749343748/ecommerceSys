<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <h1>品质商城</h1>
          <p>用户注册</p>
        </div>
        <form class="register-form" @submit.prevent="handleRegister">
          <div class="form-group">
            <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              autocomplete="username"
            />
          </div>
          <div class="form-group">
            <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码（至少6位）"
              autocomplete="new-password"
            />
          </div>
          <div class="form-group">
            <input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请确认密码"
              autocomplete="new-password"
            />
          </div>
          <div class="form-group">
            <input
              v-model="form.nickname"
              type="text"
              placeholder="请输入昵称（选填）"
            />
          </div>
          <div class="form-group">
            <input
              v-model="form.phone"
              type="text"
              placeholder="请输入手机号（选填）"
            />
          </div>
          <button type="submit" class="btn-register" :disabled="loading">
            {{ loading ? '注册中...' : '注 册' }}
          </button>
        </form>
        <div class="register-footer">
          <span>已有账号？</span>
          <router-link to="/user/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  phone: ''
})

const handleRegister = async () => {
  if (!form.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!form.password || form.password.length < 6) {
    ElMessage.warning('密码长度不能少于6位')
    return
  }
  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  loading.value = true
  try {
    const success = await userStore.register({
      username: form.username,
      password: form.password,
      nickname: form.nickname || form.username,
      phone: form.phone
    })
    if (success) {
      ElMessage.success('注册成功，请登录')
      router.push('/user/login')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
}

.register-card {
  width: 400px;
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.register-header {
  text-align: center;
  margin-bottom: 36px;
}

.register-header h1 {
  font-size: 28px;
  color: var(--primary-color);
  margin-bottom: 8px;
}

.register-header p {
  color: var(--text-light);
  font-size: 15px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group input {
  width: 100%;
  height: 46px;
  padding: 0 16px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus {
  border-color: var(--primary-color);
}

.btn-register {
  width: 100%;
  height: 48px;
  background: var(--primary-color);
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 8px;
}

.btn-register:hover:not(:disabled) {
  background: var(--primary-dark);
}

.btn-register:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: var(--text-light);
}

.register-footer a {
  color: var(--primary-color);
  text-decoration: none;
  margin-left: 4px;
}
</style>
