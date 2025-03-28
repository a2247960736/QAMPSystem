package com.qa.manager.domain.request.dir;

import com.qa.manager.domain.Dto.DirNodeDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DirTreeResp {

    private List<DirNodeDto> children = new ArrayList<>();

    // 添加无参构造器
    public DirTreeResp() {
    }
}