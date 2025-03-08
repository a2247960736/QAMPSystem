package com.ruoyi.course.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.course.mapper.CourseManagementMapper;
import com.ruoyi.course.domain.CourseManagement;
import com.ruoyi.course.service.ICourseManagementService;

/**
 * 课程管理Service业务层处理
 * 
 * @author dongjinrong
 * @date 2025-02-01
 */
@Service
public class CourseManagementServiceImpl implements ICourseManagementService 
{
    @Autowired
    private CourseManagementMapper courseManagementMapper;

    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    @Override
    public CourseManagement selectCourseManagementByCourseId(Long courseId)
    {
        return courseManagementMapper.selectCourseManagementByCourseId(courseId);
    }

    /**
     * 查询课程管理列表
     * 
     * @param courseManagement 课程管理
     * @return 课程管理
     */
    @Override
    public List<CourseManagement> selectCourseManagementList(CourseManagement courseManagement)
    {
        return courseManagementMapper.selectCourseManagementList(courseManagement);
    }

    /**
     * 新增课程管理
     * 
     * @param courseManagement 课程管理
     * @return 结果
     */
    @Override
    public int insertCourseManagement(CourseManagement courseManagement)
    {
        return courseManagementMapper.insertCourseManagement(courseManagement);
    }

    /**
     * 修改课程管理
     * 
     * @param courseManagement 课程管理
     * @return 结果
     */
    @Override
    public int updateCourseManagement(CourseManagement courseManagement)
    {
        return courseManagementMapper.updateCourseManagement(courseManagement);
    }

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseManagementByCourseIds(Long[] courseIds)
    {
        return courseManagementMapper.deleteCourseManagementByCourseIds(courseIds);
    }

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseManagementByCourseId(Long courseId)
    {
        return courseManagementMapper.deleteCourseManagementByCourseId(courseId);
    }
}
