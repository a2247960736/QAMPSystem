package com.qa.manager.domain.Vo;

import com.qa.common.core.domain.entity.SysUser;
import com.qa.manager.domain.ProProject;
import com.qa.manager.domain.ProProjectMember;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 16:25
 */
@Data
public class ProProjectVo extends ProProject {
    //用户信息
    private String creatorName;

    //需求总数
    private Integer reqCount;

    //项目成员数量
    private Integer calculatedMemberCount;

    //项目成员信息
    private List<Long> userIds;

}
