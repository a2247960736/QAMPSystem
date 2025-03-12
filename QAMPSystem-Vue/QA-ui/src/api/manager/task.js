import request from '@/utils/request'

// 查询个人任务管理列表
export function listTask(query) {
  return request({
    url: '/manager/task/list',
    method: 'get',
    params: query
  })
}

// 查询个人任务管理详细
export function getTask(id) {
  return request({
    url: '/manager/task/' + id,
    method: 'get'
  })
}

// 新增个人任务管理
export function addTask(data) {
  return request({
    url: '/manager/task',
    method: 'post',
    data: data
  })
}

// 修改个人任务管理
export function updateTask(data) {
  return request({
    url: '/manager/task',
    method: 'put',
    data: data
  })
}

// 删除个人任务管理
export function delTask(id) {
  return request({
    url: '/manager/task/' + id,
    method: 'delete'
  })
}
