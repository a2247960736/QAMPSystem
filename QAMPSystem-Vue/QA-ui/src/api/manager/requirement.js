import request from '@/utils/request'

// 查询需求管理列表
export function listRequirement(query) {
  return request({
    url: '/manager/requirement/list',
    method: 'get',
    params: query
  })
}

// 查询需求管理详细
export function getRequirement(id) {
  return request({
    url: '/manager/requirement/' + id,
    method: 'get'
  })
}

// 新增需求管理
export function addRequirement(data) {
  return request({
    url: '/manager/requirement',
    method: 'post',
    data: data
  })
}

// 修改需求管理
export function updateRequirement(data) {
  return request({
    url: '/manager/requirement',
    method: 'put',
    data: data
  })
}

// 删除需求管理
export function delRequirement(id) {
  return request({
    url: '/manager/requirement/' + id,
    method: 'delete'
  })

}

export function listRequirementByProject(projectId) {
  return request({
    url: '/manager/requirement/data/list/' + projectId,
    method: 'get'
  })
}
