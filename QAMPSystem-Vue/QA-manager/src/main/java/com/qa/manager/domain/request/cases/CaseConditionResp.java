package com.qa.manager.domain.request.cases;

import lombok.Data;

import java.util.Set;

@Data
public class CaseConditionResp {

    // 添加无参构造器
    public CaseConditionResp() {
    }

    /**
     * 符合条件的用例数
     */
    private Integer count;

    /**
     * 所有用例数
     */
    private Integer totalCount;

    /**
     * 有哪些resource标签
     */
    private Set<String> taglist;

}