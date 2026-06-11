import request from '@/utils/request'

// 分页查询优惠券列表
export function listCoupon(params) {
  return request({
    url: '/api/coupon/list',
    method: 'get',
    params
  })
}

// 新增优惠券
export function addCoupon(data) {
  return request({
    url: '/api/coupon',
    method: 'post',
    data
  })
}

// 修改优惠券
export function updateCoupon(data) {
  return request({
    url: '/api/coupon',
    method: 'put',
    data
  })
}

// 删除优惠券
export function deleteCoupon(id) {
  return request({
    url: `/api/coupon/${id}`,
    method: 'delete'
  })
}

// 修改优惠券状态
export function updateCouponStatus(id, status) {
  return request({
    url: '/api/coupon/status',
    method: 'put',
    params: { id, status }
  })
}
