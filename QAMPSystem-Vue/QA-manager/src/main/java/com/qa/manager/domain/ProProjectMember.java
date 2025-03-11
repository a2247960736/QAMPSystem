package com.qa.manager.domain;

import lombok.Data;

import java.util.Date;

/**
 * 项目成员关联实体
 * 描述pro_project与sys_user之间的多对多关系
 */
@Data
public class ProProjectMember {
    /** 主键ID */
    private Long id;

    /** 关联项目ID（对应pro_project.id） */
    private Long projectId;

    /** 关联用户ID（对应sys_user.user_id） */
    private Long userId;

    /** 成员角色：MEMBER-普通成员，ADMIN-管理员 */
    private String role;

    /** 加入时间 */
    private Date createTime;
}
