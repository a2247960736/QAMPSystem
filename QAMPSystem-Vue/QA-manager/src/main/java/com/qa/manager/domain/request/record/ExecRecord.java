package com.qa.manager.domain.request.record;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 用例执行记录对象 exec_record
 *
 * @author jinrong.dong
 * @date 2025-03-24
 */
public class ExecRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 执行的用例id */
    @Excel(name = "执行的用例id")
    private Long caseId;

    /** 用例名称 */
    @Excel(name = "用例名称")
    private String title;

    /** 执行环境： 0、测试环境 1、预发环境 2.线上环境 3.冒烟qa 4.冒烟rd */
    @Excel(name = "执行环境： 0、测试环境 1、预发环境 2.线上环境 3.冒烟qa 4.冒烟rd")
    private Integer env;

    /** 任务执行内容 */
    @Excel(name = "任务执行内容")
    private String caseContent;

    /** 用例状态 0-正常 1-删除 */
    @Excel(name = "用例状态 0-正常 1-删除")
    private Integer isDelete;

    /** 执行个数 */
    @Excel(name = "执行个数")
    private Integer passCount;

    /** 需执行总个数 */
    @Excel(name = "需执行总个数")
    private Integer totalCount;

    /** 成功个数 */
    @Excel(name = "成功个数")
    private Integer successCount;

    /** 不执行个数 */
    @Excel(name = "不执行个数")
    private Integer ignoreCount;

    /** 阻塞个数 */
    @Excel(name = "阻塞个数")
    private Integer blockCount;

    /** 失败个数 */
    @Excel(name = "失败个数")
    private Integer failCount;

    /** 用例创建人 */
    @Excel(name = "用例创建人")
    private String creator;

    /** 用例修改人 */
    @Excel(name = "用例修改人")
    private String modifier;

    /** 执行人 */
    @Excel(name = "执行人")
    private String executors;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 圈选用例内容 */
    @Excel(name = "圈选用例内容")
    private String chooseContent;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreated;

    /** 记录修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtModified;

    /** 预计开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectStartTime;

    /** 预计结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectEndTime;

    /** 实际开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualStartTime;

    /** 实际结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualEndTime;

    /** 负责人 */
    @Excel(name = "负责人")
    private String owner;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setCaseId(Long caseId)
    {
        this.caseId = caseId;
    }

    public Long getCaseId()
    {
        return caseId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setEnv(Integer env)
    {
        this.env = env;
    }

    public Integer getEnv()
    {
        return env;
    }

    public void setCaseContent(String caseContent)
    {
        this.caseContent = caseContent;
    }

    public String getCaseContent()
    {
        return caseContent;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setPassCount(Integer passCount)
    {
        this.passCount = passCount;
    }

    public Integer getPassCount()
    {
        return passCount;
    }

    public void setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
    }

    public Integer getTotalCount()
    {
        return totalCount;
    }

    public void setSuccessCount(Integer successCount)
    {
        this.successCount = successCount;
    }

    public Integer getSuccessCount()
    {
        return successCount;
    }

    public void setIgnoreCount(Integer ignoreCount)
    {
        this.ignoreCount = ignoreCount;
    }

    public Integer getIgnoreCount()
    {
        return ignoreCount;
    }

    public void setBlockCount(Integer blockCount)
    {
        this.blockCount = blockCount;
    }

    public Integer getBlockCount()
    {
        return blockCount;
    }

    public void setFailCount(Integer failCount)
    {
        this.failCount = failCount;
    }

    public Integer getFailCount()
    {
        return failCount;
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

    public void setExecutors(String executors)
    {
        this.executors = executors;
    }

    public String getExecutors()
    {
        return executors;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setChooseContent(String chooseContent)
    {
        this.chooseContent = chooseContent;
    }

    public String getChooseContent()
    {
        return chooseContent;
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

    public void setExpectStartTime(Date expectStartTime)
    {
        this.expectStartTime = expectStartTime;
    }

    public Date getExpectStartTime()
    {
        return expectStartTime;
    }

    public void setExpectEndTime(Date expectEndTime)
    {
        this.expectEndTime = expectEndTime;
    }

    public Date getExpectEndTime()
    {
        return expectEndTime;
    }

    public void setActualStartTime(Date actualStartTime)
    {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualStartTime()
    {
        return actualStartTime;
    }

    public void setActualEndTime(Date actualEndTime)
    {
        this.actualEndTime = actualEndTime;
    }

    public Date getActualEndTime()
    {
        return actualEndTime;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return owner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("caseId", getCaseId())
                .append("title", getTitle())
                .append("env", getEnv())
                .append("caseContent", getCaseContent())
                .append("isDelete", getIsDelete())
                .append("passCount", getPassCount())
                .append("totalCount", getTotalCount())
                .append("successCount", getSuccessCount())
                .append("ignoreCount", getIgnoreCount())
                .append("blockCount", getBlockCount())
                .append("failCount", getFailCount())
                .append("creator", getCreator())
                .append("modifier", getModifier())
                .append("executors", getExecutors())
                .append("description", getDescription())
                .append("chooseContent", getChooseContent())
                .append("gmtCreated", getGmtCreated())
                .append("gmtModified", getGmtModified())
                .append("expectStartTime", getExpectStartTime())
                .append("expectEndTime", getExpectEndTime())
                .append("actualStartTime", getActualStartTime())
                .append("actualEndTime", getActualEndTime())
                .append("owner", getOwner())
                .toString();
    }
}
