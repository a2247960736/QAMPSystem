import service from '@/utils/request'

// 查询用例执行记录列表
export function listRecord(query) {
    return service({
        url: 'service/manager/record/list',
        method: 'get',
        params: query
    })
}

// 查询用例执行记录详细
export function getRecord(id) {
    return service({
        url: 'http://localhost:8080/manager/record/' + id,
        method: 'get'
    })
}

// 新增用例执行记录
export function addRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record',
        method: 'post',
        data: data
    })
}

// 修改用例执行记录
export function updateRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record',
        method: 'put',
        data: data
    })
}

// 删除用例执行记录
export function delRecord(id) {
    return service({
        url: 'http://localhost:8080/manager/record/' + id,
        method: 'delete'
    })
}

// 导出用例执行记录列表
export function exportRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/export',
        method: 'post',
        data: data
    })
}

// 列表 - 新增执行任务
export function createRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/create',
        method: 'post',
        data: data
    })
}

// 列表 - 修改执行任务
export function editRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/edit',
        method: 'post',
        data: data
    })
}

// 列表 - 删除执行任务
export function deleteRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/delete',
        method: 'post',
        data: data
    })
}

// 脑图 - 清理 json 中所有的执行记录
export function clearRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/clear',
        method: 'post',
        data: data
    })
}

// 脑图 - 获取该任务用例上方的统计信息
export function getRecordGeneralInfo(id) {
    return service({
        url: 'http://localhost:8080/manager/record/getRecordInfo',
        method: 'get',
        params: { id }
    })
}


// 查询记录详情
export function getRecordTail(recordId, caseId) {
    return service({
        url: 'http://localhost:8080/manager/record/detail',
        method: 'get',
        params: { recordId, caseId }
    })
}

//更新 updateRecord
export function updateNowRecord(data) {
    return service({
        url: 'http://localhost:8080/manager/record/updateRecord',
        method: 'post',
        data: data
    })
}  