import request from '@/utils/request'

// 分页查询库存列表
export function listInventory(params) {
  return request({
    url: '/api/inventory/list',
    method: 'get',
    params
  })
}

// 入库
export function stockIn(data) {
  return request({
    url: '/api/inventory/in',
    method: 'post',
    data
  })
}

// 出库
export function stockOut(data) {
  return request({
    url: '/api/inventory/out',
    method: 'post',
    data
  })
}

// 分页查询库存记录
export function listInventoryRecords(params) {
  return request({
    url: '/api/inventory/records',
    method: 'get',
    params
  })
}
