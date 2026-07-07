package com.company.treasury.controller;

import com.company.treasury.common.exception.BizException;
import com.company.treasury.common.model.LoginRequest;
import com.company.treasury.common.model.LoginUser;
import com.company.treasury.common.result.Result;
import com.company.treasury.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return Result.success(Map.of("token", token));
    }

    @GetMapping("/user-info")
    public Result<Map<String, Object>> userInfo(Authentication auth) {
        if (auth == null) {
            throw new BizException(401, "Token无效");
        }
        Long userId = (Long) auth.getDetails();
        LoginUser user = authService.getUserInfo(userId);

        Map<String, Object> data = Map.of(
                "userInfo", user,
                "permissions", user.getPermissions(),
                "menus", user.getMenus()
        );
        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<Void> logout(Authentication auth) {
        if (auth != null) {
            authService.logout((String) auth.getCredentials());
        }
        return Result.success();
    }
}
