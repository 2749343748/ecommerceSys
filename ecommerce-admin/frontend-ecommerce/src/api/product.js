import request from '@/utils/request'

// 分页查询商品列表
export function getProductList(params) {
  return request({
    url: '/api/product/list',
    method: 'get',
    params
  })
}

// 新增商品
export function addProduct(data) {
  return request({
    url: '/api/product',
    method: 'post',
    data
  })
}

// 修改商品
export function updateProduct(data) {
  return request({
    url: '/api/product',
    method: 'put',
    data
  })
}

// 删除商品
export function deleteProduct(id) {
  return request({
    url: `/api/product/${id}`,
    method: 'delete'
  })
}

// 修改商品状态
export function updateProductStatus(id, status) {
  return request({
    url: '/api/product/status',
    method: 'put',
    params: { id, status }
  })
}

// ========== 分类相关 ==========

// 获取分类树
export function getCategoryTree() {
  return request({
    url: '/api/category/tree',
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/api/category',
    method: 'post',
    data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/api/category',
    method: 'put',
    data
  })
}

// 删除分类
export function deleteCategory(id) {
  return request({
    url: `/api/category/${id}`,
    method: 'delete'
  })
}
