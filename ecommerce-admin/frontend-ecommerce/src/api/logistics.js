import request from '@/utils/request'

// 分页查询物流列表
export function listLogistics(params) {
  return request({
    url: '/api/logistics/list',
    method: 'get',
    params
  })
}

// 新增物流信息
export function addLogistics(data) {
  return request({
    url: '/api/logistics',
    method: 'post',
    data
  })
}

// 修改物流状态
export function updateLogisticsStatus(id, status) {
  return request({
    url: '/api/logistics/status',
    method: 'put',
    params: { id, status }
  })
}

// 添加物流轨迹
export function addLogisticsTrace(data) {
  return request({
    url: '/api/logistics/trace',
    method: 'post',
    data
  })
}

// 查询物流轨迹
export function listLogisticsTraces(logisticsId) {
  return request({
    url: '/api/logistics/traces',
    method: 'get',
    params: { logisticsId }
  })
}
