package com.qa.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 测试用例对象 test_case
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
public class TestCase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 用例集id */
    @Excel(name = "用例集id")
    private Long groupId;

    /** 用例名称 */
    @Excel(name = "用例名称")
    private String title;

    /** 用例描述 */
    @Excel(name = "用例描述")
    private String description;

    /** 用例状态 0-正常 1-删除 */
    @Excel(name = "用例状态 0-正常 1-删除")
    private Integer isDelete;

    /** 用例创建人 */
    @Excel(name = "用例创建人")
    private String creator;

    /** 用例修改人 */
    @Excel(name = "用例修改人")
    private String modifier;

    /** 用例内容 */
    @Excel(name = "用例内容")
    private String caseContent;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreated;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtModified;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extra;

    /** 业务线id 默认0 */
    @Excel(name = "业务线id 默认0")
    private Long productLineId;

    /** 0-需求用例，1-核心用例，2-冒烟用例 */
    @Excel(name = "0-需求用例，1-核心用例，2-冒烟用例")
    private Long caseType;

    /** 模块节点id */
    @Excel(name = "模块节点id")
    private Long moduleNodeId;

    /** 测试计划id */
    @Excel(name = "测试计划id")
    private String planId;

    /** 冒烟case的id */
    @Excel(name = "冒烟case的id")
    private Long smkCaseId;

    /** 渠道标志 现默认1 */
    @Excel(name = "渠道标志 现默认1")
    private Integer channel;

    /** 关联的文件夹id */
    @Excel(name = "关联的文件夹id")
    private String bizId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setCreator(String creator) 
    {
        this.creator = creator;
    }

    public String getCreator() 
    {
        return creator;
    }

    public void setModifier(String modifier) 
    {
        this.modifier = modifier;
    }

    public String getModifier() 
    {
        return modifier;
    }

    public void setCaseContent(String caseContent) 
    {
        this.caseContent = caseContent;
    }

    public String getCaseContent() 
    {
        return caseContent;
    }

    public void setGmtCreated(Date gmtCreated) 
    {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtCreated() 
    {
        return gmtCreated;
    }

    public void setGmtModified(Date gmtModified) 
    {
        this.gmtModified = gmtModified;
    }

    public Date getGmtModified() 
    {
        return gmtModified;
    }

    public void setExtra(String extra) 
    {
        this.extra = extra;
    }

    public String getExtra() 
    {
        return extra;
    }

    public void setProductLineId(Long productLineId) 
    {
        this.productLineId = productLineId;
    }

    public Long getProductLineId() 
    {
        return productLineId;
    }

    public void setCaseType(Long caseType) 
    {
        this.caseType = caseType;
    }

    public Long getCaseType() 
    {
        return caseType;
    }

    public void setModuleNodeId(Long moduleNodeId) 
    {
        this.moduleNodeId = moduleNodeId;
    }

    public Long getModuleNodeId() 
    {
        return moduleNodeId;
    }

    public void setPlanId(String planId) 
    {
        this.planId = planId;
    }

    public String getPlanId() 
    {
        return planId;
    }

    public void setSmkCaseId(Long smkCaseId) 
    {
        this.smkCaseId = smkCaseId;
    }

    public Long getSmkCaseId() 
    {
        return smkCaseId;
    }

    public void setChannel(Integer channel)
    {
        this.channel = channel;
    }

    public Integer getChannel()
    {
        return channel;
    }

    public void setBizId(String bizId) 
    {
        this.bizId = bizId;
    }

    public String getBizId() 
    {
        return bizId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("isDelete", getIsDelete())
            .append("creator", getCreator())
            .append("modifier", getModifier())
            .append("caseContent", getCaseContent())
            .append("gmtCreated", getGmtCreated())
            .append("gmtModified", getGmtModified())
            .append("extra", getExtra())
            .append("productLineId", getProductLineId())
            .append("caseType", getCaseType())
            .append("moduleNodeId", getModuleNodeId())
            .append("planId", getPlanId())
            .append("smkCaseId", getSmkCaseId())
            .append("channel", getChannel())
            .append("bizId", getBizId())
            .toString();
    }
}
