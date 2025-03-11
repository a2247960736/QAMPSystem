package com.qa.manager.service.impl;

import java.util.List;
import com.qa.common.utils.DateUtils;
import com.qa.manager.service.IProProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.ProProjectMapper;
import com.qa.manager.domain.ProProject;

import static com.qa.common.utils.SecurityUtils.getUserId;

/**
 * 项目管理Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-09
 */
@Service
public class ProProjectServiceImpl implements IProProjectService
{
    @Autowired
    private ProProjectMapper proProjectMapper;

    /**
     * 查询项目管理
     * 
     * @param id 项目管理主键
     * @return 项目管理
     */
    @Override
    public ProProject selectProProjectById(Long id)
    {
        return proProjectMapper.selectProProjectById(id);
    }

    /**
     * 查询项目管理列表
     * 
     * @param proProject 项目管理
     * @return 项目管理
     */
    @Override
    public List<ProProject> selectProProjectList(ProProject proProject)
    {
        return proProjectMapper.selectProProjectList(proProject);
    }

    /**
     * 新增项目管理
     * 
     * @param proProject 项目管理
     * @return 结果
     */
    @Override
    public int insertProProject(ProProject proProject)
    {
        proProject.setCreateTime(DateUtils.getNowDate());
        proProject.setCreatorId(getUserId());
        return proProjectMapper.insertProProject(proProject);
    }

    /**
     * 修改项目管理
     * 
     * @param proProject 项目管理
     * @return 结果
     */
    @Override
    public int updateProProject(ProProject proProject)
    {
        return proProjectMapper.updateProProject(proProject);
    }

    /**
     * 批量删除项目管理
     * 
     * @param ids 需要删除的项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProProjectByIds(Long[] ids)
    {
        return proProjectMapper.deleteProProjectByIds(ids);
    }

    /**
     * 删除项目管理信息
     * 
     * @param id 项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProProjectById(Long id)
    {
        return proProjectMapper.deleteProProjectById(id);
    }
}
