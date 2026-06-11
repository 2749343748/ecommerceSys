import request from '@/utils/request'

// 分页查询操作日志
export function listLog(params) {
  return request({
    url: '/api/log/list',
    method: 'get',
    params
  })
}
