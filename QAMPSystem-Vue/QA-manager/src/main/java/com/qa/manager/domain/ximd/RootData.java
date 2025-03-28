package com.qa.manager.domain.ximd;

import lombok.Data;

import java.util.List;

@Data
public class RootData {

    private DataObj data;

    private List<RootData> children;

}