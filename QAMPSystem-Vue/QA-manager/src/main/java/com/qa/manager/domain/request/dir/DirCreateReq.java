package com.qa.manager.domain.request.dir;

import com.qa.common.utils.StringUtils;
import com.qa.manager.domain.request.ParamValidate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirCreateReq implements ParamValidate {

    /**
     * 如果要给A文件夹添加同级，则取A的父级id
     * 如果要给A文件夹添加子级，则取A的当前id
     */
    private String parentId;

    private Long productLineId;

    private String text;

    private Integer channel;

    // 添加无参构造器
    public DirCreateReq() {
    }

    @Override
    public void validate() {
        if (StringUtils.isEmpty(parentId)) {
            throw new IllegalArgumentException("请选择正确的节点进行创建");
        }
        if (productLineId == null || productLineId <= 0) {
            throw new IllegalArgumentException("业务线id为空或者非法");
        }
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("文件夹名称不能为空");
        }
        if (channel == null) {
            throw new IllegalArgumentException("渠道为空");
        }
    }
}