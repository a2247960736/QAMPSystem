package com.qa.manager.service;

import java.util.List;
import com.qa.manager.domain.PersonalTask;
import org.apache.ibatis.annotations.Param;

/**
 * 个人任务管理Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-12
 */
public interface IPersonalTaskService 
{
    /**
     * 查询个人任务管理
     * 
     * @param id 个人任务管理主键
     * @return 个人任务管理
     */
    public PersonalTask selectPersonalTaskById(Long id);

    /**
     * 查询个人任务管理列表
     * 
     * @param personalTask 个人任务管理
     * @return 个人任务管理集合
     */
    public List<PersonalTask> selectPersonalTaskList(PersonalTask personalTask);

    /**
     * 新增个人任务管理
     * 
     * @param personalTask 个人任务管理
     * @return 结果
     */
    public int insertPersonalTask(PersonalTask personalTask);

    /**
     * 修改个人任务管理
     * 
     * @param personalTask 个人任务管理
     * @return 结果
     */
    public int updatePersonalTask(PersonalTask personalTask);

    /**
     * 批量删除个人任务管理
     * 
     * @param ids 需要删除的个人任务管理主键集合
     * @return 结果
     */
    public int deletePersonalTaskByIds(Long[] ids);

    /**
     * 删除个人任务管理信息
     * 
     * @param id 个人任务管理主键
     * @return 结果
     */
    public int deletePersonalTaskById(Long id);

    /**
     * 查询逾期任务列表
     * @return 逾期任务列表
     */
    public List<PersonalTask> selectOverdueTasksList(PersonalTask personalTask);

    /**
     * 批量更新任务状态
     * @param ids 任务ID列表
     * @param status 新的任务状态
     * @return 更新的记录数
     * @param ids
     * @param status
     * @return
     */
    public int batchUpdateStatus(@Param("ids") List<Long> ids,
                                 @Param("status") String status);
}
