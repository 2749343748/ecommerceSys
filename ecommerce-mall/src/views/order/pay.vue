<template>
  <div class="pay-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="page-header">
          <h2>订单支付</h2>
          <div class="steps">
            <div class="step completed">
              <div class="step-icon">1</div>
              <span>选择商品</span>
            </div>
            <div class="step-line active"></div>
            <div class="step completed">
              <div class="step-icon">2</div>
              <span>确认订单</span>
            </div>
            <div class="step-line active"></div>
            <div class="step active">
              <div class="step-icon">3</div>
              <span>完成支付</span>
            </div>
          </div>
        </div>

        <div v-if="order.id" class="pay-content">
          <!-- 支付倒计时 -->
          <div class="countdown-card">
            <el-icon :size="24"><Timer /></el-icon>
            <span class="countdown-text">请在规定时间内完成支付</span>
            <span class="countdown-time">{{ formatTime(countdown) }}</span>
          </div>

          <!-- 订单信息 -->
          <div class="order-info-card">
            <div class="info-header">
              <h3>订单信息</h3>
              <span class="order-no">订单号：{{ order.orderNo }}</span>
            </div>
            <div class="info-body">
              <div class="info-row">
                <span class="label">商品金额</span>
                <span class="value">¥{{ order.totalAmount }}</span>
              </div>
              <div class="info-row">
                <span class="label">运费</span>
                <span class="value free">免运费</span>
              </div>
              <div class="info-row total">
                <span class="label">应付金额</span>
                <span class="value price">
                  <span class="currency">¥</span>
                  <span class="amount">{{ order.totalAmount }}</span>
                </span>
              </div>
            </div>
          </div>

          <!-- 支付方式 -->
          <div class="pay-method-card">
            <h3>选择支付方式</h3>
            <div class="method-list">
              <div
                v-for="method in payMethods"
                :key="method.id"
                :class="['method-item', { active: selectedMethod === method.id }]"
                @click="selectedMethod = method.id"
              >
                <div class="method-radio">
                  <div class="radio-dot" :class="{ checked: selectedMethod === method.id }"></div>
                </div>
                <div class="method-icon" :style="{ background: method.color }">
                  <el-icon :size="24"><component :is="method.icon" /></el-icon>
                </div>
                <div class="method-info">
                  <span class="method-name">{{ method.name }}</span>
                  <span class="method-desc">{{ method.desc }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 支付按钮 -->
          <div class="pay-action">
            <div class="pay-total">
              <span class="label">需支付</span>
              <span class="price">
                <span class="currency">¥</span>
                <span class="amount">{{ order.totalAmount }}</span>
              </span>
            </div>
            <button
              class="btn-pay"
              :disabled="paying"
              @click="handlePay"
            >
              <el-icon v-if="paying"><Loading /></el-icon>
              <span>{{ paying ? '支付中...' : '立即支付' }}</span>
            </button>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-else class="loading-state">
          <el-icon class="loading-icon" :size="40"><Loading /></el-icon>
          <p>加载订单信息中...</p>
        </div>
      </div>
    </main>
    <AppFooter />

    <!-- 支付成功弹窗 -->
    <Transition name="fade">
      <div v-if="showSuccess" class="success-overlay">
        <div class="success-dialog">
          <div class="success-icon">
            <el-icon :size="64"><CircleCheck /></el-icon>
          </div>
          <h2>支付成功</h2>
          <p class="success-amount">¥{{ order.totalAmount }}</p>
          <p class="success-tip">您的订单已支付成功，我们将尽快为您发货</p>
          <div class="success-actions">
            <router-link to="/order" class="btn-view-order">
              <el-icon><List /></el-icon>
              查看订单
            </router-link>
            <router-link to="/product" class="btn-continue">
              <el-icon><ShoppingBag /></el-icon>
              继续购物
            </router-link>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import { getOrderDetail, payOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()

const order = ref({})
const selectedMethod = ref('alipay')
const paying = ref(false)
const showSuccess = ref(false)
const countdown = ref(900) // 15分钟倒计时
let timer = null

const payMethods = [
  {
    id: 'alipay',
    name: '支付宝',
    desc: '推荐使用支付宝支付',
    icon: 'Wallet',
    color: '#1677ff'
  },
  {
    id: 'wechat',
    name: '微信支付',
    desc: '使用微信扫码支付',
    icon: 'ChatDotRound',
    color: '#07c160'
  },
  {
    id: 'balance',
    name: '余额支付',
    desc: '账户余额：¥10,000.00',
    icon: 'Coin',
    color: '#ff9500'
  }
]

const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

const startCountdown = () => {
  timer = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--
    } else {
      clearInterval(timer)
      ElMessage.warning('支付超时，订单已自动取消')
      router.push('/order')
    }
  }, 1000)
}

const loadOrder = async () => {
  try {
    const res = await getOrderDetail(route.params.id)
    if (res.code === 200) {
      order.value = res.data
      // 如果订单不是待支付状态，跳转到订单列表
      if (order.value.status !== 0) {
        ElMessage.warning('该订单不在待支付状态')
        router.push('/order')
      }
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('获取订单信息失败')
  }
}

const handlePay = async () => {
  paying.value = true
  try {
    // 模拟支付延迟
    await new Promise(resolve => setTimeout(resolve, 1500))

    const res = await payOrder(order.value.id, selectedMethod.value)
    if (res.code === 200) {
      clearInterval(timer)
      showSuccess.value = true
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('支付失败，请重试')
  } finally {
    paying.value = false
  }
}

onMounted(() => {
  loadOrder()
  startCountdown()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 24px 0;
}

/* 页面头部 */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

/* 步骤条 */
.steps {
  display: flex;
  align-items: center;
  gap: 16px;
}

.step {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #999;
}

.step.active {
  color: var(--primary-color);
}

.step.completed {
  color: #52c41a;
}

.step-icon {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
}

.step.active .step-icon {
  background: #333;
  color: #fff;
}

.step.completed .step-icon {
  background: #52c41a;
  color: #fff;
}

.step-line {
  width: 40px;
  height: 2px;
  background: #e0e0e0;
}

.step-line.active {
  background: #52c41a;
}

/* 支付倒计时 */
.countdown-card {
  background: #fff9f0;
  border: 1px solid #ffe0b2;
  border-radius: 12px;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.countdown-card .el-icon {
  color: #ff9500;
}

.countdown-text {
  font-size: 14px;
  color: #666;
}

.countdown-time {
  font-size: 20px;
  font-weight: 600;
  color: #ff4757;
  margin-left: auto;
  font-variant-numeric: tabular-nums;
}

/* 订单信息卡片 */
.order-info-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.info-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.info-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.order-no {
  font-size: 13px;
  color: #999;
}

.info-body {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info-row .label {
  font-size: 14px;
  color: #666;
}

.info-row .value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.info-row .value.free {
  color: #52c41a;
}

.info-row.total {
  padding-top: 16px;
  border-top: 1px dashed #e0e0e0;
  margin-top: 4px;
}

.info-row.total .label {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.info-row.total .price {
  color: #ff4757;
}

.info-row.total .price .currency {
  font-size: 16px;
}

.info-row.total .price .amount {
  font-size: 28px;
  font-weight: 700;
}

/* 支付方式卡片 */
.pay-method-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.pay-method-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.method-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.method-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.method-item:hover {
  border-color: #e0e0e0;
}

.method-item.active {
  border-color: #333;
  background: #f8f8f8;
}

.method-radio {
  padding: 4px;
}

.radio-dot {
  width: 20px;
  height: 20px;
  border: 2px solid #ddd;
  border-radius: 50%;
  transition: all 0.2s;
}

.radio-dot.checked {
  border-color: #333;
  background: #333;
  box-shadow: inset 0 0 0 4px #fff;
}

.method-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.method-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.method-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.method-desc {
  font-size: 13px;
  color: #999;
}

/* 支付操作栏 */
.pay-action {
  background: #fff;
  border-radius: 16px;
  padding: 24px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  position: sticky;
  bottom: 20px;
  z-index: 10;
}

.pay-total {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.pay-total .label {
  font-size: 14px;
  color: #666;
}

.pay-total .price {
  color: #ff4757;
}

.pay-total .price .currency {
  font-size: 16px;
}

.pay-total .price .amount {
  font-size: 32px;
  font-weight: 700;
}

.btn-pay {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 48px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 16px rgba(255, 71, 87, 0.3);
}

.btn-pay:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(255, 71, 87, 0.4);
}

.btn-pay:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* 加载状态 */
.loading-state {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.loading-icon {
  color: #333;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.loading-state p {
  margin-top: 16px;
  font-size: 14px;
  color: #999;
}

/* 支付成功弹窗 */
.success-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(8px);
}

.success-dialog {
  background: #fff;
  border-radius: 24px;
  padding: 48px;
  text-align: center;
  max-width: 420px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.success-icon {
  color: #52c41a;
  margin-bottom: 24px;
}

.success-dialog h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.success-amount {
  font-size: 36px;
  font-weight: 700;
  color: #ff4757;
  margin-bottom: 16px;
}

.success-tip {
  font-size: 14px;
  color: #666;
  margin-bottom: 32px;
}

.success-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.btn-view-order,
.btn-continue {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s;
}

.btn-view-order {
  background: #f5f5f5;
  color: #333;
}

.btn-view-order:hover {
  background: #eee;
}

.btn-continue {
  background: #333;
  color: #fff;
}

.btn-continue:hover {
  background: #444;
  transform: translateY(-2px);
}

/* 动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
