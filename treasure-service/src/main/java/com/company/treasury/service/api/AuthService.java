package com.company.treasury.service.api;

public interface AuthService {
    String login(String username, String password);
    void logout(String token);
}
