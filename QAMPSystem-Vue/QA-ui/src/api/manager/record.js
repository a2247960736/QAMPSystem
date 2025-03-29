import request from '@/utils/request'

// 查询用例执行记录列表
export function listRecord(query) {
  return request({
    url: '/manager/record/list',
    method: 'get',
    params: query
  })
}

// 查询用例执行记录详细
export function getRecord(id) {
  return request({
    url: '/manager/record/' + id,
    method: 'get'
  })
}

// 新增用例执行记录
export function addRecord(data) {
  return request({
    url: '/manager/record',
    method: 'post',
    data: data
  })
}

// 修改用例执行记录
export function updateRecord(data) {
  return request({
    url: '/manager/record',
    method: 'put',
    data: data
  })
}

// 删除用例执行记录
export function delRecord(id) {
  return request({
    url: '/manager/record/' + id,
    method: 'delete'
  })
}

// 导出用例执行记录列表
export function exportRecord(data) {
  return request({
    url: '/manager/record/export',
    method: 'post',
    data: data
  })
}

// 列表 - 新增执行任务
export function createRecord(data) {
  return request({
    url: '/manager/record/create',
    method: 'post',
    data: data
  })
}

// 列表 - 修改执行任务
export function editRecord(data) {
  return request({
    url: '/manager/record/edit',
    method: 'post',
    data: data
  })
}

// 列表 - 删除执行任务
export function deleteRecord(data) {
  return request({
    url: '/manager/record/delete',
    method: 'post',
    data: data
  })
}

// 脑图 - 清理 json 中所有的执行记录
export function clearRecord(data) {
  return request({
    url: '/manager/record/clear',
    method: 'post',
    data: data
  })
}

// 脑图 - 获取该任务用例上方的统计信息
export function getRecordGeneralInfo(id) {
  return request({
    url: '/manager/record/getRecordInfo',
    method: 'get',
    params: { id }
  })
}    