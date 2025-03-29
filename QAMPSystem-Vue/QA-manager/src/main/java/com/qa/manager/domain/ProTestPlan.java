package com.qa.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 测试计划对象 pro_test_plan
 * 
 * @author jinrong.dong
 * @date 2025-03-14
 */
public class ProTestPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 技术主键 */
    private Long id;

    /** 计划业务ID（格式：PLAN-001） */
    @Excel(name = "计划业务ID", readConverterExp = "格=式：PLAN-001")
    private String planId;

    /** 关联需求ID */
    @Excel(name = "关联需求ID")
    private Long reqId;

    /** 计划名称 */
    @Excel(name = "计划名称")
    private String planName;

    /** 提测人员ID */
    @Excel(name = "提测人员ID")
    private Long submitterId;

    /** 测试负责人ID */
    @Excel(name = "测试负责人ID")
    private Long leaderId;

    /** 抄送邮箱（多个用逗号分隔） */
    @Excel(name = "抄送邮箱", readConverterExp = "多=个用逗号分隔")
    private String ccEmails;

    /** 计划状态 */
    @Excel(name = "计划状态")
    private String status;

    /** 提测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submitTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPlanId(String planId) 
    {
        this.planId = planId;
    }

    public String getPlanId() 
    {
        return planId;
    }

    public void setReqId(Long reqId) 
    {
        this.reqId = reqId;
    }

    public Long getReqId() 
    {
        return reqId;
    }

    public void setPlanName(String planName) 
    {
        this.planName = planName;
    }

    public String getPlanName() 
    {
        return planName;
    }

    public void setSubmitterId(Long submitterId) 
    {
        this.submitterId = submitterId;
    }

    public Long getSubmitterId() 
    {
        return submitterId;
    }

    public void setLeaderId(Long leaderId) 
    {
        this.leaderId = leaderId;
    }

    public Long getLeaderId() 
    {
        return leaderId;
    }

    public void setCcEmails(String ccEmails) 
    {
        this.ccEmails = ccEmails;
    }

    public String getCcEmails() 
    {
        return ccEmails;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSubmitTime(Date submitTime) 
    {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime() 
    {
        return submitTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("reqId", getReqId())
            .append("planName", getPlanName())
            .append("submitterId", getSubmitterId())
            .append("leaderId", getLeaderId())
            .append("ccEmails", getCcEmails())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("submitTime", getSubmitTime())
            .toString();
    }
}
