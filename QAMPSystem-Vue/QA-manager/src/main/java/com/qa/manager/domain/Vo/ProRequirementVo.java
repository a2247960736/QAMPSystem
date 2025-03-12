package com.qa.manager.domain.Vo;

import com.qa.common.core.domain.entity.SysUser;
import com.qa.manager.domain.ProProject;
import com.qa.manager.domain.ProRequirement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * pro_requirement 和 pro_project 关联
 *
 * @author ruoyi
 * @date 2025-03-09
 */
@ApiModel(description = "需求与项目关联视图对象，包含需求信息以及关联的项目信息")
@Data
public class ProRequirementVo extends ProRequirement {

    @ApiModelProperty(value = "项目信息", notes = "该需求所关联的项目的详细信息")
    //项目信息
    private ProProject proProject;
}