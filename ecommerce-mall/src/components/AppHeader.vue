<template>
  <header class="header">
    <div class="container header-inner">
      <!-- Logo -->
      <router-link to="/" class="logo">
        <span class="logo-icon">◆</span>
        <span class="logo-text">品质商城</span>
      </router-link>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <input
          v-model="keyword"
          type="text"
          placeholder="搜索商品"
          @keyup.enter="handleSearch"
        />
        <button class="search-btn" @click="handleSearch">
          <el-icon><Search /></el-icon>
        </button>
      </div>

      <!-- 右侧操作 -->
      <div class="header-actions">
        <!-- 购物车 -->
        <router-link to="/cart" class="action-item cart-link">
          <el-icon :size="22"><ShoppingCart /></el-icon>
          <span class="cart-text">购物车</span>
          <span v-if="cartStore.cartCount > 0" class="cart-badge">{{ cartStore.cartCount }}</span>
        </router-link>

        <!-- 用户菜单 -->
        <div v-if="userStore.token" class="user-menu" @mouseenter="showUserMenu = true" @mouseleave="showUserMenu = false">
          <span class="action-item">
            <el-icon :size="20"><User /></el-icon>
            <span>{{ userStore.userInfo?.nickname || '用户' }}</span>
          </span>
          <div v-show="showUserMenu" class="user-dropdown">
            <router-link to="/user" class="dropdown-item">个人中心</router-link>
            <router-link to="/order" class="dropdown-item">我的订单</router-link>
            <router-link to="/user/address" class="dropdown-item">收货地址</router-link>
            <div class="dropdown-divider"></div>
            <span class="dropdown-item" @click="handleLogout">退出登录</span>
          </div>
        </div>

        <!-- 未登录 -->
        <template v-else>
          <router-link to="/user/login" class="action-item">登录</router-link>
          <router-link to="/user/register" class="action-item">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const keyword = ref('')
const showUserMenu = ref(false)

const handleSearch = () => {
  if (keyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: keyword.value.trim() } })
  }
}

const handleLogout = () => {
  userStore.logout()
  cartStore.cartList = []
  router.push('/')
}

onMounted(() => {
  if (userStore.token && !userStore.userInfo) {
    userStore.fetchUserInfo().then(() => {
      cartStore.fetchCartList()
    })
  } else if (userStore.token) {
    cartStore.fetchCartList()
  }
})
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  display: flex;
  align-items: center;
  height: 64px;
  gap: 40px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  flex-shrink: 0;
}

.logo-icon {
  font-size: 24px;
  color: var(--primary-color);
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
}

.search-bar {
  flex: 1;
  max-width: 500px;
  display: flex;
  border: 2px solid var(--primary-color);
  border-radius: 4px;
  overflow: hidden;
}

.search-bar input {
  flex: 1;
  height: 40px;
  padding: 0 16px;
  border: none;
  outline: none;
  font-size: 14px;
}

.search-btn {
  width: 50px;
  height: 40px;
  background: var(--primary-color);
  border: none;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn:hover {
  background: var(--primary-dark);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-shrink: 0;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  position: relative;
}

.action-item:hover {
  color: var(--primary-color);
}

.cart-link {
  padding: 6px 12px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
}

.cart-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: var(--danger-color);
  color: #fff;
  font-size: 12px;
  min-width: 18px;
  height: 18px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.user-menu {
  position: relative;
}

.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background: #fff;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-width: 120px;
  z-index: 100;
}

.dropdown-item {
  display: block;
  padding: 10px 16px;
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
}

.dropdown-item:hover {
  background: #f5f5f5;
  color: var(--primary-color);
}

.dropdown-divider {
  height: 1px;
  background: var(--border-color);
}
</style>
