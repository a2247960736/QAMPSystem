package com.qa.manager.domain.request.record;

import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 记录更新请求类，用于封装更新记录所需的参数
 */
@ApiModel(description = "记录更新请求参数")
@Data
public class RecordUpdateReq implements ParamValidate {

    // 添加无参构造器
    public RecordUpdateReq() {
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

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    private String owner;

    /**
     * 任务标题
     */
    @ApiModelProperty(value = "任务标题，不能为空", required = true)
    private String title;

    /**
     * 圈选用例内容
     */
    @ApiModelProperty(value = "圈选用例内容，不能为空", required = true)
    private String chooseContent;

    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述信息")
    private String description;

    /**
     * 预期结束时间
     */
    @ApiModelProperty(value = "预期结束时间，若设置预期开始时间，则此时间也必须设置，反之亦然")
    private Long expectEndTime;

    /**
     * 预期开始时间
     */
    @ApiModelProperty(value = "预期开始时间，若设置预期结束时间，则此时间也必须设置，反之亦然")
    private Long expectStartTime;

    @Override
    public void validate() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("任务id为空或不正确");
        }
        if (StringUtils.isEmpty(modifier)) {
            throw new IllegalArgumentException("修改人为空");
        }
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("任务标题为空");
        }
        if (expectStartTime != null && expectEndTime == null || expectStartTime == null && expectEndTime != null) {
            throw new IllegalArgumentException("计划周期时间区域不完整");
        }
        if (StringUtils.isEmpty(chooseContent)) {
            throw new IllegalArgumentException("圈选用例为空");
        }
    }
}