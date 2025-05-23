package com.qa.manager.domain.request.record;

import lombok.Data;

import java.util.Date;

/**
 * 脑图 - 执行任务上方的概览信息
 *
 * @author jinrong.dong
 * @date 2025/03/24
 */
@Data
public class RecordGeneralInfoResp {
    // 添加无参构造器
    public RecordGeneralInfoResp() {
    }

    private Long id;

    private Long caseId;

    private String title;

    /**
     * 预计周期
     */
    private Date expectStartTime;

    private Date expectEndTime;

    /**
     * 需求id
     */
    private String requirementIds;

    /**
     * 用例执行数
     */
    private int passCount;

    /**
     * 用例总数
     */
    private int totalCount;

    /**
     * 用例通过数
     */
    private int successCount;

    /**
     * 用例失败数
     */
    private int bugNum;

    /**
     * 用例阻塞数
     */
    private int blockCount;

    /**
     * 忽略个数
     */
    private int ignoreCount;

    /**
     * 通过率
     */
    private double passRate;
}
