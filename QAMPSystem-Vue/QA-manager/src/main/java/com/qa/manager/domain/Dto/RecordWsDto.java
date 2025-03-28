package com.qa.manager.domain.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecordWsDto {

    private Date updateTime;

    private String executors;

    private Integer env;

    private String caseContent;

    private String chooseContent;
}
