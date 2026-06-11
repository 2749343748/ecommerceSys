import request from './request'

// 获取购物车列表
export function getCartList(userId) {
  return request({
    url: '/api/cart/list',
    method: 'get',
    params: { userId }
  })
}

// 添加到购物车（userId从JWT token获取）
export function addToCart(productId, quantity) {
  return request({
    url: '/api/cart',
    method: 'post',
    params: { productId, quantity }
  })
}

// 更新购物车数量
export function updateCartQuantity(id, quantity) {
  return request({
    url: '/api/cart/quantity',
    method: 'put',
    params: { id, quantity }
  })
}

// 更新选中状态
export function updateCartChecked(id, checked) {
  return request({
    url: '/api/cart/checked',
    method: 'put',
    params: { id, checked }
  })
}

// 全选/取消全选（userId从JWT token获取）
export function checkAllCart(checked) {
  return request({
    url: '/api/cart/checkAll',
    method: 'put',
    params: { checked }
  })
}

// 删除购物车商品
export function deleteCartItem(id) {
  return request({
    url: `/api/cart/${id}`,
    method: 'delete'
  })
}
