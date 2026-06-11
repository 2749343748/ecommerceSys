import request from '@/utils/request'

// 分页查询评价列表
export function listReview(params) {
  return request({
    url: '/api/review/list',
    method: 'get',
    params
  })
}

// 回复评价
export function replyReview(id, reply) {
  return request({
    url: '/api/review/reply',
    method: 'put',
    params: { id, reply }
  })
}

// 修改评价状态
export function updateReviewStatus(id, status) {
  return request({
    url: '/api/review/status',
    method: 'put',
    params: { id, status }
  })
}

// 删除评价
export function deleteReview(id) {
  return request({
    url: `/api/review/${id}`,
    method: 'delete'
  })
}
