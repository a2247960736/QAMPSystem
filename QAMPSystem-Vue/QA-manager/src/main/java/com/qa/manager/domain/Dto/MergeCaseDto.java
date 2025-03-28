package com.qa.manager.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MergeCaseDto {
    // 添加无参构造器
    public MergeCaseDto() {
    }

    private Long caseId;

    private String chooseContent;

    private String recordContent;

    private Integer env;

    private Long recordId;
}