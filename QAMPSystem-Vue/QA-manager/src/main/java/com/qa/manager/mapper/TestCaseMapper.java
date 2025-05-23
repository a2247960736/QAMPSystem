package com.qa.manager.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.qa.manager.domain.TestCase;
import org.apache.ibatis.annotations.Param;

/**
 * 测试用例Mapper接口
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
public interface TestCaseMapper 
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
     * 删除测试用例
     * 
     * @param id 测试用例主键
     * @return 结果
     */
    public int deleteTestCaseById(Long id);

    /**
     * 批量删除测试用例
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTestCaseByIds(Long[] ids);



    /**
     * 新增用例
     *
     * @param testCase 测试用例
     * @return id
     */
    int insert(TestCase testCase);

    /**
     * 根据主键查询
     *
     * @param id 用例id
     * @return 用例实体
     */
    TestCase selectOne(Long id);

    /**
     * 修改用例实体
     *
     * @param testCase 修改后的实体
     * @return id
     */
    int update(TestCase testCase);

    /**
     * 删除用例
     *
     * @param caseId 用例id
     */
    void delete(Long caseId);

    /**
     * 条件筛选测试用例
     *
     * @param caseType 用例种类
     * @param caseIds 用例id列表 -- 从biz表过来的
     * @param title 标题
     * @param creator 创建人
     * @param planId 需求id
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param channel 默认 1
     * @param productLineId 业务线id
     * @return 筛选后的列表
     */
    List<TestCase> search(@Param("caseType") Integer caseType,
                          @Param("caseIds") List<Long> caseIds,
                          @Param("title") String title,
                          @Param("creator") String creator,
                          @Param("planId") String planId,
                          @Param("beginTime") Date beginTime,
                          @Param("endTime") Date endTime,
                          @Param("channel") Integer channel,
                          @Param("productLineId") Long productLineId,
                          @Param("caseKeyWords") String caseKeyWords);

    /**
     * 查找创建人
     *
     * @param caseType 用例种类
     * @param productLineId 业务线id
     * @return 一堆姓名
     */
    List<String> listCreators(@Param("caseType") Integer caseType, @Param("productLineId") Long productLineId);

    /**
     * 拿到渠道下某条业务线所有的caseIds
     *
     * @param productLineId 业务线id
     * @param channel 渠道
     * @return id集合
     */
    Set<String> findCaseIdsInBiz(@Param("productLineId") Long productLineId, @Param("channel") Integer channel);





}
