import request from './request'

// 获取优惠券列表
export function getCouponList(params) {
  return request({
    url: '/api/coupon/list',
    method: 'get',
    params
  })
}
