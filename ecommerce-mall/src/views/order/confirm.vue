<template>
  <div class="confirm-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="page-header">
          <h2>确认订单</h2>
          <div class="steps">
            <div class="step completed">
              <div class="step-icon">1</div>
              <span>选择商品</span>
            </div>
            <div class="step-line active"></div>
            <div class="step active">
              <div class="step-icon">2</div>
              <span>确认订单</span>
            </div>
            <div class="step-line"></div>
            <div class="step">
              <div class="step-icon">3</div>
              <span>完成支付</span>
            </div>
          </div>
        </div>

        <!-- 收货地址 -->
        <div class="section-card">
          <div class="section-header">
            <h3>
              <el-icon><Location /></el-icon>
              收货地址
            </h3>
            <router-link to="/user/address" class="manage-link">管理地址</router-link>
          </div>
          <div v-if="addresses.length" class="address-list">
            <div
              v-for="addr in addresses"
              :key="addr.id"
              :class="['address-item', { active: selectedAddress?.id === addr.id }]"
              @click="selectedAddress = addr"
            >
              <div class="address-radio">
                <div class="radio-dot" :class="{ checked: selectedAddress?.id === addr.id }"></div>
              </div>
              <div class="address-content">
                <div class="address-top">
                  <span class="name">{{ addr.receiverName }}</span>
                  <span class="phone">{{ addr.phone }}</span>
                  <el-tag v-if="addr.isDefault === 1" size="small" type="primary" effect="plain">默认</el-tag>
                </div>
                <div class="address-detail">
                  {{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}
                </div>
              </div>
            </div>
          </div>
          <div v-else class="no-address">
            <el-icon :size="32"><Location /></el-icon>
            <p>暂无收货地址</p>
            <router-link to="/user/address" class="add-link">去添加地址</router-link>
          </div>
        </div>

        <!-- 商品清单 -->
        <div class="section-card">
          <div class="section-header">
            <h3>
              <el-icon><ShoppingCart /></el-icon>
              商品清单
            </h3>
            <span class="item-count">共 {{ cartStore.checkedList.length }} 件商品</span>
          </div>
          <div class="goods-list">
            <div v-for="item in cartStore.checkedList" :key="item.id" class="goods-item">
              <div class="goods-image">
                <img :src="item.productImage || '/placeholder.png'" :alt="item.productName" />
              </div>
              <div class="goods-info">
                <h4 class="goods-name">{{ item.productName }}</h4>
                <div class="goods-price">
                  <span class="unit-price">¥{{ item.productPrice }}</span>
                  <span class="quantity">× {{ item.quantity }}</span>
                </div>
              </div>
              <div class="goods-subtotal">
                <span class="price">¥{{ (item.productPrice * item.quantity).toFixed(2) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 订单备注 -->
        <div class="section-card">
          <div class="section-header">
            <h3>
              <el-icon><EditPen /></el-icon>
              订单备注
            </h3>
            <span class="optional">选填</span>
          </div>
          <textarea
            v-model="remark"
            class="remark-input"
            placeholder="请填写订单备注信息（选填）"
            maxlength="200"
            rows="3"
          ></textarea>
          <div class="remark-count">{{ remark.length }}/200</div>
        </div>

        <!-- 结算信息 -->
        <div class="section-card settle-card">
          <div class="settle-info">
            <div class="settle-row">
              <span class="settle-label">商品数量</span>
              <span class="settle-value">{{ cartStore.checkedCount }} 件</span>
            </div>
            <div class="settle-row">
              <span class="settle-label">商品金额</span>
              <span class="settle-value">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
            </div>
            <div class="settle-row">
              <span class="settle-label">运费</span>
              <span class="settle-value free">免运费</span>
            </div>
          </div>
          <div class="settle-footer">
            <div class="total-section">
              <span class="total-label">应付总额</span>
              <span class="total-price">
                <span class="currency">¥</span>
                <span class="amount">{{ cartStore.totalPrice.toFixed(2) }}</span>
              </span>
            </div>
            <button
              class="btn-submit"
              :disabled="!selectedAddress || submitting"
              @click="handleSubmit"
            >
              <el-icon v-if="submitting"><Loading /></el-icon>
              <span>{{ submitting ? '提交中...' : '提交订单' }}</span>
            </button>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { getAddressList } from '@/api/address'
import { createOrder } from '@/api/order'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const addresses = ref([])
const selectedAddress = ref(null)
const remark = ref('')
const submitting = ref(false)

const loadAddresses = async () => {
  try {
    const res = await getAddressList(userStore.userInfo?.id)
    if (res.code === 200) {
      addresses.value = res.data || []
      const defaultAddr = addresses.value.find(a => a.isDefault === 1)
      if (defaultAddr) {
        selectedAddress.value = defaultAddr
      } else if (addresses.value.length > 0) {
        selectedAddress.value = addresses.value[0]
      }
    }
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  if (cartStore.checkedList.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }

  submitting.value = true
  try {
    const orderData = {
      addressId: selectedAddress.value.id,
      items: cartStore.checkedList.map(item => ({
        productId: item.productId,
        quantity: item.quantity
      })),
      remark: remark.value
    }
    const res = await createOrder(orderData)
    if (res.code === 200) {
      ElMessage.success('下单成功，请完成支付')
      cartStore.fetchCartList()
      // 跳转到支付页面
      router.push(`/order/pay/${res.data.orderId}`)
    }
  } catch (error) {
    console.error(error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadAddresses()
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
  background: #333;
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
  display: flex;
  align-items: center;
  justify-content: space-between;
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

.manage-link {
  font-size: 13px;
  color: #333;
  text-decoration: none;
}

.manage-link:hover {
  text-decoration: underline;
}

.item-count {
  font-size: 13px;
  color: #999;
}

.optional {
  font-size: 12px;
  color: #ccc;
}

/* 地址列表 */
.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.address-item:hover {
  border-color: #e0e0e0;
}

.address-item.active {
  border-color: #333;
  background: #f8f8f8;
}

.address-radio {
  padding-top: 2px;
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

.address-content {
  flex: 1;
}

.address-top {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.phone {
  font-size: 14px;
  color: #666;
}

.address-detail {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.no-address {
  text-align: center;
  padding: 32px;
  color: #999;
}

.no-address p {
  margin: 12px 0;
}

.add-link {
  color: #333;
  text-decoration: none;
  font-size: 14px;
}

/* 商品列表 */
.goods-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.goods-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
}

.goods-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  background: #f0f0f0;
  flex-shrink: 0;
}

.goods-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.goods-info {
  flex: 1;
}

.goods-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.goods-price {
  display: flex;
  align-items: center;
  gap: 8px;
}

.unit-price {
  font-size: 14px;
  color: #666;
}

.quantity {
  font-size: 13px;
  color: #999;
}

.goods-subtotal .price {
  font-size: 16px;
  font-weight: 600;
  color: #ff4757;
}

/* 备注 */
.remark-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  outline: none;
  transition: all 0.2s;
  font-family: inherit;
  box-sizing: border-box;
}

.remark-input:focus {
  border-color: #333;
}

.remark-input::placeholder {
  color: #ccc;
}

.remark-count {
  text-align: right;
  font-size: 12px;
  color: #ccc;
  margin-top: 8px;
}

/* 结算栏 */
.settle-card {
  position: sticky;
  bottom: 20px;
  z-index: 10;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.settle-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.settle-row {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  font-size: 14px;
}

.settle-label {
  color: #999;
}

.settle-value {
  color: #333;
  min-width: 100px;
  text-align: right;
}

.settle-value.free {
  color: #52c41a;
}

.settle-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 32px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.total-section {
  text-align: right;
}

.total-label {
  display: block;
  font-size: 13px;
  color: #999;
  margin-bottom: 4px;
}

.total-price {
  color: #ff4757;
}

.total-price .currency {
  font-size: 16px;
}

.total-price .amount {
  font-size: 28px;
  font-weight: 600;
}

.btn-submit {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 40px;
  background: #333;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-submit:hover:not(:disabled) {
  background: #444;
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
</style>
