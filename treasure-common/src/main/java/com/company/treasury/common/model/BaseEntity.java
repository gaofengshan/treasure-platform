package com.company.treasury.common.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseEntity {
    private Long id;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
