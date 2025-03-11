package com.qa.manager.mapper;

import java.util.List;
import com.qa.manager.domain.ProRequirement;
import com.qa.manager.domain.Vo.ProRequirementVo;

/**
 * 需求管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
public interface ProRequirementMapper 
{
    /**
     * 查询需求管理
     * 
     * @param id 需求管理主键
     * @return 需求管理
     */
    public ProRequirement selectProRequirementById(Long id);

    /**
     * 查询需求管理列表
     * 
     * @param proRequirement 需求管理
     * @return 需求管理集合
     */
    public List<ProRequirement> selectProRequirementList(ProRequirement proRequirement);

    /**
     * 新增需求管理
     * 
     * @param proRequirement 需求管理
     * @return 结果
     */
    public int insertProRequirement(ProRequirement proRequirement);

    /**
     * 修改需求管理
     * 
     * @param proRequirement 需求管理
     * @return 结果
     */
    public int updateProRequirement(ProRequirement proRequirement);

    /**
     * 删除需求管理
     * 
     * @param id 需求管理主键
     * @return 结果
     */
    public int deleteProRequirementById(Long id);

    /**
     * 批量删除需求管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProRequirementByIds(Long[] ids);

    /**
     * 查询需求管理列表 和 项目名称 、用户信息、需求数量
     * @param proRequirement
     * @return
     */
    List<ProRequirementVo> selectProRequirementVoList(ProRequirement proRequirement);
}
