<template>
  <div class="order-detail-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="center-layout">
          <UserSidebar active="order" />

          <div class="content">
            <!-- 面包屑 -->
            <div class="breadcrumb">
              <router-link to="/order">我的订单</router-link>
              <el-icon><ArrowRight /></el-icon>
              <span>订单详情</span>
            </div>

            <div v-if="order.id" class="order-detail">
              <!-- 订单状态卡片 -->
              <div :class="['status-card', `status-${order.status}`]">
                <div class="status-icon">
                  <el-icon :size="40">
                    <component :is="statusIcon" />
                  </el-icon>
                </div>
                <div class="status-info">
                  <h2>{{ getStatusText(order.status) }}</h2>
                  <p>订单号：{{ order.orderNo }}</p>
                </div>
                <div class="status-tips">
                  {{ getStatusTip(order.status) }}
                </div>
              </div>

              <!-- 收货信息 -->
              <div class="section-card">
                <div class="section-header">
                  <h3>
                    <el-icon><Location /></el-icon>
                    收货信息
                  </h3>
                </div>
                <div class="info-grid">
                  <div class="info-item">
                    <span class="info-label">收货人</span>
                    <span class="info-value">{{ order.receiverName }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">联系电话</span>
                    <span class="info-value">{{ order.receiverPhone }}</span>
                  </div>
                  <div class="info-item full-width">
                    <span class="info-label">收货地址</span>
                    <span class="info-value">{{ order.receiverAddress }}</span>
                  </div>
                </div>
              </div>

              <!-- 订单信息 -->
              <div class="section-card">
                <div class="section-header">
                  <h3>
                    <el-icon><Document /></el-icon>
                    订单信息
                  </h3>
                </div>
                <div class="info-grid">
                  <div class="info-item">
                    <span class="info-label">订单号</span>
                    <span class="info-value">{{ order.orderNo }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">创建时间</span>
                    <span class="info-value">{{ order.createTime }}</span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">订单状态</span>
                    <span :class="['info-value', 'status-text', `status-${order.status}`]">
                      {{ getStatusText(order.status) }}
                    </span>
                  </div>
                  <div class="info-item">
                    <span class="info-label">订单金额</span>
                    <span class="info-value price">¥{{ order.totalAmount }}</span>
                  </div>
                  <div v-if="order.payMethod" class="info-item">
                    <span class="info-label">支付方式</span>
                    <span class="info-value">{{ getPayMethodText(order.payMethod) }}</span>
                  </div>
                  <div v-if="order.payTime" class="info-item">
                    <span class="info-label">支付时间</span>
                    <span class="info-value">{{ order.payTime }}</span>
                  </div>
                </div>
              </div>

              <!-- 操作按钮 -->
              <div class="action-bar">
                <router-link to="/order" class="btn-action btn-back">
                  <el-icon><ArrowLeft /></el-icon>
                  返回订单列表
                </router-link>
                <div class="action-right">
                  <router-link v-if="order.status === 0" :to="`/order/pay/${order.id}`" class="btn-action btn-pay">
                    <el-icon><Wallet /></el-icon>
                    去支付
                  </router-link>
                  <button v-if="order.status === 0" class="btn-action btn-cancel" @click="handleCancel">
                    <el-icon><Close /></el-icon>
                    取消订单
                  </button>
                  <button v-if="order.status === 2" class="btn-action btn-confirm" @click="handleConfirm">
                    <el-icon><Check /></el-icon>
                    确认收货
                  </button>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import UserSidebar from '@/components/UserSidebar.vue'
import { getOrderDetail, cancelOrder, confirmOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()

const order = ref({})

const statusIcon = computed(() => {
  const icons = { 0: 'Wallet', 1: 'Check', 2: 'Van', 3: 'CircleCheck', 4: 'Close' }
  return icons[order.value.status] || 'Document'
})

const getStatusText = (status) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

const getStatusTip = (status) => {
  const tips = {
    0: '请尽快完成支付，超时订单将自动取消',
    1: '商家正在准备发货，请耐心等待',
    2: '商品正在配送中，请注意查收',
    3: '订单已完成，感谢您的购买',
    4: '订单已取消'
  }
  return tips[status] || ''
}

const getPayMethodText = (method) => {
  const map = {
    alipay: '支付宝',
    wechat: '微信支付',
    balance: '余额支付'
  }
  return map[method] || '未知'
}

const loadOrder = async () => {
  try {
    const res = await getOrderDetail(route.params.id)
    if (res.code === 200) {
      order.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

const handleCancel = async () => {
  await ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
  try {
    await cancelOrder(order.value.id)
    ElMessage.success('订单已取消')
    loadOrder()
  } catch (error) {
    console.error(error)
  }
}

const handleConfirm = async () => {
  await ElMessageBox.confirm('确定已收到商品吗？', '提示', { type: 'warning' })
  try {
    await confirmOrder(order.value.id)
    ElMessage.success('已确认收货')
    loadOrder()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadOrder()
})
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

/* 面包屑 */
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  font-size: 14px;
}

.breadcrumb a {
  color: #666;
  text-decoration: none;
}

.breadcrumb a:hover {
  color: var(--primary-color);
}

.breadcrumb .el-icon {
  font-size: 12px;
  color: #ccc;
}

.breadcrumb span {
  color: #333;
  font-weight: 500;
}

/* 状态卡片 */
.status-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  position: relative;
  overflow: hidden;
}

.status-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
}

.status-card.status-0::before { background: linear-gradient(90deg, #f093fb, #f5576c); }
.status-card.status-1::before { background: linear-gradient(90deg, #4facfe, #00f2fe); }
.status-card.status-2::before { background: linear-gradient(90deg, #43e97b, #38f9d7); }
.status-card.status-3::before { background: linear-gradient(90deg, #fa709a, #fee140); }
.status-card.status-4::before { background: linear-gradient(90deg, #a8a8a8, #d0d0d0); }

.status-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.status-0 .status-icon { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.status-1 .status-icon { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.status-2 .status-icon { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.status-3 .status-icon { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
.status-4 .status-icon { background: linear-gradient(135deg, #a8a8a8 0%, #d0d0d0 100%); }

.status-info h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.status-info p {
  font-size: 13px;
  color: #999;
}

.status-tips {
  margin-left: auto;
  font-size: 13px;
  color: #666;
  background: #f8f9fa;
  padding: 8px 16px;
  border-radius: 8px;
}

/* 区块卡片 */
.section-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.section-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-header h3 .el-icon {
  color: #333;
}

/* 信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-item.full-width {
  grid-column: span 2;
}

.info-label {
  font-size: 13px;
  color: #999;
}

.info-value {
  font-size: 14px;
  color: #333;
}

.info-value.price {
  font-size: 20px;
  font-weight: 600;
  color: #ff4757;
}

.status-text.status-0 { color: #fa8c16; }
.status-text.status-1 { color: #1890ff; }
.status-text.status-2 { color: #fa8c16; }
.status-text.status-3 { color: #52c41a; }
.status-text.status-4 { color: #999; }

/* 操作栏 */
.action-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 24px;
}

.action-right {
  display: flex;
  gap: 12px;
}

.btn-action {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
}

.btn-back {
  background: #f5f5f5;
  color: #666;
}

.btn-back:hover {
  background: #eee;
  color: #333;
}

.btn-pay {
  background: #ff4757;
  color: #fff;
}

.btn-pay:hover {
  background: #ff6b6b;
  transform: translateY(-1px);
}

.btn-cancel {
  background: #fff0f0;
  color: #ff4757;
}

.btn-cancel:hover {
  background: #ffe0e0;
}

.btn-confirm {
  background: #333;
  color: #fff;
}

.btn-confirm:hover {
  background: #444;
  transform: translateY(-1px);
}
</style>
