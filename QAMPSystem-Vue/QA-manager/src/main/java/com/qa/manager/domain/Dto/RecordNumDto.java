package com.qa.manager.domain.Dto;

import lombok.Data;

@Data
public class RecordNumDto {

    /**
     * 所属用例id
     */
    Long caseId;

    /**
     * 任务数量
     */
    Integer recordNum;

}