package com.qa.manager.mapper;

import java.util.List;
import com.qa.manager.domain.ProjectMember;
import com.qa.manager.domain.Vo.ProjectMemberVo;
import org.apache.ibatis.annotations.Param;

/**
 * 项目成员Mapper接口
 * 
 * @author jinrong.dong
 * @date 2025-03-11
 */
public interface ProjectMemberMapper 
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
     * 删除项目成员
     * 
     * @param id 项目成员主键
     * @return 结果
     */
    public int deleteProjectMemberById(Long id);

    /**
     * 批量删除项目成员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectMemberByIds(Long[] ids);

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
     * @param members 项目成员集合
     * @return 结果
     */

    int batchInsertProjectMembers(@Param("list") List<ProjectMember> members);

}
