import request from './request'

// 获取商品列表
export function getProductList(params) {
  return request({
    url: '/api/product/list',
    method: 'get',
    params
  })
}

// 获取商品详情
export function getProductDetail(id) {
  return request({
    url: `/api/product/${id}`,
    method: 'get'
  })
}

// 获取分类树
export function getCategoryTree() {
  return request({
    url: '/api/category/tree',
    method: 'get'
  })
}

// 获取品牌列表
export function getBrandList(params) {
  return request({
    url: '/api/brand/list',
    method: 'get',
    params
  })
}

// 获取规格列表
export function getSpecList(categoryId) {
  return request({
    url: '/api/spec/list',
    method: 'get',
    params: { categoryId }
  })
}
