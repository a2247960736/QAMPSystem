package com.qa.manager.domain;

import com.qa.common.core.domain.entity.SysUser;
import lombok.Data;

/**
 * pro_requirement 和 pro_project 关联
 *
 * @author ruoyi
 * @date 2025-03-09
 */
@Data
public class ProRequirementVo  extends ProRequirement{

    //项目信息
    private ProProject proProject;

    //用户信息
    private SysUser sysUser;

    //需求总数
    private int reqCount;



}
