package com.company.treasury.common.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private boolean remember;
}
