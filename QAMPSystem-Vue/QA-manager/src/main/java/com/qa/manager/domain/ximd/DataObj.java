package com.qa.manager.domain.ximd;

import lombok.Data;

@Data
public class DataObj {

    /**
     * 用例行为 阻塞、失败、成功、不执行
     */
    private Integer progress;

    /**
     * 用例标题
     */
    private String text;

    /**
     * 节点id
     */
    private String id;

    /**
     * 备注
     */
    private String note;

    /**
     * 超链接
     */
    private String hyperLink;

    /**
     * 超链接文本提示
     */
    private String hyperLinkTitle;

    public String getProgressStr() {
        return String.valueOf(progress);
    }
}
