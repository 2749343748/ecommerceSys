import request from '@/utils/request'

// 分页查询订单列表
export function getOrderList(params) {
  return request({
    url: '/api/order/list',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderDetail(id) {
  return request({
    url: `/api/order/${id}`,
    method: 'get'
  })
}

// 修改订单状态
export function updateOrderStatus(id, status) {
  return request({
    url: '/api/order/status',
    method: 'put',
    params: { id, status }
  })
}

// 获取统计数据
export function getDashboardTotal() {
  return request({
    url: '/api/order/dashboard/total',
    method: 'get'
  })
}

// 获取销售趋势
export function getSaleTrend() {
  return request({
    url: '/api/order/dashboard/saleTrend',
    method: 'get'
  })
}
