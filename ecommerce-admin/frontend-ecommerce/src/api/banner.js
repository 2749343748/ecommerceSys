import request from '@/utils/request'

// 分页查询轮播图列表
export function listBanner(params) {
  return request({
    url: '/api/banner/list',
    method: 'get',
    params
  })
}

// 新增轮播图
export function addBanner(data) {
  return request({
    url: '/api/banner',
    method: 'post',
    data
  })
}

// 修改轮播图
export function updateBanner(data) {
  return request({
    url: '/api/banner',
    method: 'put',
    data
  })
}

// 删除轮播图
export function deleteBanner(id) {
  return request({
    url: `/api/banner/${id}`,
    method: 'delete'
  })
}

// 修改轮播图状态
export function updateBannerStatus(id, status) {
  return request({
    url: '/api/banner/status',
    method: 'put',
    params: { id, status }
  })
}
