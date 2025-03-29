import request from '@/utils/request'

// 查询文件夹列表
export function listBiz(query) {
  return request({
    url: '/manager/biz/list',
    method: 'get',
    params: query
  })
}

// 查询文件夹详细
export function getBiz(id) {
  return request({
    url: '/manager/biz/' + id,
    method: 'get'
  })
}

// 新增文件夹
export function addBiz(data) {
  return request({
    url: '/manager/biz',
    method: 'post',
    data: data
  })
}

// 修改文件夹
export function updateBiz(data) {
  return request({
    url: '/manager/biz',
    method: 'put',
    data: data
  })
}

// 删除文件夹
export function delBiz(id) {
  return request({
    url: '/manager/biz/' + id,
    method: 'delete'
  })
}


// 重命名文件夹
export function renameDir(data) {
  return request({
    url: '/manager/biz/rename',
    method: 'post',
    data: data
  })
}

// 删除目录节点
export function deleteDir(data) {
  return request({
    url: '/manager/biz/delete',
    method: 'post',
    data: data
  })
}

// 获取卡片目录树
export function getDirCardTree(query) {
  return request({
    url: '/manager/biz/cardTree',
    method: 'get',
    params: query
  })
}

// 移动目录
export function moveDir(data) {
  return request({
    url: '/manager/biz/move',
    method: 'post',
    data: data
  })
}
