package com.qa.quartz.task;

import com.qa.common.constant.ProjConstants;
import com.qa.manager.domain.PersonalTask;
import com.qa.manager.service.IPersonalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.qa.common.utils.DateUtils.getNowDate;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-12
 * Time: 17:33
 */

/**
 * 定时任务：每天凌晨自动扫描超过预计完成时间的任务，并标记为逾期任务
 *
 * @author jinrong.dong
 */
@Component("dailyPersonTask")
public class DailyPersonTaskScheduled {
    @Autowired
    private IPersonalTaskService personalTaskService;

    public void dailyPersonTask() {
        //今天日期
        Date today = getNowDate();
        PersonalTask personalTask = new PersonalTask();
        personalTask.setUpdateTime(today);
        //查询预期的任务
        List<PersonalTask> personalTaskList = personalTaskService.selectOverdueTasksList(personalTask);

        if(personalTaskList.size() == 0) {
            //无逾期任务
            return;
        }
        //遍历逾期任务列表
        List<Long> ids = new ArrayList<>();
        for (PersonalTask task : personalTaskList) {
            //获取逾期任务的ID
            Long id = task.getId();
            ids.add(id);
        }
        //批量更新任务状态
        int result = personalTaskService.batchUpdateStatus(ids, ProjConstants.TASK_STATUS_OVERDUE);



    }
    
}
