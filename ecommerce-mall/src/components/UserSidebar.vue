<template>
  <aside class="user-sidebar">
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="avatar-wrapper">
        <div class="avatar">
          <span>{{ firstLetter }}</span>
        </div>
        <div class="user-status">
          <span class="status-dot"></span>
          在线
        </div>
      </div>
      <div class="user-info">
        <h3 class="nickname">{{ userStore.userInfo?.nickname || '用户' }}</h3>
        <p class="username">ID: {{ userStore.userInfo?.id || '--' }}</p>
      </div>
    </div>

    <!-- 导航菜单 -->
    <nav class="nav-menu">
      <div class="menu-group">
        <div class="menu-title">个人中心</div>
        <router-link to="/user" class="menu-item" :class="{ active: active === 'center' }">
          <el-icon><HomeFilled /></el-icon>
          <span>我的主页</span>
        </router-link>
        <router-link to="/user/info" class="menu-item" :class="{ active: active === 'info' }">
          <el-icon><User /></el-icon>
          <span>个人资料</span>
        </router-link>
      </div>

      <div class="menu-group">
        <div class="menu-title">交易管理</div>
        <router-link to="/order" class="menu-item" :class="{ active: active === 'order' }">
          <el-icon><List /></el-icon>
          <span>我的订单</span>
        </router-link>
      </div>

      <div class="menu-group">
        <div class="menu-title">账户管理</div>
        <router-link to="/user/address" class="menu-item" :class="{ active: active === 'address' }">
          <el-icon><Location /></el-icon>
          <span>收货地址</span>
        </router-link>
      </div>
    </nav>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/store/user'

const props = defineProps({
  active: {
    type: String,
    default: 'center'
  }
})

const userStore = useUserStore()

const firstLetter = computed(() => {
  const name = userStore.userInfo?.nickname || userStore.userInfo?.username || 'U'
  return name.charAt(0).toUpperCase()
})
</script>

<style scoped>
.user-sidebar {
  width: 240px;
  flex-shrink: 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  align-self: stretch;
}

/* 用户卡片 */
.user-card {
  background: #469ef6;
  border-radius: 12px;
  padding: 28px 20px;
  text-align: center;
  margin-bottom: 24px;
  position: relative;
  overflow: hidden;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  border: 3px solid rgba(255, 255, 255, 0.2);
}

.user-status {
  position: absolute;
  bottom: -4px;
  right: -4px;
  background: #fff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 11px;
  color: #52c41a;
  display: flex;
  align-items: center;
  gap: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #52c41a;
}

.user-info {
  position: relative;
}

.nickname {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 4px;
}

.username {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
}

/* 导航菜单 */
.nav-menu {
  flex: 1;
}

.menu-group {
  padding: 8px 0;
}

.menu-group:not(:last-child) {
  border-bottom: 1px solid #f5f5f5;
}

.menu-title {
  padding: 12px 20px 8px;
  font-size: 12px;
  color: #999;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  margin: 2px 0;
  border-radius: 8px;
  color: #666;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s ease;
  position: relative;
}

.menu-item:hover {
  background: #f8f9fa;
  color: #333;
}

.menu-item.active {
  background: #f04949;
  color: #fff;
  font-weight: 500;
}

.menu-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: #fff;
  border-radius: 0 3px 3px 0;
}
</style>
