import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCartList, addToCart, updateCartQuantity, deleteCartItem, updateCartChecked, checkAllCart } from '@/api/cart'
import { useUserStore } from './user'

export const useCartStore = defineStore('cart', () => {
  const cartList = ref([])

  // 购物车商品数量
  const cartCount = computed(() => {
    return cartList.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  // 选中的商品
  const checkedList = computed(() => {
    return cartList.value.filter(item => item.checked === 1)
  })

  // 选中商品总价
  const totalPrice = computed(() => {
    return checkedList.value.reduce((sum, item) => {
      return sum + (item.productPrice || 0) * item.quantity
    }, 0)
  })

  // 选中商品数量
  const checkedCount = computed(() => {
    return checkedList.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  // 是否全选
  const isAllChecked = computed(() => {
    return cartList.value.length > 0 && cartList.value.every(item => item.checked === 1)
  })

  // 获取购物车列表
  async function fetchCartList() {
    const userStore = useUserStore()
    if (!userStore.token) {
      cartList.value = []
      return
    }
    try {
      const res = await getCartList(userStore.userInfo?.id)
      if (res.code === 200) {
        cartList.value = res.data || []
      }
    } catch (error) {
      console.error('获取购物车失败', error)
    }
  }

  // 添加到购物车
  async function addItem(productId, quantity = 1) {
    const userStore = useUserStore()
    if (!userStore.token) {
      return false
    }
    try {
      await addToCart(userStore.userInfo?.id, productId, quantity)
      await fetchCartList()
      return true
    } catch (error) {
      console.error('添加购物车失败', error)
      return false
    }
  }

  // 更新数量
  async function updateQuantity(id, quantity) {
    try {
      await updateCartQuantity(id, quantity)
      await fetchCartList()
    } catch (error) {
      console.error('更新数量失败', error)
    }
  }

  // 删除商品
  async function removeItem(id) {
    try {
      await deleteCartItem(id)
      await fetchCartList()
    } catch (error) {
      console.error('删除失败', error)
    }
  }

  // 切换选中状态
  async function toggleChecked(id, checked) {
    try {
      await updateCartChecked(id, checked)
      await fetchCartList()
    } catch (error) {
      console.error('更新选中状态失败', error)
    }
  }

  // 全选/取消全选
  async function toggleCheckAll(checked) {
    const userStore = useUserStore()
    try {
      await checkAllCart(userStore.userInfo?.id, checked)
      await fetchCartList()
    } catch (error) {
      console.error('全选操作失败', error)
    }
  }

  return {
    cartList,
    cartCount,
    checkedList,
    totalPrice,
    checkedCount,
    isAllChecked,
    fetchCartList,
    addItem,
    updateQuantity,
    removeItem,
    toggleChecked,
    toggleCheckAll
  }
})
