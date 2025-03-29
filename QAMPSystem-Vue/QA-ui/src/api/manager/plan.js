import request from '@/utils/request'

// 查询测试计划列表
export function listPlan(query) {
  return request({
    url: '/manager/plan/list',
    method: 'get',
    params: query
  })
}

// 查询测试计划详细
export function getPlan(id) {
  return request({
    url: '/manager/plan/' + id,
    method: 'get'
  })
}

// 新增测试计划
export function addPlan(data) {
  return request({
    url: '/manager/plan',
    method: 'post',
    data: data
  })
}

// 修改测试计划
export function updatePlan(data) {
  return request({
    url: '/manager/plan',
    method: 'put',
    data: data
  })
}

// 删除测试计划
export function delPlan(id) {
  return request({
    url: '/manager/plan/' + id,
    method: 'delete'
  })
}

export function listPlanByReqId(id) {
  return request({
    url: '/manager/plan/requirements/' + id,
    method: 'get'
  })
}
