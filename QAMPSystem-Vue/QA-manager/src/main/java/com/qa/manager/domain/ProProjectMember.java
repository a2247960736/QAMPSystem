package com.qa.manager.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ProProjectMember {
    private Long id;
    private Long projectId;
    private Long userId;
    private String role;
    private Date createTime;
}