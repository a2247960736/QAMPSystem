package com.qa.manager.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.qa.common.exception.ServiceException;
import com.qa.common.utils.DateUtils;
import com.qa.manager.domain.Vo.ProjectMemberVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.ProjectMemberMapper;
import com.qa.manager.domain.ProjectMember;
import com.qa.manager.service.IProjectMemberService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目成员Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-11
 */
@Service
public class ProjectMemberServiceImpl implements IProjectMemberService 
{
    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    /**
     * 查询项目成员
     * 
     * @param id 项目成员主键
     * @return 项目成员
     */
    @Override
    public ProjectMember selectProjectMemberById(Long id)
    {
        return projectMemberMapper.selectProjectMemberById(id);
    }

    /**
     * 查询项目成员列表
     * 
     * @param projectMember 项目成员
     * @return 项目成员
     */
    @Override
    public List<ProjectMember> selectProjectMemberList(ProjectMember projectMember)
    {
        return projectMemberMapper.selectProjectMemberList(projectMember);
    }

    /**
     * 新增项目成员
     * 
     * @param projectMember 项目成员
     * @return 结果
     */
    @Override
    public int insertProjectMember(ProjectMember projectMember)
    {
        projectMember.setCreateTime(DateUtils.getNowDate());
        return projectMemberMapper.insertProjectMember(projectMember);
    }

    /**
     * 修改项目成员
     * 
     * @param projectMember 项目成员
     * @return 结果
     */
    @Override
    public int updateProjectMember(ProjectMember projectMember)
    {
        return projectMemberMapper.updateProjectMember(projectMember);
    }

    /**
     * 批量删除项目成员
     * 
     * @param ids 需要删除的项目成员主键
     * @return 结果
     */
    @Override
    public int deleteProjectMemberByIds(Long[] ids)
    {
        return projectMemberMapper.deleteProjectMemberByIds(ids);
    }

    /**
     * 删除项目成员信息
     * 
     * @param id 项目成员主键
     * @return 结果
     */
    @Override
    public int deleteProjectMemberById(Long id)
    {
        return projectMemberMapper.deleteProjectMemberById(id);
    }


    /**
     * 查询项目成员列表和成员名字
     *
     * @param projectMember 项目成员
     * @return 项目成员和成员名字集合
     */
    @Override
    public List<ProjectMemberVo> selectProjectMemberVoList(ProjectMember projectMember){

        return projectMemberMapper.selectProjectMemberVoList(projectMember);
    }

    /**
     * 批量插入项目成员
     *
     * @param projectId 关联项目ID
     * @param userIds   邀请用户ID
     * @return 结果
     */
    @Transactional
    @Override
    public int batchInsertProjectMembers(Long projectId, List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new ServiceException("请选择要添加的成员");
        }

        List<ProjectMember> members = userIds.stream()
                .map(userId -> {
                    ProjectMember pm = new ProjectMember();
                    pm.setProjectId(projectId);
                    pm.setUserId(userId);
                    return pm;
                }).collect(Collectors.toList());

        return projectMemberMapper.batchInsertProjectMembers(members);
    }
}
