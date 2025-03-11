package com.qa.manager.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 项目管理对象 pro_project
 * 
 * @author jinrong.dong
 * @date 2025-03-09
 */
public class ProProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 业务ID */
    @Excel(name = "业务ID")
    private String projectId;

    /** 所属部门ID（引用sys_dept.dept_id） */
    @Excel(name = "所属部门ID", readConverterExp = "引=用sys_dept.dept_id")
    private Long deptId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 创建者ID */
    @Excel(name = "创建者ID")
    private Long creatorId;

    /** 需求总数 */
    @Excel(name = "需求总数")
    private Long requirementCount;

    /** 成员数 */
    @Excel(name = "成员数")
    private Long memberCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProjectId(String projectId) 
    {
        this.projectId = projectId;
    }

    public String getProjectId() 
    {
        return projectId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setCreatorId(Long creatorId) 
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() 
    {
        return creatorId;
    }

    public void setRequirementCount(Long requirementCount) 
    {
        this.requirementCount = requirementCount;
    }

    public Long getRequirementCount() 
    {
        return requirementCount;
    }

    public void setMemberCount(Long memberCount) 
    {
        this.memberCount = memberCount;
    }

    public Long getMemberCount() 
    {
        return memberCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("deptId", getDeptId())
            .append("projectName", getProjectName())
            .append("creatorId", getCreatorId())
            .append("createTime", getCreateTime())
            .append("requirementCount", getRequirementCount())
            .append("memberCount", getMemberCount())
            .toString();
    }
}
