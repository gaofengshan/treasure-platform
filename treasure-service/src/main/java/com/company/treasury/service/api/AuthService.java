package com.company.treasury.service.api;

import com.company.treasury.common.model.LoginUser;

public interface AuthService {
    String login(String username, String password);
    void logout(String token);
    LoginUser getUserInfo(Long userId);
}
