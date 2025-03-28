package com.qa.manager.domain.request.cases;

import com.qa.common.annotation.Excel;
import com.qa.manager.domain.request.dir.BizListResp;
import lombok.Data;

import java.util.List;

@Data
public class CaseDetailResp {

    private Integer caseType;

    private String description;

    private Long id;

    private String modifier;

    private String planId;

    private String title;

    private Long productLineId;

    private List<BizListResp> biz;

    private Long groupId;

    /** 用例内容 */
    @Excel(name = "用例内容")
    private String caseContent;

    // 添加无参构造器
    public CaseDetailResp() {
    }

}
