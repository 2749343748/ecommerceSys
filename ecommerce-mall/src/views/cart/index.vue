<template>
  <div class="cart-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <h1 class="page-title">我的购物车</h1>

        <div v-if="cartStore.cartList.length" class="cart-content">
          <!-- 购物车表格 -->
          <div class="cart-table">
            <div class="cart-header">
              <label class="check-all">
                <input type="checkbox" :checked="cartStore.isAllChecked" @change="cartStore.toggleCheckAll($event.target.checked)" />
                全选
              </label>
              <span class="col-product">商品信息</span>
              <span class="col-price">单价</span>
              <span class="col-quantity">数量</span>
              <span class="col-total">小计</span>
              <span class="col-action">操作</span>
            </div>
            <div v-for="item in cartStore.cartList" :key="item.id" class="cart-item">
              <label class="item-check">
                <input type="checkbox" :checked="item.checked === 1" @change="cartStore.toggleChecked(item.id, $event.target.checked ? 1 : 0)" />
              </label>
              <div class="col-product item-info">
                <router-link :to="`/product/${item.productId}`" class="item-image">
                  <img :src="item.productImage || '/placeholder.png'" :alt="item.productName" />
                </router-link>
                <router-link :to="`/product/${item.productId}`" class="item-name">
                  {{ item.productName }}
                </router-link>
              </div>
              <div class="col-price item-price">
                <span class="price">¥{{ item.productPrice }}</span>
              </div>
              <div class="col-quantity item-quantity">
                <div class="quantity-control">
                  <button @click="handleQuantityChange(item, item.quantity - 1)" :disabled="item.quantity <= 1">-</button>
                  <input :value="item.quantity" @change="handleQuantityChange(item, $event.target.value)" type="number" min="1" />
                  <button @click="handleQuantityChange(item, item.quantity + 1)">+</button>
                </div>
              </div>
              <div class="col-total item-total">
                <span class="price">¥{{ (item.productPrice * item.quantity).toFixed(2) }}</span>
              </div>
              <div class="col-action item-action">
                <button class="btn-delete" @click="handleDelete(item.id)">删除</button>
              </div>
            </div>
          </div>

          <!-- 结算栏 -->
          <div class="cart-footer">
            <div class="footer-left">
              <label class="check-all">
                <input type="checkbox" :checked="cartStore.isAllChecked" @change="cartStore.toggleCheckAll($event.target.checked)" />
                全选
              </label>
              <button class="btn-delete-selected" @click="handleDeleteSelected">删除选中</button>
            </div>
            <div class="footer-right">
              <div class="total-info">
                <span>已选择 <strong>{{ cartStore.checkedCount }}</strong> 件商品</span>
                <span class="total-price">合计：<strong>¥{{ cartStore.totalPrice.toFixed(2) }}</strong></span>
              </div>
              <button class="btn-checkout" :disabled="cartStore.checkedCount === 0" @click="handleCheckout">
                去结算
              </button>
            </div>
          </div>
        </div>

        <div v-else class="empty-cart">
          <el-icon :size="64" color="#ddd"><ShoppingCart /></el-icon>
          <p>购物车还是空的</p>
          <router-link to="/product" class="btn-go-shopping">去逛逛</router-link>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const cartStore = useCartStore()

const handleQuantityChange = (item, newQuantity) => {
  const qty = parseInt(newQuantity)
  if (qty >= 1 && qty !== item.quantity) {
    cartStore.updateQuantity(item.id, qty)
  }
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定要删除该商品吗？', '提示', { type: 'warning' })
  cartStore.removeItem(id)
  ElMessage.success('已删除')
}

const handleDeleteSelected = async () => {
  const checkedItems = cartStore.checkedList
  if (checkedItems.length === 0) {
    ElMessage.warning('请先选择商品')
    return
  }
  await ElMessageBox.confirm(`确定要删除选中的 ${checkedItems.length} 件商品吗？`, '提示', { type: 'warning' })
  checkedItems.forEach(item => cartStore.removeItem(item.id))
  ElMessage.success('已删除')
}

const handleCheckout = () => {
  if (cartStore.checkedCount === 0) {
    ElMessage.warning('请先选择商品')
    return
  }
  router.push('/order/confirm')
}

onMounted(() => {
  cartStore.fetchCartList()
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 20px 0;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
}

/* 购物车表格 */
.cart-table {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.cart-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fafafa;
  border-bottom: 1px solid var(--border-color);
  font-size: 14px;
  color: var(--text-secondary);
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.cart-item:last-child {
  border-bottom: none;
}

.check-all,
.item-check {
  width: 50px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
}

.check-all input,
.item-check input {
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.col-product {
  flex: 1;
  min-width: 0;
}

.col-price {
  width: 120px;
  text-align: center;
}

.col-quantity {
  width: 140px;
  text-align: center;
}

.col-total {
  width: 120px;
  text-align: center;
}

.col-action {
  width: 80px;
  text-align: center;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
  background: #f8f8f8;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-name {
  font-size: 14px;
  color: var(--text-color);
  text-decoration: none;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-name:hover {
  color: var(--primary-color);
}

.item-price .price {
  font-size: 14px;
}

.item-total .price {
  font-size: 16px;
  font-weight: 600;
  color: var(--danger-color);
}

.quantity-control {
  display: inline-flex;
  border: 1px solid var(--border-color);
  border-radius: 4px;
}

.quantity-control button {
  width: 30px;
  height: 30px;
  border: none;
  background: #f8f8f8;
  cursor: pointer;
  font-size: 14px;
}

.quantity-control button:hover:not(:disabled) {
  background: #eee;
}

.quantity-control input {
  width: 50px;
  height: 30px;
  border: none;
  border-left: 1px solid var(--border-color);
  border-right: 1px solid var(--border-color);
  text-align: center;
  font-size: 13px;
  outline: none;
}

.btn-delete {
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  font-size: 13px;
}

.btn-delete:hover {
  color: var(--danger-color);
}

/* 结算栏 */
.cart-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 16px 20px;
  margin-top: 20px;
  border-radius: 8px;
  position: sticky;
  bottom: 0;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.06);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.btn-delete-selected {
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  font-size: 14px;
}

.btn-delete-selected:hover {
  color: var(--danger-color);
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.total-info {
  text-align: right;
  font-size: 14px;
  color: var(--text-secondary);
}

.total-info strong {
  color: var(--primary-color);
}

.total-price {
  margin-left: 20px;
}

.total-price strong {
  font-size: 22px;
  color: var(--danger-color);
}

.btn-checkout {
  padding: 12px 40px;
  background: var(--danger-color);
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-checkout:hover:not(:disabled) {
  background: #e03e3e;
}

.btn-checkout:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 空购物车 */
.empty-cart {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 8px;
}

.empty-cart p {
  color: var(--text-light);
  margin: 20px 0;
}

.btn-go-shopping {
  display: inline-block;
  padding: 10px 30px;
  background: var(--primary-color);
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
}

.btn-go-shopping:hover {
  background: var(--primary-dark);
}
</style>
