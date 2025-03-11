package com.qa.manager.domain.Vo;

import com.qa.common.core.domain.entity.SysUser;
import com.qa.manager.domain.ProProject;
import lombok.Data;

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
    private String nickName;

    //需求总数
    private int reqCount;

}
