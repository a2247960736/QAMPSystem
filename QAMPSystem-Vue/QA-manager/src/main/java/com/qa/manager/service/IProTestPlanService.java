package com.qa.manager.service;

import java.util.List;
import com.qa.manager.domain.ProTestPlan;
import com.qa.manager.domain.Vo.ProTestPlanVo;

/**
 * 测试计划Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-14
 */
public interface IProTestPlanService 
{
    /**
     * 查询测试计划
     * 
     * @param id 测试计划主键
     * @return 测试计划
     */
    public ProTestPlan selectProTestPlanById(Long id);

    /**
     * 查询测试计划列表
     * 
     * @param proTestPlan 测试计划
     * @return 测试计划集合
     */
    public List<ProTestPlan> selectProTestPlanList(ProTestPlan proTestPlan);

    /**
     * 新增测试计划
     * 
     * @param proTestPlan 测试计划
     * @return 结果
     */
    public int insertProTestPlan(ProTestPlan proTestPlan);

    /**
     * 修改测试计划
     * 
     * @param proTestPlan 测试计划
     * @return 结果
     */
    public int updateProTestPlan(ProTestPlan proTestPlan);

    /**
     * 批量删除测试计划
     * 
     * @param ids 需要删除的测试计划主键集合
     * @return 结果
     */
    public int deleteProTestPlanByIds(Long[] ids);

    /**
     * 删除测试计划信息
     * 
     * @param id 测试计划主键
     * @return 结果
     */
    public int deleteProTestPlanById(Long id);

    /**
     * 查询测试计划列表 - 视图对象
     * 用于查询测试计划及其关联的项目信息和需求信息
     *
     * @param proTestPlan 测试计划
     * @return 测试计划集合
     */
    List<ProTestPlanVo> selectProTestPlanListVo(ProTestPlan proTestPlan);


}
