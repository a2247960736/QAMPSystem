package com.qa.manager.domain.Dto;

import lombok.Data;

import java.util.List;

@Data
public class PickCaseDto {

    /**
     * 优先级 ["0", "1", "2"....]
     */
    private List<String> priority;

    /**
     * 资源 ["用户自己", "在测试用例中", "定义的标签"]
     */
    private List<String> resource;
}
