package com.qa.manager.domain.request.record;

import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 脑图清理执行记录请求类，用于封装清理操作所需的参数
 */
@ApiModel(description = "脑图清理执行记录请求参数")
@Data
public class RecordWsClearReq implements ParamValidate {

    // 添加无参构造器
    public RecordWsClearReq() {
    }

    /**
     * 任务 ID
     */
    @ApiModelProperty(value = "任务 ID，不能为空且必须大于 0", required = true)
    private Long id;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人，不能为空", required = true)
    private String modifier;

    @Override
    public void validate() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("任务id为空或非法");
        }
        if (StringUtils.isEmpty(modifier)) {
            throw new IllegalArgumentException("修改人为空");
        }
    }
}