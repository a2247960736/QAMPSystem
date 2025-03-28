package com.qa.manager.domain.request.cases;

import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import lombok.Data;

@Data
public class WsSaveReq implements ParamValidate {

    /**
     * 传进来的是当前脑图的caseContent
     * 可能是record的也可能是testcase的
     */
    private String caseContent;

    private Long id;

    private String modifier;

    /**
     * 如果是用例页面 则传进来是null
     * 如果是任务页面 则传进来具体的任务id
     */
    private Long recordId;

    // 添加无参构造器
    public WsSaveReq() {
    }

    @Override
    public void validate() {
        if (StringUtils.isEmpty(caseContent)) {
            throw new IllegalArgumentException("保存的内容为空");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("用例id为空");
        }
        
    }
}