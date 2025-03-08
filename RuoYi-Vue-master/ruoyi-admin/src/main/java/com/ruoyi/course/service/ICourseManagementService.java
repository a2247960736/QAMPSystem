package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.CourseManagement;

/**
 * 课程管理Service接口
 * 
 * @author dongjinrong
 * @date 2025-02-01
 */
public interface ICourseManagementService 
{
    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    public CourseManagement selectCourseManagementByCourseId(Long courseId);

    /**
     * 查询课程管理列表
     * 
     * @param courseManagement 课程管理
     * @return 课程管理集合
     */
    public List<CourseManagement> selectCourseManagementList(CourseManagement courseManagement);

    /**
     * 新增课程管理
     * 
     * @param courseManagement 课程管理
     * @return 结果
     */
    public int insertCourseManagement(CourseManagement courseManagement);

    /**
     * 修改课程管理
     * 
     * @param courseManagement 课程管理
     * @return 结果
     */
    public int updateCourseManagement(CourseManagement courseManagement);

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键集合
     * @return 结果
     */
    public int deleteCourseManagementByCourseIds(Long[] courseIds);

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    public int deleteCourseManagementByCourseId(Long courseId);
}
