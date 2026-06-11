<template>
  <div class="center-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="center-layout">
          <UserSidebar active="center" />

          <!-- 内容区 -->
          <div class="content">
            <!-- 订单快捷入口 -->
            <div class="section-card">
              <div class="section-header">
                <h3>我的订单</h3>
                <router-link to="/order" class="view-all">查看全部 →</router-link>
              </div>
              <div class="order-shortcuts">
                <router-link to="/order?status=0" class="shortcut-item">
                  <el-icon :size="32" class="shortcut-icon"><Wallet /></el-icon>
                  <span class="shortcut-label">待支付</span>
                </router-link>
                <router-link to="/order?status=1" class="shortcut-item">
                  <el-icon :size="32" class="shortcut-icon"><Box /></el-icon>
                  <span class="shortcut-label">待发货</span>
                </router-link>
                <router-link to="/order?status=2" class="shortcut-item">
                  <el-icon :size="32" class="shortcut-icon"><Van /></el-icon>
                  <span class="shortcut-label">待收货</span>
                </router-link>
                <router-link to="/order?status=3" class="shortcut-item">
                  <el-icon :size="32" class="shortcut-icon"><CircleCheck /></el-icon>
                  <span class="shortcut-label">已完成</span>
                </router-link>
              </div>
            </div>

            <!-- 个人信息 -->
            <div class="section-card">
              <div class="section-header">
                <h3>基本信息</h3>
              </div>
              <div class="info-grid">
                <div class="info-item">
                  <div class="info-icon">
                    <el-icon><User /></el-icon>
                  </div>
                  <div class="info-content">
                    <span class="info-label">用户名</span>
                    <span class="info-value">{{ userStore.userInfo?.username || '--' }}</span>
                  </div>
                </div>
                <div class="info-item">
                  <div class="info-icon">
                    <el-icon><UserFilled /></el-icon>
                  </div>
                  <div class="info-content">
                    <span class="info-label">昵称</span>
                    <span class="info-value">{{ userStore.userInfo?.nickname || '--' }}</span>
                  </div>
                </div>
                <div class="info-item">
                  <div class="info-icon">
                    <el-icon><Phone /></el-icon>
                  </div>
                  <div class="info-content">
                    <span class="info-label">手机号</span>
                    <span class="info-value">{{ userStore.userInfo?.phone || '--' }}</span>
                  </div>
                </div>
                <div class="info-item">
                  <div class="info-icon">
                    <el-icon><Message /></el-icon>
                  </div>
                  <div class="info-content">
                    <span class="info-label">邮箱</span>
                    <span class="info-value">{{ userStore.userInfo?.email || '--' }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 快捷操作 -->
            <div class="section-card">
              <div class="section-header">
                <h3>快捷操作</h3>
              </div>
              <div class="quick-actions">
                <router-link to="/user/address" class="action-item">
                  <el-icon :size="20"><Location /></el-icon>
                  <span>收货地址</span>
                </router-link>
                <router-link to="/product" class="action-item">
                  <el-icon :size="20"><Goods /></el-icon>
                  <span>商品分类</span>
                </router-link>
                <router-link to="/search" class="action-item">
                  <el-icon :size="20"><Search /></el-icon>
                  <span>搜索商品</span>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import UserSidebar from '@/components/UserSidebar.vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 24px 0;
}

.center-layout {
  display: flex;
  gap: 24px;
  align-items: stretch;
}

/* 内容区 */
.content {
  flex: 1;
  min-width: 0;
  max-height: calc(100vh - 64px - 48px);
  overflow-y: auto;
  padding-right: 4px;
}

.content::-webkit-scrollbar {
  width: 6px;
}

.content::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 3px;
}

.content::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

/* 区块卡片 */
.section-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.section-card:last-child {
  margin-bottom: 0;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.view-all {
  font-size: 13px;
  color: #999;
  text-decoration: none;
  transition: color 0.2s;
}

.view-all:hover {
  color: var(--primary-color);
}

/* 订单快捷入口 */
.order-shortcuts {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.shortcut-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 24px 16px;
  background: #fafafa;
  border-radius: 12px;
  text-decoration: none;
  color: #666;
  transition: all 0.2s;
  min-height: 100px;
}

.shortcut-item:hover {
  background: #f0f0f0;
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.shortcut-icon {
  color: #333;
  flex-shrink: 0;
}

.shortcut-label {
  font-size: 14px;
  font-weight: 500;
  line-height: 1;
}

/* 个人信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 18px 16px;
  background: #f8f9fa;
  border-radius: 12px;
  min-height: 72px;
}

.info-icon {
  width: 40px;
  height: 40px;
  background: #f0f0f0;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;
  flex-shrink: 0;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
}

.info-label {
  font-size: 12px;
  color: #999;
  line-height: 1;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  line-height: 1.2;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 20px 12px;
  background: #f8f9fa;
  border-radius: 12px;
  text-decoration: none;
  color: #666;
  font-size: 13px;
  transition: all 0.2s;
  min-height: 88px;
}

.action-item:hover {
  background: #f0f5ff;
  color: var(--primary-color);
  transform: translateY(-2px);
}
</style>
