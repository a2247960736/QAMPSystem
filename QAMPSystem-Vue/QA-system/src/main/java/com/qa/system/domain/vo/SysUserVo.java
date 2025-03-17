package com.qa.system.domain.vo;

import com.qa.common.core.domain.entity.SysUser;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-16
 * Time: 13:58
 */

public class SysUserVo extends SysUser {
    //是开发1 测试：2  获取所有用户0
    private String isDev = "0";


    //set get方法
    public String getIsDev() {
        return isDev;
    }

    public void setIsDev(String isDev) {
        this.isDev = isDev;
    }
}
