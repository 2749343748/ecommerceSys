import request from '@/utils/request'

// 分页查询品牌列表
export function listBrand(params) {
  return request({
    url: '/api/brand/list',
    method: 'get',
    params
  })
}

// 新增品牌
export function addBrand(data) {
  return request({
    url: '/api/brand',
    method: 'post',
    data
  })
}

// 修改品牌
export function updateBrand(data) {
  return request({
    url: '/api/brand',
    method: 'put',
    data
  })
}

// 删除品牌
export function deleteBrand(id) {
  return request({
    url: `/api/brand/${id}`,
    method: 'delete'
  })
}

// 修改品牌状态
export function updateBrandStatus(id, status) {
  return request({
    url: '/api/brand/status',
    method: 'put',
    params: { id, status }
  })
}
