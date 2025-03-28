package com.qa.manager.domain.request.cases;

import com.qa.common.constant.BizConstant;
import com.qa.common.constant.SystemConstant;
import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;

/**
 * 用例编辑请求类，用于封装编辑用例所需的参数
 */
@ApiModel(description = "用例编辑请求参数")
@Data
public class CaseEditReq implements ParamValidate {

    // 添加无参构造器
    public CaseEditReq() {
    }

    /**
     * 必填 用例id
     */
    @ApiModelProperty(value = "用例 ID，不能为空且必须大于 0", required = true)
    private Long id;

    /**
     * 必填 用例种类 默认给0
     */
    @ApiModelProperty(value = "用例种类，不能为空，默认值为 0", required = true)
    private Integer caseType;

    /**
     * 必填 用例标题
     */
    @ApiModelProperty(value = "用例标题，不能为空", required = true)
    private String title;

    /**
     * 必填 修改人
     */
    @ApiModelProperty(value = "修改人，不能为空", required = true)
    private String modifier;

    /**
     * 必填 文件夹id列表 默认-1
     */
    @ApiModelProperty(value = "文件夹 ID 列表，不能为空，默认值为 -1", required = true)
    private String bizId;

    /**
     * 必填 渠道 默认1
     */
    @ApiModelProperty(value = "渠道，不能为空，默认值为 1", required = true)
    private Integer channel;

    /**
     * 非必填 需求id列表
     */
    @ApiModelProperty(value = "需求 ID 列表，可为空")
    private String planId;

    /**
     * 非必填 描述
     */
    @ApiModelProperty(value = "描述信息，可为空")
    private String description;

    @Override
    public void validate() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("要修改的用例id为空或者非法");
        }
        if (StringUtils.isEmpty(modifier)) {
            throw new IllegalArgumentException("请传入修改人");
        }
        // oe需要关联文件夹
        if (StringUtils.isEmpty(bizId)) {
            throw new IllegalArgumentException("文件夹选择为空");
        } else {
            long count = Arrays.stream(bizId.split(SystemConstant.COMMA)).filter(BizConstant.ROOT_BIZ_ID::equals).count();
            if (count > 0) {
                throw new IllegalArgumentException("不可以在根文件夹下创建用例");
            }
        }
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("标题输入为空");
        }
        if (caseType == null) {
            throw new IllegalArgumentException("用例种类为空");
        }
        if (channel == null) {
            throw new IllegalArgumentException("渠道为空");
        }

        if (StringUtils.isEmpty(planId)) {
            planId = SystemConstant.EMPTY_STR;
        }
        if (description == null) {
            description = SystemConstant.EMPTY_STR;
        }
    }
}