package com.qa.manager.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.qa.common.annotation.Excel;
import com.qa.common.core.domain.BaseEntity;

/**
 * 文件夹对象 biz
 * 一个业务线有一条自己的文件夹数据，这里采用json去存储
 *
 * @author jinrong.dong
 * @date 2025-03-22
 */
public class Biz extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件夹主键 */
    private Long id;

    /** 业务线名称 */
    @Excel(name = "业务线名称")
    private Long productLineId;

    /** 文件数内容 */
    @Excel(name = "文件数内容")
    private String content;

    /** 渠道 */
    @Excel(name = "渠道")
    private Integer channel;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Integer isDelete;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreated;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtModified;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProductLineId(Long productLineId) 
    {
        this.productLineId = productLineId;
    }

    public Long getProductLineId() 
    {
        return productLineId;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setChannel(Integer channel) 
    {
        this.channel = channel;
    }

    public Integer getChannel() 
    {
        return channel;
    }

    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productLineId", getProductLineId())
            .append("content", getContent())
            .append("channel", getChannel())
            .append("isDelete", getIsDelete())
            .append("gmtCreated", getGmtCreated())
            .append("gmtModified", getGmtModified())
            .toString();
    }
}
