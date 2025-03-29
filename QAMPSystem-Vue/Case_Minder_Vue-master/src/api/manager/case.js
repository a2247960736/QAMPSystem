import service from '@/utils/request'



// 查询测试用例详细
export function getCase(id) {
    return service({
        url: 'http://localhost:8080/manager/case/' + id,
        method: 'get'
    })
}

// 新增测试用例
export function addCase(data) {
    return service({
        url: 'http://localhost:8080/manager/case',
        method: 'post',
        data: data
    })
}

// 修改测试用例
export function updateCase(data) {
    return service({
        url: 'http://localhost:8080/manager/case',
        method: 'put',
        data: data
    })
}

// 删除测试用例
export function delCase(id) {
    return service({
        url: 'http://localhost:8080/manager/case/' + id,
        method: 'delete'
    })
}

// 查询测试用例列表
export function listCase(query) {
    return service({
        url: 'http://localhost:8080/manager/case/list',
        method: 'get',
        params: query
    })
}

// 列表 - 创建或者复制用例
export function createOrCopyCase(data) {
    return service({
        url: 'http://localhost:8080/manager/case/create',
        method: 'post',
        data: data
    })
}

// 列表 - 修改用例属性
export function editCase(data) {
    return service({
        url: 'http://localhost:8080/manager/case/edit',
        method: 'post',
        data: data
    })
}

// 列表 - 删除用例
export function deleteCaseById(data) {
    return service({
        url: 'http://localhost:8080/manager/case/delete',
        method: 'post',
        data: data
    })
}

// 列表 - 查看用例详情
export function getCaseDetail(caseId) {
    return service({
        url: 'http://localhost:8080/manager/case/detail',
        method: 'get',
        params: { caseId }
    })
}

// 配合list 筛选时获取所有创建人的列表
export function listCreators(caseType, productLineId) {
    return service({
        url: 'http://localhost:8080/manager/case/listCreators',
        method: 'get',
        params: { caseType, productLineId }
    })
}

// 配合detail 修改圈选用例时统计的用例条目数据
export function getCountByCondition(caseId, priority, resource) {
    return service({
        url: 'http://localhost:8080/manager/case/countByCondition',
        method: 'get',
        params: { caseId, priority, resource }
    })
}

// 脑图 - 获取上方用例概览信息
export function getCaseGeneralInfo(id) {
    return service({
        url: 'http://localhost:8080/manager/case/getCaseInfo',
        method: 'get',
        params: { id }
    })
}