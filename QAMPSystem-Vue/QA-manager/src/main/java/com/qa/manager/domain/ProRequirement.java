package com.qa.manager.domain;

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
public class ProRequirement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 技术主键 */
    private Long id;

    /** 业务ID（格式：REQ-001） */
    @Excel(name = "业务ID", readConverterExp = "格=式：REQ-001")
    private String reqId;

    /** 所属项目技术ID（引用pro_project.id） */
    @Excel(name = "所属项目技术ID", readConverterExp = "引=用pro_project.id")
    private Long projectId;

    /** 需求标题 */
    @Excel(name = "需求标题")
    private String title;

    /** 优先级 */
    @Excel(name = "优先级")
    private String priority;

    /** 需求状态 */
    @Excel(name = "需求状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setReqId(String reqId) 
    {
        this.reqId = reqId;
    }

    public String getReqId() 
    {
        return reqId;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setPriority(String priority) 
    {
        this.priority = priority;
    }

    public String getPriority() 
    {
        return priority;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
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
