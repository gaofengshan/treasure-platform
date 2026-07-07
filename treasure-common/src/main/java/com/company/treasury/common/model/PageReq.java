package com.company.treasury.common.model;

import lombok.Data;

@Data
public class PageReq {
    private int pageNum = 1;
    private int pageSize = 10;
    private String orderBy;
}
