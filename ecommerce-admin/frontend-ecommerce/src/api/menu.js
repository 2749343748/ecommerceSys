import request from '@/utils/request'

// 查询菜单树
export function getMenuTree() {
  return request({
    url: '/api/menu/tree',
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/api/menu',
    method: 'post',
    data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/api/menu',
    method: 'put',
    data
  })
}

// 删除菜单
export function deleteMenu(id) {
  return request({
    url: `/api/menu/${id}`,
    method: 'delete'
  })
}
