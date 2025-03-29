package com.qa.manager.service;

import java.util.List;

import com.qa.common.core.domain.entity.SysUser;
import com.qa.manager.domain.TestCase;
import com.qa.manager.domain.request.cases.*;
import com.qa.manager.domain.request.dir.DirTreeResp;

/**
 * 测试用例Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
public interface ITestCaseService 
{
    /**
     * 查询测试用例
     * 
     * @param id 测试用例主键
     * @return 测试用例
     */
    public TestCase selectTestCaseById(Long id);

    /**
     * 查询测试用例列表
     * 
     * @param testCase 测试用例
     * @return 测试用例集合
     */
    public List<TestCase> selectTestCaseList(TestCase testCase);

    /**
     * 新增测试用例
     * 
     * @param testCase 测试用例
     * @return 结果
     */
    public int insertTestCase(TestCase testCase);

    /**
     * 修改测试用例
     * 
     * @param testCase 测试用例
     * @return 结果
     */
    public int updateTestCase(TestCase testCase);

    /**
     * 批量删除测试用例
     * 
     * @param ids 需要删除的测试用例主键集合
     * @return 结果
     */
    public int deleteTestCaseByIds(Long[] ids);

    /**
     * 删除测试用例信息
     * 
     * @param id 测试用例主键
     * @return 结果
     */
    public int deleteTestCaseById(Long id);


    /**
     * 获取case列表
     *
     * @param request 请求体
     * @return 用例集列表
     */
    List<CaseListResp> getCaseList(CaseQueryReq request);

    /**
     * 根据文件夹id获取用例集列表
     *
     * @param caseId 用例id
     * @return 用例集详情
     */
    CaseDetailResp getCaseDetail(Long caseId);

    /**
     * 用例新建或者复制
     *
     * @param request 请求体
     * @return 创建的测试用例的caseId
     */
    Long insertOrDuplicateCase(CaseCreateReq request);

    /**
     * 更新用例
     *
     * @param request 请求体
     * @return 更新后的节点
     */
    DirTreeResp updateCase(CaseEditReq request);

    /**
     * 删除用例
     *
     * @param caseId 用例id
     * @return 被删除的用例的主键id
     */
    DirTreeResp deleteCase(Long caseId);

    /**
     * 获取根据用例种类和业务线获取用例的创建人map
     *
     * @param caseType 用例种类
     * @param lineId (本质上就是productLineId) 业务线id
     * @return 某条业务线，特定用例种类下的用例创建人
     */
    List<SysUser> listCreators(Integer caseType, Long lineId);

    /**
     * 遍历一份用例，获取其中满足几个条件的count
     *
     * @param req 优先级、资源
     * @return 同级个数
     */
    CaseConditionResp getCountByCondition(CaseConditionReq req);

    /**
     * 点开用例后查看id 标题 关联需求的基本信息
     *
     * @param caseId 用例id
     * @return 概览信息
     */
    CaseGeneralInfoResp getCaseGeneralInfo(Long caseId);


}
