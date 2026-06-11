import request from './request'

// 获取轮播图列表
export function getBannerList() {
  return request({
    url: '/api/banner/list',
    method: 'get',
    params: { pageNum: 1, pageSize: 10 }
  })
}
