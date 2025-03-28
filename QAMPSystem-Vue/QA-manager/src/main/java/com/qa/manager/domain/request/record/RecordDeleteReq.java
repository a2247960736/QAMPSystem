package com.qa.manager.domain.request.record;

import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 记录删除请求类，用于封装删除记录所需的参数
 */
@ApiModel(description = "记录删除请求参数")
@Data
public class RecordDeleteReq implements ParamValidate {

    // 添加无参构造器
    public RecordDeleteReq() {
    }

    /**
     * 任务 ID
     */
    @ApiModelProperty(value = "任务 ID，不能为空且必须大于 0", required = true)
    private Long id;

    @Override
    public void validate() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("任务id为空或不正确");
        }
    }
}