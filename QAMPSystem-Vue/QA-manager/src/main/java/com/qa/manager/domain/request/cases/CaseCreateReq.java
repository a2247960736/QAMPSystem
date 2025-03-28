package com.qa.manager.domain.request.cases;

import com.qa.common.constant.BizConstant;
import com.qa.common.constant.SystemConstant;
import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CaseCreateReq", description = "用例创建请求对象")
public class CaseCreateReq implements ParamValidate {

    /**
     * 必填 创建人
     */
    @ApiModelProperty(value = "创建人", required = true)
    private String creator;

    /**
     * 必填 业务线id
     */
    @ApiModelProperty(value = "业务线id", required = true)
    private Long productLineId;

    /**
     * 必填 用例种类 默认给0
     */
    @ApiModelProperty(value = "用例种类，默认给0", required = true)
    private Integer caseType;

    /**
     * 必填 用例初始化内容
     */
    @ApiModelProperty(value = "用例初始化内容", required = true)
    private String caseContent;

    /**
     * 必填 用例标题
     */
    @ApiModelProperty(value = "用例标题", required = true)
    private String title;

    /**
     * 必填 默认给1
     * 其实这里的channel意思就是渠道，用户在agileTc页面创建的就是1
     * 如果是其他自定义系统过来的，可以自定义
     */
    @ApiModelProperty(value = "渠道，默认给1", required = true)
    private Integer channel;

    /**
     * 必填 -- 但是表单不填，如果没有传默认给-1
     * 关联文件夹ids，现在无论done还是oe都默认给-1
     */
    @ApiModelProperty(value = "关联文件夹ids，表单可不填，没传时默认给-1，done和oe目前都默认给-1", required = true)
    private String bizId;

    /**
     * 非必填
     * 复制需要传id
     * 新建不需要传
     */
    @ApiModelProperty(value = "用例id，复制时需要传，新建时不需要传", required = false)
    private Long id;

    /**
     * 非必填 需求id
     */
    @ApiModelProperty(value = "需求id", required = false)
    private String planId;

    /**
     * 非必填 描述
     */
    @ApiModelProperty(value = "描述", required = false)
    private String description;

    @Override
    public void validate() {
        // 复制操作才需要id
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("所复制的用例id非法");
        }

        // 文件夹判断
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
        if (StringUtils.isEmpty(caseContent)) {
            throw new IllegalArgumentException("新建的用例内容为空");
        }



        // 如果没传，默认给空字符串，这样service层不需要特殊判断了
        if (StringUtils.isEmpty(planId)) {
            planId = SystemConstant.EMPTY_STR;
        }
        if (description == null) {
            description = SystemConstant.EMPTY_STR;
        }
    }
}