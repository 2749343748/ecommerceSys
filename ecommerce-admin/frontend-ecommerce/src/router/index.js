import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      // 首页
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      // 商品管理
      {
        path: 'product/category',
        name: 'ProductCategory',
        component: () => import('@/views/product/category/index.vue'),
        meta: { title: '商品分类', icon: 'Tree', parent: '商品管理' }
      },
      {
        path: 'product/brand',
        name: 'ProductBrand',
        component: () => import('@/views/product/brand/index.vue'),
        meta: { title: '品牌管理', icon: 'Stamp', parent: '商品管理' }
      },
      {
        path: 'product/list',
        name: 'ProductList',
        component: () => import('@/views/product/index.vue'),
        meta: { title: '商品列表', icon: 'List', parent: '商品管理' }
      },
      {
        path: 'product/spec',
        name: 'ProductSpec',
        component: () => import('@/views/product/spec/index.vue'),
        meta: { title: '规格管理', icon: 'Grid', parent: '商品管理' }
      },
      // 库存管理
      {
        path: 'inventory/list',
        name: 'InventoryList',
        component: () => import('@/views/inventory/list/index.vue'),
        meta: { title: '库存列表', icon: 'Box', parent: '库存管理' }
      },
      {
        path: 'inventory/record',
        name: 'InventoryRecord',
        component: () => import('@/views/inventory/record/index.vue'),
        meta: { title: '入库记录', icon: 'Document', parent: '库存管理' }
      },
      // 订单管理
      {
        path: 'order/list',
        name: 'OrderList',
        component: () => import('@/views/order/index.vue'),
        meta: { title: '订单列表', icon: 'List', parent: '订单管理' }
      },
      {
        path: 'order/logistics',
        name: 'OrderLogistics',
        component: () => import('@/views/order/logistics/index.vue'),
        meta: { title: '物流管理', icon: 'Van', parent: '订单管理' }
      },
      // 用户管理
      {
        path: 'user/list',
        name: 'UserList',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户列表', icon: 'List', parent: '用户管理' }
      },
      {
        path: 'user/role',
        name: 'UserRole',
        component: () => import('@/views/user/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', parent: '用户管理' }
      },
      {
        path: 'user/address',
        name: 'UserAddress',
        component: () => import('@/views/user/address/index.vue'),
        meta: { title: '收货地址', icon: 'Location', parent: '用户管理' }
      },
      // 营销管理
      {
        path: 'marketing/coupon',
        name: 'MarketingCoupon',
        component: () => import('@/views/marketing/coupon/index.vue'),
        meta: { title: '优惠券管理', icon: 'Ticket', parent: '营销管理' }
      },
      {
        path: 'marketing/promotion',
        name: 'MarketingPromotion',
        component: () => import('@/views/marketing/promotion/index.vue'),
        meta: { title: '促销活动', icon: 'AlarmClock', parent: '营销管理' }
      },
      // 内容管理
      {
        path: 'content/banner',
        name: 'ContentBanner',
        component: () => import('@/views/content/banner/index.vue'),
        meta: { title: '轮播图管理', icon: 'Picture', parent: '内容管理' }
      },
      {
        path: 'content/review',
        name: 'ContentReview',
        component: () => import('@/views/content/review/index.vue'),
        meta: { title: '评价管理', icon: 'ChatDotRound', parent: '内容管理' }
      },
      // 系统管理
      {
        path: 'system/menu',
        name: 'SystemMenu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu', parent: '系统管理' }
      },
      {
        path: 'system/log',
        name: 'SystemLog',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Notebook', parent: '系统管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token

  if (to.path === '/login') {
    // 如果已登录，跳转到首页
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    // 如果未登录，跳转到登录页
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
