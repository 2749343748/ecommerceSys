import request from '@/utils/request'

// 查询所有规格列表
export function listAllSpecs() {
  return request({
    url: '/api/spec/all',
    method: 'get'
  })
}

// 根据分类ID查询规格列表
export function listSpecByCategory(categoryId) {
  return request({
    url: '/api/spec/list',
    method: 'get',
    params: { categoryId }
  })
}

// 查询规格值列表
export function listSpecValues(specId) {
  return request({
    url: '/api/spec/values',
    method: 'get',
    params: { specId }
  })
}

// 新增规格
export function addSpec(data) {
  return request({
    url: '/api/spec',
    method: 'post',
    data
  })
}

// 修改规格
export function updateSpec(data) {
  return request({
    url: '/api/spec',
    method: 'put',
    data
  })
}

// 删除规格
export function deleteSpec(id) {
  return request({
    url: `/api/spec/${id}`,
    method: 'delete'
  })
}

// 新增规格值
export function addSpecValue(data) {
  return request({
    url: '/api/spec/value',
    method: 'post',
    data
  })
}

// 删除规格值
export function deleteSpecValue(id) {
  return request({
    url: `/api/spec/value/${id}`,
    method: 'delete'
  })
}
