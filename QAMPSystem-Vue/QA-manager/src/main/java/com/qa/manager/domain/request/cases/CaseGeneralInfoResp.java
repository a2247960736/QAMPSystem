package com.qa.manager.domain.request.cases;

import lombok.Data;

@Data
public class CaseGeneralInfoResp {

    // 添加无参构造器
    public CaseGeneralInfoResp() {
    }

    private Long id;

    private String title;

    private String planId;

    private Long productLineId;
}
