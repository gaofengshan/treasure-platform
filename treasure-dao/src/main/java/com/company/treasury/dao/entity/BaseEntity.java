package com.company.treasury.dao.entity;

import java.time.LocalDateTime;

public class BaseEntity {
    private Long id;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
