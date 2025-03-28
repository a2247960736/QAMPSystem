package com.qa.manager.domain.request.record;

import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增执行记录的请求参数类
 */
@ApiModel(description = "新增执行记录的请求参数")
@Data
public class RecordAddReq implements ParamValidate {

    // 添加无参构造器
    public RecordAddReq() {
    }

    /**
     * 用例集 ID
     */
    @ApiModelProperty(value = "用例集 ID，不能为空且必须大于 0", required = true)
    private Long caseId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人，不能为空", required = true)
    private String creator;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题，不能为空", required = true)
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

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    private String owner;

    @Override
    public void validate() {
        if (caseId == null || caseId <= 0) {
            throw new IllegalArgumentException("用例id为空或者非法");
        }
        if (StringUtils.isEmpty(creator)) {
            throw new IllegalArgumentException("创建人为空");
        }
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("标题为空");
        }
        if (expectStartTime != null && expectEndTime == null || expectStartTime == null && expectEndTime != null) {
            throw new IllegalArgumentException("计划周期时间区域不完整");
        }
        if (StringUtils.isEmpty(chooseContent)) {
            throw new IllegalArgumentException("圈选用例为空");
        }
    }
}