<template>
  <div class="product-detail-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <!-- 面包屑 -->
        <div class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span>/</span>
          <router-link to="/product">商品列表</router-link>
          <span>/</span>
          <span>{{ product.name }}</span>
        </div>

        <!-- 商品信息 -->
        <div class="product-info-card" v-if="product.id">
          <div class="product-gallery">
            <div class="main-image">
              <img :src="product.mainImage || '/placeholder.png'" :alt="product.name" />
            </div>
          </div>
          <div class="product-detail">
            <h1 class="product-title">{{ product.name }}</h1>
            <div class="product-price-area">
              <div class="price-row">
                <span class="label">价格</span>
                <span class="current-price">¥{{ product.price }}</span>
                <span v-if="product.originalPrice" class="original-price">¥{{ product.originalPrice }}</span>
              </div>
            </div>
            <div class="product-meta">
              <div class="meta-item">
                <span class="label">销量</span>
                <span>{{ product.sales || 0 }}</span>
              </div>
              <div class="meta-item">
                <span class="label">库存</span>
                <span>{{ product.stock }}</span>
              </div>
            </div>
            <div class="product-desc" v-if="product.description">
              <span class="label">描述</span>
              <span>{{ product.description }}</span>
            </div>
            <div class="quantity-selector">
              <span class="label">数量</span>
              <div class="quantity-control">
                <button @click="quantity > 1 && quantity--" :disabled="quantity <= 1">-</button>
                <input v-model.number="quantity" type="number" min="1" :max="product.stock" />
                <button @click="quantity < product.stock && quantity++" :disabled="quantity >= product.stock">+</button>
              </div>
            </div>
            <div class="action-buttons">
              <button class="btn-add-cart" @click="handleAddCart">
                <el-icon><ShoppingCart /></el-icon>
                加入购物车
              </button>
              <button class="btn-buy-now" @click="handleBuyNow">
                立即购买
              </button>
            </div>
          </div>
        </div>

        <!-- 商品详情/评价 -->
        <div class="product-tabs">
          <div class="tab-header">
            <a :class="{ active: activeTab === 'detail' }" @click="activeTab = 'detail'">商品详情</a>
            <a :class="{ active: activeTab === 'review' }" @click="activeTab = 'review'">用户评价 ({{ reviews.length }})</a>
          </div>
          <div class="tab-content">
            <!-- 商品详情 -->
            <div v-if="activeTab === 'detail'" class="detail-content">
              <p v-if="product.description">{{ product.description }}</p>
              <p v-else class="no-content">暂无详细描述</p>
            </div>
            <!-- 评价列表 -->
            <div v-if="activeTab === 'review'" class="review-content">
              <div v-if="reviews.length" class="review-list">
                <div v-for="review in reviews" :key="review.id" class="review-item">
                  <div class="review-header">
                    <span class="review-user">{{ review.username || '匿名用户' }}</span>
                    <div class="review-rating">
                      <span v-for="i in 5" :key="i" :class="{ filled: i <= review.rating }">★</span>
                    </div>
                    <span class="review-time">{{ review.createTime }}</span>
                  </div>
                  <p class="review-text">{{ review.content }}</p>
                  <div v-if="review.reply" class="review-reply">
                    <span class="reply-label">商家回复：</span>{{ review.reply }}
                  </div>
                </div>
              </div>
              <div v-else class="empty-state">
                <p>暂无评价</p>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import { getProductDetail } from '@/api/product'
import { getProductReviews } from '@/api/review'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const product = ref({})
const reviews = ref([])
const quantity = ref(1)
const activeTab = ref('detail')

// 加载商品详情
const loadProduct = async () => {
  try {
    const res = await getProductDetail(route.params.id)
    if (res.code === 200) {
      product.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

// 加载评价
const loadReviews = async () => {
  try {
    const res = await getProductReviews({
      productId: route.params.id,
      pageNum: 1,
      pageSize: 50
    })
    if (res.code === 200) {
      reviews.value = res.data.records || []
    }
  } catch (error) {
    console.error(error)
  }
}

// 加入购物车
const handleAddCart = async () => {
  if (!userStore.token) {
    router.push('/user/login')
    return
  }
  const success = await cartStore.addItem(product.value.id, quantity.value)
  if (success) {
    ElMessage.success('已加入购物车')
  }
}

// 立即购买
const handleBuyNow = () => {
  if (!userStore.token) {
    router.push('/user/login')
    return
  }
  // 先加入购物车再跳转到确认订单
  cartStore.addItem(product.value.id, quantity.value).then(() => {
    router.push('/order/confirm')
  })
}

onMounted(() => {
  loadProduct()
  loadReviews()
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 20px 0;
}

.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
  color: var(--text-light);
}

.breadcrumb a {
  color: var(--text-secondary);
  text-decoration: none;
}

.breadcrumb a:hover {
  color: var(--primary-color);
}

.breadcrumb span {
  margin: 0 8px;
}

/* 商品信息卡片 */
.product-info-card {
  display: flex;
  gap: 40px;
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  margin-bottom: 30px;
}

.product-gallery {
  width: 400px;
  flex-shrink: 0;
}

.main-image {
  width: 400px;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  background: #f8f8f8;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-detail {
  flex: 1;
}

.product-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 20px;
  line-height: 1.4;
}

.product-price-area {
  background: #fafafa;
  padding: 16px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.current-price {
  font-size: 28px;
  color: var(--danger-color);
  font-weight: 600;
}

.original-price {
  font-size: 16px;
  color: var(--text-light);
  text-decoration: line-through;
}

.product-meta {
  display: flex;
  gap: 40px;
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  gap: 8px;
  font-size: 14px;
}

.label {
  color: var(--text-light);
  min-width: 50px;
}

.product-desc {
  margin-bottom: 20px;
  font-size: 14px;
  line-height: 1.6;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 30px;
}

.quantity-control {
  display: flex;
  border: 1px solid var(--border-color);
  border-radius: 4px;
}

.quantity-control button {
  width: 36px;
  height: 36px;
  border: none;
  background: #f8f8f8;
  cursor: pointer;
  font-size: 16px;
}

.quantity-control button:hover:not(:disabled) {
  background: #eee;
}

.quantity-control input {
  width: 60px;
  height: 36px;
  border: none;
  border-left: 1px solid var(--border-color);
  border-right: 1px solid var(--border-color);
  text-align: center;
  font-size: 14px;
  outline: none;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.btn-add-cart,
.btn-buy-now {
  padding: 14px 40px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.btn-add-cart {
  background: #ff9800;
  color: #fff;
}

.btn-add-cart:hover {
  background: #f57c00;
}

.btn-buy-now {
  background: var(--danger-color);
  color: #fff;
}

.btn-buy-now:hover {
  background: #e03e3e;
}

/* 标签页 */
.product-tabs {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.tab-header {
  display: flex;
  border-bottom: 1px solid var(--border-color);
}

.tab-header a {
  padding: 16px 32px;
  cursor: pointer;
  font-size: 15px;
  color: var(--text-secondary);
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.tab-header a:hover {
  color: var(--primary-color);
}

.tab-header a.active {
  color: var(--primary-color);
  border-bottom-color: var(--primary-color);
}

.tab-content {
  padding: 24px;
}

.detail-content {
  line-height: 1.8;
  color: var(--text-secondary);
}

.no-content {
  text-align: center;
  color: var(--text-light);
  padding: 40px;
}

/* 评价 */
.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  padding: 16px;
  background: #fafafa;
  border-radius: 4px;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.review-user {
  font-weight: 600;
  color: var(--text-color);
}

.review-rating {
  color: #ddd;
}

.review-rating .filled {
  color: #ff9800;
}

.review-time {
  font-size: 12px;
  color: var(--text-light);
}

.review-text {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
}

.review-reply {
  margin-top: 12px;
  padding: 12px;
  background: #fff;
  border-radius: 4px;
  font-size: 13px;
  color: var(--text-secondary);
}

.reply-label {
  color: var(--primary-color);
  font-weight: 600;
}
</style>
