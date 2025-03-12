package com.qa.manager.service;

import java.util.List;
import com.qa.manager.domain.ProjectMember;
import com.qa.manager.domain.Vo.ProjectMemberVo;

/**
 * 项目成员Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-11
 */
public interface IProjectMemberService 
{
    /**
     * 查询项目成员
     * 
     * @param id 项目成员主键
     * @return 项目成员
     */
    public ProjectMember selectProjectMemberById(Long id);

    /**
     * 查询项目成员列表
     * 
     * @param projectMember 项目成员
     * @return 项目成员集合
     */
    public List<ProjectMember> selectProjectMemberList(ProjectMember projectMember);

    /**
     * 新增项目成员
     * 
     * @param projectMember 项目成员
     * @return 结果
     */
    public int insertProjectMember(ProjectMember projectMember);

    /**
     * 修改项目成员
     * 
     * @param projectMember 项目成员
     * @return 结果
     */
    public int updateProjectMember(ProjectMember projectMember);

    /**
     * 批量删除项目成员
     * 
     * @param ids 需要删除的项目成员主键集合
     * @return 结果
     */
    public int deleteProjectMemberByIds(Long[] ids);

    /**
     * 删除项目成员信息
     * 
     * @param id 项目成员主键
     * @return 结果
     */
    public int deleteProjectMemberById(Long id);


    /**
     * 查询项目成员列表和成员名字
     *
     * @param projectMember 项目成员
     * @return 项目成员和成员名字集合
     */
    public List<ProjectMemberVo> selectProjectMemberVoList(ProjectMember projectMember);

    /**
     * 批量插入项目成员
     *
     * @param projectId 关联项目ID
     * @param userIds   邀请用户ID
     * @return 结果
     */
    public int batchInsertProjectMembers(Long projectId, List<Long> userIds);
}
