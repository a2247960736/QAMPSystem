package com.qa.manager.domain.Vo;

import com.qa.manager.domain.ProProject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 16:25
 */
@ApiModel(value = "ProProjectVo", description = "项目信息视图对象，包含额外的项目相关信息")
@Data
public class ProProjectVo extends ProProject {

    @ApiModelProperty(value = "创建者姓名", example = "张三")
    //用户信息
    private String creatorName;

    @ApiModelProperty(value = "需求总数", example = "10")
    //需求总数
    private Integer reqCount;

    @ApiModelProperty(value = "项目成员数量", example = "5")
    //项目成员数量
    private Integer calculatedMemberCount;

    @ApiModelProperty(value = "项目成员的用户 ID 列表", example = "[1, 2, 3]")
    //项目成员信息
    private List<Long> userIds;
}