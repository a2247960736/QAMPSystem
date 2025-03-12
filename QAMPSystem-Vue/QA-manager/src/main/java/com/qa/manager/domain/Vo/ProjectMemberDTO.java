package com.qa.manager.domain.Vo;

import com.qa.manager.domain.ProjectMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 20:53
 */
@ApiModel(description = "项目成员数据传输对象，继承自 ProjectMember 并包含成员用户 ID 列表")
@Data
public class ProjectMemberDTO extends ProjectMember {

    @ApiModelProperty(value = "成员用户 ID 列表", example = "[1, 2, 3]", notes = "表示要添加到项目中的成员的用户 ID 集合")
    private List<Long> userIds;
}