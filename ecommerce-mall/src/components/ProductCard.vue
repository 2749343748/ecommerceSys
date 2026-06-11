<template>
  <router-link :to="`/product/${product.id}`" class="product-card">
    <div class="product-image">
      <img :src="product.mainImage || '/placeholder.png'" :alt="product.name" />
    </div>
    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <div class="product-price">
        <span class="price">¥{{ product.price }}</span>
        <span v-if="product.originalPrice && product.originalPrice > product.price" class="price-original">
          ¥{{ product.originalPrice }}
        </span>
      </div>
      <div class="product-meta">
        <span v-if="product.sales" class="sales">已售 {{ product.sales }}</span>
      </div>
    </div>
  </router-link>
</template>

<script setup>
defineProps({
  product: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.product-card {
  display: block;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  text-decoration: none;
  color: inherit;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  aspect-ratio: 1;
  overflow: hidden;
  background: #f8f8f8;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 12px;
}

.product-name {
  font-size: 14px;
  font-weight: 400;
  color: var(--text-color);
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 8px;
  min-height: 40px;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 4px;
}

.product-price .price {
  font-size: 18px;
  color: var(--danger-color);
  font-weight: 600;
}

.product-meta {
  font-size: 12px;
  color: var(--text-light);
}
</style>
