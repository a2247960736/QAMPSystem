package com.qa.manager.service.impl;

import java.util.List;
import com.qa.common.utils.DateUtils;
import com.qa.manager.domain.Vo.ProRequirementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.ProRequirementMapper;
import com.qa.manager.domain.ProRequirement;
import com.qa.manager.service.IProRequirementService;

/**
 * 需求管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@Service
public class ProRequirementServiceImpl implements IProRequirementService 
{
    @Autowired
    private ProRequirementMapper proRequirementMapper;

    /**
     * 查询需求管理
     * 
     * @param id 需求管理主键
     * @return 需求管理
     */
    @Override
    public ProRequirement selectProRequirementById(Long id)
    {
        return proRequirementMapper.selectProRequirementById(id);
    }

    /**
     * 查询需求管理列表
     * 
     * @param proRequirement 需求管理
     * @return 需求管理
     */
    @Override
    public List<ProRequirement> selectProRequirementList(ProRequirement proRequirement)
    {
        return proRequirementMapper.selectProRequirementList(proRequirement);
    }

    /**
     * 新增需求管理
     * 
     * @param proRequirement 需求管理
     * @return 结果
     */
    @Override
    public int insertProRequirement(ProRequirement proRequirement)
    {
        proRequirement.setCreateTime(DateUtils.getNowDate());
        return proRequirementMapper.insertProRequirement(proRequirement);
    }

    /**
     * 修改需求管理
     * 
     * @param proRequirement 需求管理
     * @return 结果
     */
    @Override
    public int updateProRequirement(ProRequirement proRequirement)
    {
        return proRequirementMapper.updateProRequirement(proRequirement);
    }

    /**
     * 批量删除需求管理
     * 
     * @param ids 需要删除的需求管理主键
     * @return 结果
     */
    @Override
    public int deleteProRequirementByIds(Long[] ids)
    {
        return proRequirementMapper.deleteProRequirementByIds(ids);
    }

    /**
     * 删除需求管理信息
     * 
     * @param id 需求管理主键
     * @return 结果
     */
    @Override
    public int deleteProRequirementById(Long id)
    {
        return proRequirementMapper.deleteProRequirementById(id);
    }

    /**
     * 查询需求管理列表 和 项目名称
     * @param proRequirement
     * @return
     */
    @Override
    public List<ProRequirementVo> selectProRequirementVoList(ProRequirement proRequirement) {
        return proRequirementMapper.selectProRequirementVoList(proRequirement);
    }
}
