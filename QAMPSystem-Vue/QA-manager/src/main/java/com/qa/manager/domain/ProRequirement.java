package com.qa.manager.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 需求管理对象 pro_requirement
 *
 * @author ruoyi
 * @date 2025-03-09
 */
@ApiModel(description = "需求管理对象，用于记录项目中的需求信息")
public class ProRequirement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "技术主键", example = "1", notes = "该需求记录的唯一标识")
    /** 技术主键 */
    private Long id;

    @ApiModelProperty(value = "业务ID", example = "REQ-001", notes = "业务ID，格式为：REQ-001")
    @Excel(name = "业务ID", readConverterExp = "格=式：REQ-001")
    /** 业务ID（格式：REQ-001） */
    private String reqId;

    @ApiModelProperty(value = "所属项目技术ID", example = "100", notes = "引用 pro_project 表的 id，表示该需求所属的项目")
    @Excel(name = "所属项目技术ID", readConverterExp = "引=用pro_project.id")
    /** 所属项目技术ID（引用pro_project.id） */
    private Long projectId;

    @ApiModelProperty(value = "需求标题", example = "系统性能优化需求", notes = "需求的标题描述")
    @Excel(name = "需求标题")
    /** 需求标题 */
    private String title;

    @ApiModelProperty(value = "优先级", example = "高", notes = "需求的优先级，如高、中、低等")
    @Excel(name = "优先级")
    /** 优先级 */
    private String priority;

    @ApiModelProperty(value = "需求状态", example = "待处理", notes = "需求当前的状态，如待处理、处理中、已完成等")
    @Excel(name = "需求状态")
    /** 需求状态 */
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("reqId", getReqId())
                .append("projectId", getProjectId())
                .append("title", getTitle())
                .append("priority", getPriority())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .toString();
    }
}