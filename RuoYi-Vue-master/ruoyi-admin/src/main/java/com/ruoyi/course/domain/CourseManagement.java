package com.ruoyi.course.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程管理对象 course_management
 * 
 * @author dongjinrong
 * @date 2025-02-01
 */
public class CourseManagement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程id */
    private Long courseId;

    /** 课程编码 */
    @Excel(name = "课程编码")
    private String courseCode;

    /** 学科 */
    @Excel(name = "学科")
    private String courseSubject;

    /** 课程名字 */
    @Excel(name = "课程名字")
    private String courseName;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 适用人群 */
    @Excel(name = "适用人群")
    private String targetAudience;

    /** 课程介绍 */
    @Excel(name = "课程介绍")
    private String courseDescription;

    /** 上架时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上架时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }
    public void setCourseCode(String courseCode) 
    {
        this.courseCode = courseCode;
    }

    public String getCourseCode() 
    {
        return courseCode;
    }
    public void setCourseSubject(String courseSubject) 
    {
        this.courseSubject = courseSubject;
    }

    public String getCourseSubject() 
    {
        return courseSubject;
    }
    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setTargetAudience(String targetAudience) 
    {
        this.targetAudience = targetAudience;
    }

    public String getTargetAudience() 
    {
        return targetAudience;
    }
    public void setCourseDescription(String courseDescription) 
    {
        this.courseDescription = courseDescription;
    }

    public String getCourseDescription() 
    {
        return courseDescription;
    }
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }
    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("courseId", getCourseId())
            .append("courseCode", getCourseCode())
            .append("courseSubject", getCourseSubject())
            .append("courseName", getCourseName())
            .append("price", getPrice())
            .append("targetAudience", getTargetAudience())
            .append("courseDescription", getCourseDescription())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
