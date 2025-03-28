package com.qa.manager.domain.request.dir;

import com.qa.common.constant.BizConstant;
import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirDeleteReq implements ParamValidate {

    private String parentId;

    private Long productLineId;

    private String delId;

    private Integer channel;

    // 添加无参构造器
    public DirDeleteReq() {
    }

    @Override
    public void validate() {
        if (productLineId == null || productLineId <= 0) {
            throw new IllegalArgumentException("业务线id为空或者非法");
        }
        if (StringUtils.isEmpty(delId)) {
            throw new IllegalArgumentException("要删除的文件夹id不能为空");
        }
        if (BizConstant.ROOT_BIZ_ID.equals(delId)) {
            throw new IllegalArgumentException("不能删除根文件夹");
        }
        if (StringUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("父文件夹id为空");
        }
        if (channel == null) {
            throw new IllegalArgumentException("渠道为空");
        }
    }
}