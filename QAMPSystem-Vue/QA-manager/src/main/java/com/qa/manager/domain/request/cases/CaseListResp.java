package com.qa.manager.domain.request.cases;

import lombok.Data;

import java.util.Date;

@Data
public class CaseListResp {

    /**
     * 用例id
     */
    private Long id;

    /**
     * 用例标题
     */
    private String title;

    /**
     * 用例描述
     */
    private String description;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 业务线id
     */
    private Long productLineId;

    /**
     * 用例种类
     */
    private Integer caseType;

    /**
     * 需求id str
     */
    private String planId;

    /**
     * 渠道
     */
    private Integer channel;

    @Deprecated
    private Long groupId;

    /**
     * 每个用例下面的recordNum
     */
    private Integer recordNum;

    // 添加无参构造器
    public CaseListResp() {
    }

}