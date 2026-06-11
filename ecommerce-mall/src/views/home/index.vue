<template>
  <div class="home">
    <AppHeader />
    <main class="main">
      <!-- 轮播图 -->
      <section class="banner-section">
        <div class="container">
          <div class="banner-wrapper">
            <div class="banner-main">
              <div class="banner-slide" v-for="(banner, index) in banners" :key="banner.id" v-show="currentBanner === index">
                <img :src="banner.image" :alt="banner.title" />
                <div class="banner-overlay">
                  <h2>{{ banner.title }}</h2>
                </div>
              </div>
              <div class="banner-dots">
                <span
                  v-for="(banner, index) in banners"
                  :key="index"
                  :class="{ active: currentBanner === index }"
                  @click="currentBanner = index"
                ></span>
              </div>
            </div>
            <!-- 侧边分类 -->
            <div class="category-sidebar">
              <h3>商品分类</h3>
              <ul class="main-categories">
                <li v-for="cat in categories" :key="cat.id" class="category-item">
                  <router-link :to="{ path: '/product', query: { categoryId: cat.id } }" class="category-link">
                    <span class="category-name">{{ cat.name }}</span>
                    <el-icon v-if="cat.children && cat.children.length"><ArrowRight /></el-icon>
                  </router-link>
                  <!-- 子分类 -->
                  <ul v-if="cat.children && cat.children.length" class="sub-categories">
                    <li v-for="sub in cat.children" :key="sub.id">
                      <router-link :to="{ path: '/product', query: { categoryId: sub.id } }">
                        {{ sub.name }}
                      </router-link>
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>

      <!-- 热销商品 -->
      <section class="section">
        <div class="container">
          <div class="section-header">
            <h2>热销商品</h2>
            <router-link to="/product" class="view-all">查看全部 →</router-link>
          </div>
          <div class="product-grid">
            <ProductCard v-for="product in hotProducts" :key="product.id" :product="product" />
          </div>
        </div>
      </section>

      <!-- 新品上架 -->
      <section class="section">
        <div class="container">
          <div class="section-header">
            <h2>新品上架</h2>
            <router-link to="/product" class="view-all">查看全部 →</router-link>
          </div>
          <div class="product-grid">
            <ProductCard v-for="product in newProducts" :key="product.id" :product="product" />
          </div>
        </div>
      </section>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import ProductCard from '@/components/ProductCard.vue'
import { getBannerList } from '@/api/banner'
import { getCategoryTree } from '@/api/product'
import { getProductList } from '@/api/product'

const banners = ref([])
const categories = ref([])
const hotProducts = ref([])
const newProducts = ref([])
const currentBanner = ref(0)
let bannerTimer = null

// 加载轮播图
const loadBanners = async () => {
  try {
    const res = await getBannerList()
    if (res.code === 200) {
      banners.value = res.data.records || []
    }
  } catch (error) {
    console.error('加载轮播图失败', error)
  }
}

// 加载分类
const loadCategories = async () => {
  try {
    const res = await getCategoryTree()
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

// 加载热销商品
const loadHotProducts = async () => {
  try {
    const res = await getProductList({ pageNum: 1, pageSize: 8 })
    if (res.code === 200) {
      hotProducts.value = res.data.records || []
    }
  } catch (error) {
    console.error('加载热销商品失败', error)
  }
}

// 加载新品
const loadNewProducts = async () => {
  try {
    const res = await getProductList({ pageNum: 1, pageSize: 4 })
    if (res.code === 200) {
      newProducts.value = res.data.records || []
    }
  } catch (error) {
    console.error('加载新品失败', error)
  }
}

// 轮播图自动播放
const startBannerTimer = () => {
  bannerTimer = setInterval(() => {
    if (banners.value.length > 0) {
      currentBanner.value = (currentBanner.value + 1) % banners.value.length
    }
  }, 4000)
}

onMounted(() => {
  loadBanners()
  loadCategories()
  loadHotProducts()
  loadNewProducts()
  startBannerTimer()
})

onUnmounted(() => {
  if (bannerTimer) {
    clearInterval(bannerTimer)
  }
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
}

/* 轮播图区域 */
.banner-section {
  margin-bottom: 40px;
}

.banner-wrapper {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.banner-main {
  flex: 1;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  height: 400px;
}

.banner-slide {
  width: 100%;
  height: 100%;
}

.banner-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.5));
  color: #fff;
}

.banner-overlay h2 {
  font-size: 24px;
  font-weight: 600;
}

.banner-dots {
  position: absolute;
  bottom: 16px;
  right: 16px;
  display: flex;
  gap: 8px;
}

.banner-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.banner-dots span.active {
  background: #fff;
  width: 20px;
  border-radius: 4px;
}

/* 侧边分类 */
.category-sidebar {
  width: 240px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  flex-shrink: 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.category-sidebar h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.main-categories {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  margin-bottom: 4px;
}

.category-link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  color: #333;
  text-decoration: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
}

.category-link:hover {
  background: #f0f5ff;
  color: var(--primary-color);
}

.category-link .el-icon {
  font-size: 12px;
  color: #ccc;
  transition: transform 0.2s;
}

.category-link:hover .el-icon {
  color: var(--primary-color);
  transform: translateX(2px);
}

.sub-categories {
  list-style: none;
  padding: 0 0 0 16px;
  margin: 0;
}

.sub-categories li {
  margin-bottom: 2px;
}

.sub-categories a {
  display: block;
  padding: 6px 12px;
  color: #666;
  text-decoration: none;
  border-radius: 6px;
  font-size: 13px;
  transition: all 0.2s;
}

.sub-categories a:hover {
  background: #f0f5ff;
  color: var(--primary-color);
}

/* 区块通用 */
.section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color);
}

.view-all {
  color: var(--text-light);
  text-decoration: none;
  font-size: 14px;
}

.view-all:hover {
  color: var(--primary-color);
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
</style>
