package com.qa.manager.domain.request.cases;

import lombok.Data;

@Data
public class CaseQueryReq {

    private Long id;

    private Integer caseType;

    private Long lineId;

    private String title;

    private String creator;

    private String planId;

    private String beginTime;

    private String endTime;

    private Integer channel;

    private String bizId;



    // 添加无参构造器
    public CaseQueryReq() {
    }

    public CaseQueryReq(Integer caseType, String title, String creator, String reqIds, String beginTime, String endTime, Integer channel, String bizId, Long lineId) {
        this.caseType = caseType;
        this.title = title;
        this.creator = creator;
        this.planId = reqIds;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.channel = channel;
        this.bizId = bizId;
        this.lineId = lineId;

    }
}