package com.qa.manager.service.impl;

import java.util.List;
import com.qa.common.utils.DateUtils;
import com.qa.manager.domain.Vo.ProTestPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.ProTestPlanMapper;
import com.qa.manager.domain.ProTestPlan;
import com.qa.manager.service.IProTestPlanService;

/**
 * 测试计划Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-14
 */
@Service
public class ProTestPlanServiceImpl implements IProTestPlanService 
{
    @Autowired
    private ProTestPlanMapper proTestPlanMapper;

    /**
     * 查询测试计划
     * 
     * @param id 测试计划主键
     * @return 测试计划
     */
    @Override
    public ProTestPlan selectProTestPlanById(Long id)
    {
        return proTestPlanMapper.selectProTestPlanById(id);
    }

    /**
     * 查询测试计划列表
     * 
     * @param proTestPlan 测试计划
     * @return 测试计划
     */
    @Override
    public List<ProTestPlan> selectProTestPlanList(ProTestPlan proTestPlan)
    {
        return proTestPlanMapper.selectProTestPlanList(proTestPlan);
    }

    /**
     * 新增测试计划
     * 
     * @param proTestPlan 测试计划
     * @return 结果
     */
    @Override
    public int insertProTestPlan(ProTestPlan proTestPlan)
    {
        proTestPlan.setCreateTime(DateUtils.getNowDate());
        return proTestPlanMapper.insertProTestPlan(proTestPlan);
    }

    /**
     * 修改测试计划
     * 
     * @param proTestPlan 测试计划
     * @return 结果
     */
    @Override
    public int updateProTestPlan(ProTestPlan proTestPlan)
    {
        proTestPlan.setUpdateTime(DateUtils.getNowDate());
        return proTestPlanMapper.updateProTestPlan(proTestPlan);
    }

    /**
     * 批量删除测试计划
     * 
     * @param ids 需要删除的测试计划主键
     * @return 结果
     */
    @Override
    public int deleteProTestPlanByIds(Long[] ids)
    {
        return proTestPlanMapper.deleteProTestPlanByIds(ids);
    }

    /**
     * 删除测试计划信息
     * 
     * @param id 测试计划主键
     * @return 结果
     */
    @Override
    public int deleteProTestPlanById(Long id)
    {
        return proTestPlanMapper.deleteProTestPlanById(id);
    }

    /**
     * 查询测试计划列表 - 视图对象
     * 用于查询测试计划及其关联的项目信息和需求信息
     *
     * @param proTestPlan 测试计划
     * @return 测试计划集合
     */
    @Override
    public List<ProTestPlanVo> selectProTestPlanListVo(ProTestPlan proTestPlan) {
        return proTestPlanMapper.selectProTestPlanListVo(proTestPlan);
    }
}
