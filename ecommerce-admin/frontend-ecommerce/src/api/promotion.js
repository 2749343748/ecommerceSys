import request from '@/utils/request'

// 分页查询促销活动列表
export function listPromotion(params) {
  return request({
    url: '/api/promotion/list',
    method: 'get',
    params
  })
}

// 新增促销活动
export function addPromotion(data) {
  return request({
    url: '/api/promotion',
    method: 'post',
    data
  })
}

// 修改促销活动
export function updatePromotion(data) {
  return request({
    url: '/api/promotion',
    method: 'put',
    data
  })
}

// 删除促销活动
export function deletePromotion(id) {
  return request({
    url: `/api/promotion/${id}`,
    method: 'delete'
  })
}

// 修改活动状态
export function updatePromotionStatus(id, status) {
  return request({
    url: '/api/promotion/status',
    method: 'put',
    params: { id, status }
  })
}
