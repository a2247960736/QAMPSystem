package com.qa.manager.domain.request.dir;

import com.qa.common.constant.BizConstant;
import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import lombok.Data;

@Data
public class DirMoveReq implements ParamValidate {
    private Integer channel;

    private Long productLineId;

    /**
     * 被选中文件夹的id
     */
    private String fromId;

    /**
     * 如果想移到同级，设置为选中文件夹的parentId
     * 如果想要移到自己，设为选中文件夹的Id
     */
    private String toId;

    // 添加无参构造器
    public DirMoveReq() {
    }

    @Override
    public void validate() {
        if (productLineId == null || productLineId <= 0) {
            throw new IllegalArgumentException("业务线id为空或者非法");
        }
        if (StringUtils.isEmpty(fromId) || StringUtils.isEmpty(toId)) {
            throw new IllegalArgumentException("来源或迁移文件夹id不能为空");
        }
        if (BizConstant.ROOT_BIZ_ID.equals(fromId)) {
            throw new IllegalArgumentException("根文件夹暂不支持迁移");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("相同的文件夹不需要迁移");
        }
    }
}