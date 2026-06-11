<template>
  <div class="search-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="search-header">
          <h2>搜索结果：<span class="keyword">{{ keyword }}</span></h2>
          <span class="result-count">共找到 {{ total }} 件商品</span>
        </div>

        <div v-if="products.length" class="product-grid">
          <ProductCard v-for="product in products" :key="product.id" :product="product" />
        </div>
        <div v-else class="empty-state">
          <el-icon :size="64" color="#ddd"><Search /></el-icon>
          <p>未找到相关商品</p>
          <router-link to="/" class="link">返回首页</router-link>
        </div>

        <!-- 分页 -->
        <div v-if="total > pageSize" class="pagination">
          <button :disabled="pageNum <= 1" @click="changePage(pageNum - 1)">上一页</button>
          <span>{{ pageNum }} / {{ Math.ceil(total / pageSize) }}</span>
          <button :disabled="pageNum >= Math.ceil(total / pageSize)" @click="changePage(pageNum + 1)">下一页</button>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import ProductCard from '@/components/ProductCard.vue'
import { getProductList } from '@/api/product'

const route = useRoute()

const keyword = ref('')
const products = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)

const loadProducts = async () => {
  try {
    const res = await getProductList({
      keyword: keyword.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    if (res.code === 200) {
      products.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  }
}

const changePage = (page) => {
  pageNum.value = page
  loadProducts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watch(() => route.query.keyword, (newKeyword) => {
  if (newKeyword) {
    keyword.value = newKeyword
    pageNum.value = 1
    loadProducts()
  }
})

onMounted(() => {
  keyword.value = route.query.keyword || ''
  loadProducts()
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 20px 0;
}

.search-header {
  display: flex;
  align-items: baseline;
  gap: 16px;
  margin-bottom: 24px;
}

.search-header h2 {
  font-size: 20px;
}

.keyword {
  color: var(--primary-color);
}

.result-count {
  font-size: 14px;
  color: var(--text-light);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 8px;
}

.empty-state p {
  color: var(--text-light);
  margin: 20px 0;
}

.link {
  color: var(--primary-color);
  text-decoration: none;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 30px 0;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
