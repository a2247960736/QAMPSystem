package com.qa.manager.domain.Vo;

import com.qa.common.core.domain.entity.SysUser;
import com.qa.manager.domain.ProProject;
import com.qa.manager.domain.ProRequirement;
import lombok.Data;

/**
 * pro_requirement 和 pro_project 关联
 *
 * @author ruoyi
 * @date 2025-03-09
 */
@Data
public class ProRequirementVo  extends ProRequirement {

    //项目信息
    private ProProject proProject;




}
