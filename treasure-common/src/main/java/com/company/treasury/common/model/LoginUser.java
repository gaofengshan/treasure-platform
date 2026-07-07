package com.company.treasury.common.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginUser {
    private String userId;
    private String username;
    private String realName;
    private String avatar;
    private String email;
    private String phone;
    private List<String> permissions;
    private List<MenuVO> menus;

    @Data
    public static class MenuVO {
        private String path;
        private String title;
        private String icon;
        private List<MenuVO> children;
    }
}
