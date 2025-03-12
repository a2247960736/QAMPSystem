package com.qa.manager.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 项目成员对象 project_member
 *
 * @author jinrong.dong
 * @date 2025-03-11
 */
@ApiModel(description = "项目成员对象，记录项目与用户的关联关系")
public class ProjectMember extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "技术主键", example = "1", notes = "该记录的唯一标识")
    /** 技术主键 */
    private Long id;

    @ApiModelProperty(value = "项目技术主键", example = "100", notes = "引用 pro_project 表的 id，表示所属项目", dataType = "java.lang.Long")
    @Excel(name = "项目技术主键", readConverterExp = "引=用pro_project.id")
    /** 项目技术主键（引用pro_project.id） */
    private Long projectId;

    @ApiModelProperty(value = "用户ID", example = "200", notes = "引用 sys_user 表的 user_id，表示关联的用户", dataType = "java.lang.Long")
    @Excel(name = "用户ID", readConverterExp = "引=用sys_user.user_id")
    /** 用户ID（引用sys_user.user_id） */
    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("projectId", getProjectId())
                .append("userId", getUserId())
                .append("createTime", getCreateTime())
                .toString();
    }
}