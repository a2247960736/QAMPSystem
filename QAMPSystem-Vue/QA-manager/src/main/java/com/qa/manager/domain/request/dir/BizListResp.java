package com.qa.manager.domain.request.dir;

import lombok.Data;

@Data
public class BizListResp {

    private String bizId;

    private String text;

    private boolean select;

    // 添加无参构造器
    public BizListResp() {
    }
}