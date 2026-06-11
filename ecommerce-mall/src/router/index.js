import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/product',
    component: () => import('@/views/product/list.vue'),
    meta: { title: '商品列表' }
  },
  {
    path: '/product/:id',
    component: () => import('@/views/product/detail.vue'),
    meta: { title: '商品详情' }
  },
  {
    path: '/cart',
    component: () => import('@/views/cart/index.vue'),
    meta: { title: '购物车', requireAuth: true }
  },
  {
    path: '/order/confirm',
    component: () => import('@/views/order/confirm.vue'),
    meta: { title: '确认订单', requireAuth: true }
  },
  {
    path: '/order',
    component: () => import('@/views/order/list.vue'),
    meta: { title: '我的订单', requireAuth: true }
  },
  {
    path: '/order/pay/:id',
    component: () => import('@/views/order/pay.vue'),
    meta: { title: '订单支付', requireAuth: true }
  },
  {
    path: '/order/:id',
    component: () => import('@/views/order/detail.vue'),
    meta: { title: '订单详情', requireAuth: true }
  },
  {
    path: '/user/login',
    component: () => import('@/views/user/login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/user/register',
    component: () => import('@/views/user/register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/user',
    component: () => import('@/views/user/center.vue'),
    meta: { title: '个人中心', requireAuth: true }
  },
  {
    path: '/user/info',
    component: () => import('@/views/user/info.vue'),
    meta: { title: '个人资料', requireAuth: true }
  },
  {
    path: '/user/address',
    component: () => import('@/views/user/address.vue'),
    meta: { title: '收货地址', requireAuth: true }
  },
  {
    path: '/search',
    component: () => import('@/views/search/index.vue'),
    meta: { title: '搜索结果' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 品质商城` : '品质商城'

  const userStore = useUserStore()
  const token = userStore.token

  // 需要登录的页面
  if (to.meta.requireAuth && !token) {
    next({ path: '/user/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
