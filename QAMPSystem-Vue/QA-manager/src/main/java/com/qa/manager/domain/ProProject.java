package com.qa.manager.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "项目管理对象")
public class ProProject extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    /** 业务ID */
    @Excel(name = "业务ID")
    @ApiModelProperty(value = "业务ID", example = "PROJ001")
    private String projectId;

    /** 所属部门ID（引用sys_dept.dept_id） */
    @Excel(name = "所属部门ID", readConverterExp = "引=用sys_dept.dept_id")
    @ApiModelProperty(value = "所属部门ID（引用sys_dept.dept_id）", example = "1001")
    private Long deptId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    @ApiModelProperty(value = "项目名称", example = "示例项目")
    private String projectName;

    /** 创建者ID */
    @Excel(name = "创建者ID")
    @ApiModelProperty(value = "创建者ID", example = "2001")
    private Long creatorId;

    /** 需求总数 */
    @Excel(name = "需求总数")
    @ApiModelProperty(value = "需求总数", example = "10")
    private Long requirementCount;

    /** 成员数 */
    @Excel(name = "成员数")
    @ApiModelProperty(value = "成员数", example = "5")
    private Long memberCount;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setRequirementCount(Long requirementCount) {
        this.requirementCount = requirementCount;
    }

    public Long getRequirementCount() {
        return requirementCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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