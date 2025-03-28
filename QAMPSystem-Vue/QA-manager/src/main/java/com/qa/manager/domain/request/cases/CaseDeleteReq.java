package com.qa.manager.domain.request.cases;

import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用例删除请求类，用于封装删除用例所需的参数
 */
@ApiModel(description = "用例删除请求参数")
@Data
public class CaseDeleteReq implements ParamValidate {

    // 添加无参构造器
    public CaseDeleteReq() {
    }

    /**
     * 用例 ID
     */
    @ApiModelProperty(value = "用例 ID，不能为空且必须大于 0", required = true)
    private Long id;

    @Override
    public void validate() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("用例id为空");
        }
    }
}