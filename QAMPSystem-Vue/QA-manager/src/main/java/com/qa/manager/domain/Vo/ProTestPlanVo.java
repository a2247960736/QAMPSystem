package com.qa.manager.domain.Vo;

import com.qa.manager.domain.ProTestPlan;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-16
 * Time: 10:15
 */
@Data
public class ProTestPlanVo extends ProTestPlan {
    /** 需求标题 */
    private String title;

    //提测人员名称
    private String devName;

    //测试负责人名称
    private String testName;
}
