package com.qa.manager.mapper;

import java.util.List;
import com.qa.manager.domain.ProProject;
import com.qa.manager.domain.Vo.ProProjectVo;

/**
 * 项目管理Mapper接口
 * 
 * @author jinrong.dong
 * @date 2025-03-09
 */
public interface ProProjectMapper 
{
    /**
     * 查询项目管理
     * 
     * @param id 项目管理主键
     * @return 项目管理
     */
    public ProProject selectProProjectById(Long id);

    /**
     * 查询项目管理列表
     * 
     * @param proProject 项目管理
     * @return 项目管理集合
     */
    public List<ProProject> selectProProjectList(ProProject proProject);

    /**
     * 新增项目管理
     * 
     * @param proProject 项目管理
     * @return 结果
     */
    public int insertProProject(ProProject proProject);

    /**
     * 修改项目管理
     * 
     * @param proProject 项目管理
     * @return 结果
     */
    public int updateProProject(ProProject proProject);

    /**
     * 删除项目管理
     * 
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteProProjectById(Long id);

    /**
     * 批量删除项目管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProProjectByIds(Long[] ids);


    /**
     * 查询项目管理列表、需求数量、用户信息
     *
     *
     * @param proProject 项目管理
     * @return ProProjectVo 包含管理列表、需求数量、用户信息
     */
    public List<ProProjectVo> selectProProjectVoList(ProProject proProject);
}
