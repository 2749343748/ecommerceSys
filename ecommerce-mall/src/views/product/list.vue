<template>
  <div class="product-list-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <!-- 面包屑 -->
        <div class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span>/</span>
          <span>{{ currentCategoryName || '全部商品' }}</span>
        </div>

        <div class="content-wrapper">
          <!-- 左侧分类 -->
          <aside class="category-aside">
            <h3>商品分类</h3>
            <ul class="category-list">
              <li>
                <a :class="{ active: !selectedCategoryId }" @click="selectCategory(null)">全部商品</a>
              </li>
              <li v-for="cat in categories" :key="cat.id">
                <a :class="{ active: selectedCategoryId === cat.id }" @click="selectCategory(cat.id)">
                  {{ cat.name }}
                </a>
                <ul v-if="cat.children && cat.children.length" class="sub-category">
                  <li v-for="sub in cat.children" :key="sub.id">
                    <a :class="{ active: selectedCategoryId === sub.id }" @click="selectCategory(sub.id)">
                      {{ sub.name }}
                    </a>
                  </li>
                </ul>
              </li>
            </ul>
          </aside>

          <!-- 右侧商品列表 -->
          <div class="product-content">
            <!-- 排序栏 -->
            <div class="sort-bar">
              <div class="sort-options">
                <a :class="{ active: sortBy === 'default' }" @click="changeSort('default')">综合排序</a>
                <a :class="{ active: sortBy === 'price_asc' }" @click="changeSort('price_asc')">价格 ↑</a>
                <a :class="{ active: sortBy === 'price_desc' }" @click="changeSort('price_desc')">价格 ↓</a>
                <a :class="{ active: sortBy === 'sales' }" @click="changeSort('sales')">销量</a>
              </div>
              <div class="result-count">
                共 <span>{{ total }}</span> 件商品
              </div>
            </div>

            <!-- 商品网格 -->
            <div v-if="products.length" class="product-grid">
              <ProductCard v-for="product in products" :key="product.id" :product="product" />
            </div>
            <div v-else class="empty-state">
              <p>暂无商品</p>
            </div>

            <!-- 分页 -->
            <div v-if="total > pageSize" class="pagination">
              <button :disabled="pageNum <= 1" @click="changePage(pageNum - 1)">上一页</button>
              <span>{{ pageNum }} / {{ totalPages }}</span>
              <button :disabled="pageNum >= totalPages" @click="changePage(pageNum + 1)">下一页</button>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import ProductCard from '@/components/ProductCard.vue'
import { getProductList, getCategoryTree } from '@/api/product'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const products = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)
const sortBy = ref('default')
const selectedCategoryId = ref(null)

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

const currentCategoryName = computed(() => {
  if (!selectedCategoryId.value) return ''
  const findName = (list) => {
    for (const cat of list) {
      if (cat.id === selectedCategoryId.value) return cat.name
      if (cat.children) {
        const found = findName(cat.children)
        if (found) return found
      }
    }
    return ''
  }
  return findName(categories.value)
})

// 加载分类
const loadCategories = async () => {
  try {
    const res = await getCategoryTree()
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

// 加载商品
const loadProducts = async () => {
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (selectedCategoryId.value) {
      params.categoryId = selectedCategoryId.value
    }
    if (route.query.keyword) {
      params.keyword = route.query.keyword
    }
    const res = await getProductList(params)
    if (res.code === 200) {
      products.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error(error)
  }
}

const selectCategory = (categoryId) => {
  selectedCategoryId.value = categoryId
  pageNum.value = 1
  loadProducts()
}

const changeSort = (sort) => {
  sortBy.value = sort
  pageNum.value = 1
  loadProducts()
}

const changePage = (page) => {
  pageNum.value = page
  loadProducts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watch(() => route.query, () => {
  if (route.query.categoryId) {
    selectedCategoryId.value = Number(route.query.categoryId)
  }
  pageNum.value = 1
  loadProducts()
})

onMounted(() => {
  if (route.query.categoryId) {
    selectedCategoryId.value = Number(route.query.categoryId)
  }
  loadCategories()
  loadProducts()
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

.content-wrapper {
  display: flex;
  gap: 20px;
}

/* 左侧分类 */
.category-aside {
  width: 200px;
  flex-shrink: 0;
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}

.category-aside h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.category-list > li {
  margin-bottom: 8px;
}

.category-list a {
  display: block;
  padding: 8px 12px;
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.category-list a:hover,
.category-list a.active {
  background: #f0f5ff;
  color: var(--primary-color);
}

.sub-category {
  margin-left: 16px;
}

.sub-category a {
  font-size: 13px;
  padding: 6px 12px;
}

/* 右侧内容 */
.product-content {
  flex: 1;
}

.sort-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.sort-options {
  display: flex;
  gap: 20px;
}

.sort-options a {
  color: var(--text-secondary);
  text-decoration: none;
  cursor: pointer;
  font-size: 14px;
}

.sort-options a:hover,
.sort-options a.active {
  color: var(--primary-color);
}

.result-count {
  font-size: 13px;
  color: var(--text-light);
}

.result-count span {
  color: var(--primary-color);
  font-weight: 600;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

/* 分页 */
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

.pagination span {
  font-size: 14px;
  color: var(--text-secondary);
}
</style>
