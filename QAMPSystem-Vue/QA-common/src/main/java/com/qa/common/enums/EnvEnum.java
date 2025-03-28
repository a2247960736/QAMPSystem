package com.qa.common.enums;

public enum EnvEnum {
    // 枚举
    TestEnv(0, "测试环境"),
    PreEnv(1, "预发环境"),
    OnlineEnv(2, "线上环境"),
    TestQaEnv(3, "冒烟qa"),
    TestRdEnv(4, "冒烟rd");

    private Integer value;
    private String name;

    EnvEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}