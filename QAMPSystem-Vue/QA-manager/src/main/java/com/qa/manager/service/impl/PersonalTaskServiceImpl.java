package com.qa.manager.service.impl;

import java.util.List;
import com.qa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.PersonalTaskMapper;
import com.qa.manager.domain.PersonalTask;
import com.qa.manager.service.IPersonalTaskService;

/**
 * 个人任务管理Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-12
 */
@Service
public class PersonalTaskServiceImpl implements IPersonalTaskService 
{
    @Autowired
    private PersonalTaskMapper personalTaskMapper;

    /**
     * 查询个人任务管理
     * 
     * @param id 个人任务管理主键
     * @return 个人任务管理
     */
    @Override
    public PersonalTask selectPersonalTaskById(Long id)
    {
        return personalTaskMapper.selectPersonalTaskById(id);
    }

    /**
     * 查询个人任务管理列表
     * 
     * @param personalTask 个人任务管理
     * @return 个人任务管理
     */
    @Override
    public List<PersonalTask> selectPersonalTaskList(PersonalTask personalTask)
    {
        return personalTaskMapper.selectPersonalTaskList(personalTask);
    }

    /**
     * 新增个人任务管理
     * 
     * @param personalTask 个人任务管理
     * @return 结果
     */
    @Override
    public int insertPersonalTask(PersonalTask personalTask)
    {
        personalTask.setCreateTime(DateUtils.getNowDate());
        return personalTaskMapper.insertPersonalTask(personalTask);
    }

    /**
     * 修改个人任务管理
     * 
     * @param personalTask 个人任务管理
     * @return 结果
     */
    @Override
    public int updatePersonalTask(PersonalTask personalTask)
    {
        personalTask.setUpdateTime(DateUtils.getNowDate());
        return personalTaskMapper.updatePersonalTask(personalTask);
    }

    /**
     * 批量删除个人任务管理
     * 
     * @param ids 需要删除的个人任务管理主键
     * @return 结果
     */
    @Override
    public int deletePersonalTaskByIds(Long[] ids)
    {
        return personalTaskMapper.deletePersonalTaskByIds(ids);
    }

    /**
     * 删除个人任务管理信息
     * 
     * @param id 个人任务管理主键
     * @return 结果
     */
    @Override
    public int deletePersonalTaskById(Long id)
    {
        return personalTaskMapper.deletePersonalTaskById(id);
    }
}
