package com.qa.manager.domain.Vo;

import com.qa.manager.domain.ProjectMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 18:27
 */
@ApiModel(description = "项目成员视图对象，继承自 ProjectMember 并扩展了成员名字信息")
@Data
public class ProjectMemberVo extends ProjectMember {

    @ApiModelProperty(value = "成员名字", example = "张三", notes = "项目成员的昵称")
    //成员名字
    private String nickName;
}