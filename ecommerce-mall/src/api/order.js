import request from './request'

// 创建订单
export function createOrder(data) {
  return request({
    url: '/api/order/create',
    method: 'post',
    data
  })
}

// 获取用户订单列表
export function getUserOrders(params) {
  return request({
    url: '/api/order/user/list',
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

// 取消订单
export function cancelOrder(id) {
  return request({
    url: '/api/order/status',
    method: 'put',
    params: { id, status: 4 }
  })
}

// 确认收货
export function confirmOrder(id) {
  return request({
    url: '/api/order/status',
    method: 'put',
    params: { id, status: 3 }
  })
}

// 模拟支付
export function payOrder(orderId, payMethod) {
  return request({
    url: '/api/order/pay',
    method: 'post',
    params: { orderId, payMethod }
  })
}

// 获取物流信息
export function getLogistics(orderId) {
  return request({
    url: '/api/logistics/list',
    method: 'get',
    params: { orderId }
  })
}

// 获取物流轨迹
export function getLogisticsTraces(logisticsId) {
  return request({
    url: '/api/logistics/traces',
    method: 'get',
    params: { logisticsId }
  })
}
