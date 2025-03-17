package com.qa.manager.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qa.common.constant.ProjConstants;
import com.qa.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.domain.PersonalTask;
import com.qa.manager.service.IPersonalTaskService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

import static com.qa.common.utils.DateUtils.getNowDate;

/**
 * 个人任务管理Controller
 *
 * @author jinrong.dong
 * @date 2025-03-12
 */
@Api(tags = "个人任务管理")
@RestController
@RequestMapping("/manager/task")
public class PersonalTaskController extends BaseController
{
    @Autowired
    private IPersonalTaskService personalTaskService;

    /**
     * 查询个人任务管理列表
     */
    @ApiOperation("查询个人任务管理列表")
    @PreAuthorize("@ss.hasPermi('manager:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam("个人任务查询条件") PersonalTask personalTask)
    {
        startPage();
        //限定：只能查询自己的任务
        personalTask.setCreatorId(getUserId());
        //扩展：判断用户角色是否是超级管理员 or 测试leader
        //如果是超级管理员，查询所有任务
        //如果是测试leader，查询当前用户部门下所有成员的任务 （需要扩展PersonalTask表，绑定成员name名字）
        List<PersonalTask> list = personalTaskService.selectPersonalTaskList(personalTask);
        return getDataTable(list);
    }

    /**
     * 导出个人任务管理列表
     */
    @ApiOperation("导出个人任务管理列表")
    @PreAuthorize("@ss.hasPermi('manager:task:export')")
    @Log(title = "个人任务管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@ApiParam("HTTP响应对象") HttpServletResponse response, @ApiParam("个人任务查询条件") PersonalTask personalTask)
    {
        List<PersonalTask> list = personalTaskService.selectPersonalTaskList(personalTask);
        ExcelUtil<PersonalTask> util = new ExcelUtil<PersonalTask>(PersonalTask.class);
        util.exportExcel(response, list, "个人任务管理数据");
    }

    /**
     * 获取个人任务管理详细信息
     */
    @ApiOperation("获取个人任务管理详细信息")
    @PreAuthorize("@ss.hasPermi('manager:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam("个人任务ID") @PathVariable("id") Long id)
    {
        return success(personalTaskService.selectPersonalTaskById(id));
    }

    /**
     * 新增个人任务管理
     */
    @ApiOperation("新增个人任务管理")
    @PreAuthorize("@ss.hasPermi('manager:task:add')")
    @Log(title = "个人任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam("个人任务信息") @RequestBody PersonalTask personalTask)
    {
        //设置创建者ID
        personalTask.setCreatorId(getUserId());
        //判断开始时间是否小于结束时间
        if (personalTask.getStartTime().compareTo(personalTask.getEndTime()) > 0) {
            return error("开始时间不能晚于结束时间");
        }
        return toAjax(personalTaskService.insertPersonalTask(personalTask));
    }

    /**
     * 修改个人任务管理
     */
    @ApiOperation("修改个人任务管理")
    @PreAuthorize("@ss.hasPermi('manager:task:edit')")
    @Log(title = "个人任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam("个人任务信息") @RequestBody PersonalTask personalTask)
    {
        //判断开始时间是否小于结束时间
        if (personalTask.getStartTime().compareTo(personalTask.getEndTime()) > 0) {
            throw new ServiceException("任务开始时间不能晚于结束时间");
        }
        //判断当前任务的状态是否是已完成
        PersonalTask personalTaskToStatus = personalTaskService.selectPersonalTaskById(personalTask.getId());
        String status = personalTaskToStatus.getStatus();
        if (status.equals(ProjConstants.TASK_STATUS_COMPLETED)) {
            throw new ServiceException("修改任务状态失败，已完成的任务无法修改！如需帮助请联系部门Leader");
        }
        personalTask.setUpdateTime(getNowDate());
        //如果将任务状态修改为已完成，计算实际工时
        if (personalTask.getStatus().equals(ProjConstants.TASK_STATUS_COMPLETED)) {
            //计算实际工时
            Date date = getNowDate();
            long actualHours = (date.getTime() - personalTask.getStartTime().getTime()) / (1000 * 60 * 60);
            personalTask.setActualHours(actualHours);
        }

        return toAjax(personalTaskService.updatePersonalTask(personalTask));
    }

    /**
     * 删除个人任务管理
     */
    @ApiOperation("删除个人任务管理")
    @PreAuthorize("@ss.hasPermi('manager:task:remove')")
    @Log(title = "个人任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam("个人任务ID数组") @PathVariable Long[] ids)
    {
        for (Long id : ids) {
            //判断当前任务的状态是否是已完成
            PersonalTask personalTaskToStatus = personalTaskService.selectPersonalTaskById(id);
            String status = personalTaskToStatus.getStatus();
            if (status.equals(ProjConstants.TASK_STATUS_COMPLETED)) {
                throw new ServiceException("删除失败！存在已完成的任务，无法删除！如需帮助请联系部门Leader");
            }
        }
        return toAjax(personalTaskService.deletePersonalTaskByIds(ids));
    }
}