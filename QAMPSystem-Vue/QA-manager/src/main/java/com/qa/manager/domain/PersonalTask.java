package com.qa.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 个人任务管理对象 personal_task
 * 
 * @author jinrong.dong
 * @date 2025-03-12
 */
public class PersonalTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    private Long id;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务优先级 */
    @Excel(name = "任务优先级")
    private String priority;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private String status;

    /** 创建者ID（关联sys_user.user_id） */
    @Excel(name = "创建者ID", readConverterExp = "关=联sys_user.user_id")
    private Long creatorId;

    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 预估工时（小时） */
    @Excel(name = "预估工时", readConverterExp = "小=时")
    private Long estimatedHours;

    /** 实际工时（小时） */
    @Excel(name = "实际工时", readConverterExp = "小=时")
    private Long actualHours;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
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

    public void setCreatorId(Long creatorId) 
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() 
    {
        return creatorId;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setEstimatedHours(Long estimatedHours) 
    {
        this.estimatedHours = estimatedHours;
    }

    public Long getEstimatedHours() 
    {
        return estimatedHours;
    }

    public void setActualHours(Long actualHours) 
    {
        this.actualHours = actualHours;
    }

    public Long getActualHours() 
    {
        return actualHours;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskName", getTaskName())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("estimatedHours", getEstimatedHours())
            .append("actualHours", getActualHours())
            .toString();
    }
}
