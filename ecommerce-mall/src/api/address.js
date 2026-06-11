import request from './request'

// 获取收货地址列表
export function getAddressList(userId) {
  return request({
    url: '/api/address/list',
    method: 'get',
    params: { userId }
  })
}

// 新增收货地址
export function addAddress(data) {
  return request({
    url: '/api/address',
    method: 'post',
    data
  })
}

// 修改收货地址
export function updateAddress(data) {
  return request({
    url: '/api/address',
    method: 'put',
    data
  })
}

// 删除收货地址
export function deleteAddress(id) {
  return request({
    url: `/api/address/${id}`,
    method: 'delete'
  })
}

// 设置默认地址
export function setDefaultAddress(id, userId) {
  return request({
    url: '/api/address/default',
    method: 'put',
    params: { id, userId }
  })
}
