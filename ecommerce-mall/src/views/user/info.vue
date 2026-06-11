<template>
  <div class="info-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="center-layout">
          <UserSidebar active="info" />

          <div class="content">
            <div class="page-header">
              <h2>个人资料</h2>
            </div>

            <div class="info-card">
              <div class="card-header">
                <h3>基本信息</h3>
              </div>
              <div class="info-grid">
                <div class="info-item">
                  <div class="item-label">
                    <el-icon><User /></el-icon>
                    <span>用户名</span>
                  </div>
                  <div class="item-value">{{ userStore.userInfo?.username || '--' }}</div>
                </div>
                <div class="info-item">
                  <div class="item-label">
                    <el-icon><UserFilled /></el-icon>
                    <span>昵称</span>
                  </div>
                  <div class="item-value">{{ userStore.userInfo?.nickname || '--' }}</div>
                </div>
                <div class="info-item">
                  <div class="item-label">
                    <el-icon><Phone /></el-icon>
                    <span>手机号</span>
                  </div>
                  <div class="item-value">{{ userStore.userInfo?.phone || '--' }}</div>
                </div>
                <div class="info-item">
                  <div class="item-label">
                    <el-icon><Message /></el-icon>
                    <span>邮箱</span>
                  </div>
                  <div class="item-value">{{ userStore.userInfo?.email || '--' }}</div>
                </div>
                <div class="info-item">
                  <div class="item-label">
                    <el-icon><Timer /></el-icon>
                    <span>注册时间</span>
                  </div>
                  <div class="item-value">{{ userStore.userInfo?.createTime || '--' }}</div>
                </div>
              </div>
            </div>

            <div class="info-card">
              <div class="card-header">
                <h3>账户安全</h3>
              </div>
              <div class="security-list">
                <div class="security-item">
                  <div class="security-info">
                    <el-icon :size="20"><Lock /></el-icon>
                    <div>
                      <span class="security-label">登录密码</span>
                      <span class="security-desc">已设置</span>
                    </div>
                  </div>
                  <button class="btn-modify" @click="handleChangePassword">修改</button>
                </div>
                <div class="security-item">
                  <div class="security-info">
                    <el-icon :size="20"><Phone /></el-icon>
                    <div>
                      <span class="security-label">手机绑定</span>
                      <span class="security-desc">{{ userStore.userInfo?.phone ? '已绑定' : '未绑定' }}</span>
                    </div>
                  </div>
                  <button class="btn-modify">{{ userStore.userInfo?.phone ? '更换' : '绑定' }}</button>
                </div>
                <div class="security-item">
                  <div class="security-info">
                    <el-icon :size="20"><Message /></el-icon>
                    <div>
                      <span class="security-label">邮箱绑定</span>
                      <span class="security-desc">{{ userStore.userInfo?.email ? '已绑定' : '未绑定' }}</span>
                    </div>
                  </div>
                  <button class="btn-modify">{{ userStore.userInfo?.email ? '更换' : '绑定' }}</button>
                </div>
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
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import UserSidebar from '@/components/UserSidebar.vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const handleChangePassword = () => {
  ElMessage.info('密码修改功能开发中')
}
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 24px 0;
}

.center-layout {
  display: flex;
  gap: 24px;
}

.content {
  flex: 1;
  min-width: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.info-card {
  background: #fff;
  border-radius: 12px;
  margin-bottom: 20px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.card-header {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.info-grid {
  padding: 24px;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-item:last-child {
  border-bottom: none;
}

.item-label {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
  font-size: 14px;
}

.item-label .el-icon {
  color: #999;
}

.item-value {
  font-size: 14px;
  color: #333;
}

.security-list {
  padding: 8px 24px;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
}

.security-item:last-child {
  border-bottom: none;
}

.security-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.security-info .el-icon {
  color: #999;
}

.security-label {
  display: block;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.security-desc {
  display: block;
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.btn-modify {
  padding: 6px 16px;
  background: #f5f5f5;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-modify:hover {
  background: #eee;
  color: #333;
}
</style>
