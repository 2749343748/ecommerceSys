<template>
  <div class="order-list-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="center-layout">
          <UserSidebar active="order" />

          <div class="content">
            <div class="page-header">
              <h2>我的订单</h2>
              <span class="order-count">共 {{ total }} 个订单</span>
            </div>

            <!-- 状态筛选 -->
            <div class="status-tabs">
              <button
                v-for="tab in statusTabs"
                :key="tab.value"
                :class="['tab-item', { active: currentStatus === tab.value }]"
                @click="changeStatus(tab.value)"
              >
                <span class="tab-label">{{ tab.label }}</span>
              </button>
            </div>

            <!-- 订单列表 -->
            <div v-if="orders.length" class="order-list">
              <div v-for="order in orders" :key="order.id" class="order-card">
                <div class="order-header">
                  <div class="order-meta">
                    <span class="order-no">订单号：{{ order.orderNo }}</span>
                    <span class="order-time">{{ order.createTime }}</span>
                  </div>
                  <span :class="['order-status', `status-${order.status}`]">
                    {{ getStatusText(order.status) }}
                  </span>
                </div>
                <div class="order-body">
                  <!-- 商品列表 -->
                  <div class="goods-list">
                    <div v-for="item in order.orderItems" :key="item.id" class="goods-item">
                      <div class="goods-image">
                        <img :src="item.productImage || '/placeholder.png'" :alt="item.productName" />
                      </div>
                      <div class="goods-info">
                        <h4 class="goods-name">{{ item.productName }}</h4>
                        <div class="goods-meta">
                          <span class="goods-price">¥{{ item.price }}</span>
                          <span class="goods-quantity">× {{ item.quantity }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="order-amount">
                    <span class="amount-label">订单金额</span>
                    <span class="price">¥{{ order.totalAmount }}</span>
                    <span class="goods-count">共 {{ order.orderItems?.length || 0 }} 件商品</span>
                  </div>
                </div>
                <div class="order-footer">
                  <router-link :to="`/order/${order.id}`" class="btn-action btn-detail">
                    <el-icon><View /></el-icon>
                    查看详情
                  </router-link>
                  <router-link v-if="order.status === 0" :to="`/order/pay/${order.id}`" class="btn-action btn-pay">
                    <el-icon><Wallet /></el-icon>
                    去支付
                  </router-link>
                  <button v-if="order.status === 0" class="btn-action btn-cancel" @click="handleCancel(order.id)">
                    <el-icon><Close /></el-icon>
                    取消订单
                  </button>
                  <button v-if="order.status === 2" class="btn-action btn-confirm" @click="handleConfirm(order.id)">
                    <el-icon><Check /></el-icon>
                    确认收货
                  </button>
                </div>
              </div>
            </div>

            <div v-else class="empty-state">
              <div class="empty-icon">
                <el-icon :size="48"><Document /></el-icon>
              </div>
              <h3>暂无订单</h3>
              <p>快去挑选心仪的商品吧</p>
              <router-link to="/product" class="btn-shopping">
                <el-icon><ShoppingBag /></el-icon>
                去购物
              </router-link>
            </div>

            <!-- 分页 -->
            <div v-if="total > pageSize" class="pagination">
              <button
                class="page-btn"
                :disabled="pageNum <= 1"
                @click="changePage(pageNum - 1)"
              >
                <el-icon><ArrowLeft /></el-icon>
              </button>
              <div class="page-info">
                <span class="current">{{ pageNum }}</span>
                <span class="separator">/</span>
                <span class="total">{{ Math.ceil(total / pageSize) }}</span>
              </div>
              <button
                class="page-btn"
                :disabled="pageNum >= Math.ceil(total / pageSize)"
                @click="changePage(pageNum + 1)"
              >
                <el-icon><ArrowRight /></el-icon>
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import UserSidebar from '@/components/UserSidebar.vue'
import { useUserStore } from '@/store/user'
import { getUserOrders, cancelOrder, confirmOrder } from '@/api/order'

const userStore = useUserStore()

const orders = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const currentStatus = ref(null)

const statusTabs = [
  { label: '全部', value: null },
  { label: '待支付', value: 0 },
  { label: '已支付', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已完成', value: 3 },
  { label: '已取消', value: 4 }
]

const getStatusText = (status) => {
  const map = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

const loadOrders = async () => {
  try {
    const params = {
      userId: userStore.userInfo?.id,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (currentStatus.value !== null) {
      params.status = currentStatus.value
    }
    const res = await getUserOrders(params)
    if (res.code === 200) {
      orders.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  }
}

const changeStatus = (status) => {
  currentStatus.value = status
  pageNum.value = 1
  loadOrders()
}

const changePage = (page) => {
  pageNum.value = page
  loadOrders()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const handleCancel = async (id) => {
  await ElMessageBox.confirm('确定要取消该订单吗？', '提示', { type: 'warning' })
  try {
    await cancelOrder(id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    console.error(error)
  }
}

const handleConfirm = async (id) => {
  await ElMessageBox.confirm('确定已收到商品吗？', '提示', { type: 'warning' })
  try {
    await confirmOrder(id)
    ElMessage.success('已确认收货')
    loadOrders()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadOrders()
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

/* 页面头部 */
.page-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.order-count {
  font-size: 13px;
  color: #999;
}

/* 状态标签 */
.status-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: #fff;
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.tab-item {
  flex: 1;
  padding: 10px 16px;
  background: transparent;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-item:hover {
  background: #f5f5f5;
  color: #333;
}

.tab-item.active {
  background: #333;
  color: #fff;
  font-weight: 500;
}

/* 订单卡片 */
.order-card {
  background: #fff;
  border-radius: 12px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.2s;
}

.order-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.order-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.order-meta {
  display: flex;
  align-items: center;
  gap: 20px;
}

.order-no {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.order-time {
  font-size: 13px;
  color: #999;
}

.order-status {
  font-size: 13px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
}

.status-0 {
  background: #fff7e6;
  color: #fa8c16;
}

.status-1 {
  background: #e6f7ff;
  color: #1890ff;
}

.status-2 {
  background: #fff7e6;
  color: #fa8c16;
}

.status-3 {
  background: #f6ffed;
  color: #52c41a;
}

.status-4 {
  background: #f5f5f5;
  color: #999;
}

.order-body {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 20px;
  gap: 20px;
}

/* 商品列表 */
.goods-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.goods-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.goods-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background: #f5f5f5;
  flex-shrink: 0;
}

.goods-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.goods-info {
  flex: 1;
  min-width: 0;
}

.goods-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.goods-price {
  font-size: 14px;
  color: #666;
}

.goods-quantity {
  font-size: 13px;
  color: #999;
}

.order-amount {
  text-align: right;
  flex-shrink: 0;
}

.amount-label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.order-amount .price {
  display: block;
  font-size: 22px;
  font-weight: 600;
  color: #ff4757;
  margin-bottom: 4px;
}

.goods-count {
  font-size: 12px;
  color: #999;
}

.order-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-action {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
}

.btn-detail {
  background: #f5f5f5;
  color: #666;
}

.btn-detail:hover {
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

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.empty-icon {
  width: 80px;
  height: 80px;
  background: #f5f5f5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: #ccc;
}

.empty-state h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.empty-state p {
  font-size: 14px;
  color: #999;
  margin-bottom: 24px;
}

.btn-shopping {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: #333;
  color: #fff;
  border-radius: 8px;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-shopping:hover {
  background: #444;
  transform: translateY(-2px);
}

/* 分页 */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 32px 0;
}

.page-btn {
  width: 40px;
  height: 40px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.page-info .current {
  font-weight: 600;
  color: #333;
}

.page-info .separator {
  color: #ccc;
}

.page-info .total {
  color: #999;
}
</style>
