package com.qa.manager.service;

import java.util.List;
import com.qa.manager.domain.ProProject;

/**
 * 项目管理Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-09
 */
public interface IProProjectService 
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
     * 批量删除项目管理
     * 
     * @param ids 需要删除的项目管理主键集合
     * @return 结果
     */
    public int deleteProProjectByIds(Long[] ids);

    /**
     * 删除项目管理信息
     * 
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteProProjectById(Long id);
}
