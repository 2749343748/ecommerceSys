import request from './request'

// 获取商品评价列表
export function getProductReviews(params) {
  return request({
    url: '/api/review/list',
    method: 'get',
    params
  })
}
