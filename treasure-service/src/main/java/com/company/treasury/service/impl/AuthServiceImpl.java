package com.company.treasury.service.impl;

import com.company.treasury.common.exception.BizException;
import com.company.treasury.common.model.LoginUser;
import com.company.treasury.security.jwt.JwtTokenProvider;
import com.company.treasury.service.api.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    // 硬编码管理员用户（Phase 3 联调用，后续接入数据库）
    private static final Map<String, Object> ADMIN_USER = new LinkedHashMap<>();

    static {
        ADMIN_USER.put("userId", "1");
        ADMIN_USER.put("username", "admin");
        ADMIN_USER.put("password", "admin123");
        ADMIN_USER.put("realName", "系统管理员");
        ADMIN_USER.put("avatar", "");
        ADMIN_USER.put("email", "admin@treasury.com");
        ADMIN_USER.put("phone", "13800138000");
    }

    @Override
    public String login(String username, String password) {
        if (!ADMIN_USER.get("username").equals(username)) {
            throw new BizException(401, "用户名或密码错误");
        }
        if (!ADMIN_USER.get("password").equals(password)) {
            throw new BizException(401, "用户名或密码错误");
        }
        return jwtTokenProvider.createToken(1L, username);
    }

    @Override
    public void logout(String token) {
        log.info("用户退出登录: token={}", token);
    }

    public LoginUser getUserInfo(Long userId) {
        if (userId != 1L) {
            throw new BizException(401, "用户不存在");
        }

        LoginUser user = new LoginUser();
        user.setUserId("1");
        user.setUsername("admin");
        user.setRealName("系统管理员");
        user.setAvatar("");
        user.setEmail("admin@treasury.com");
        user.setPhone("13800138000");
        user.setPermissions(List.of("*:*:*"));

        // 菜单与前端 Mock 保持一致
        LoginUser.MenuVO dashboard = new LoginUser.MenuVO();
        dashboard.setPath("/dashboard");
        dashboard.setTitle("首页");
        dashboard.setIcon("ep:home-filled");

        LoginUser.MenuVO account = new LoginUser.MenuVO();
        account.setTitle("账户管理");
        account.setIcon("ep:collection");
        LoginUser.MenuVO bank = new LoginUser.MenuVO();
        bank.setPath("/account/bank-account");
        bank.setTitle("银行账户");
        bank.setIcon("ep:coin");
        LoginUser.MenuVO inner = new LoginUser.MenuVO();
        inner.setPath("/account/inner-account");
        inner.setTitle("内部账户");
        inner.setIcon("ep:wallet");
        account.setChildren(List.of(bank, inner));

        LoginUser.MenuVO fund = new LoginUser.MenuVO();
        fund.setTitle("资金归集");
        fund.setIcon("ep:upload");
        LoginUser.MenuVO collect = new LoginUser.MenuVO();
        collect.setPath("/fund/collect");
        collect.setTitle("资金上收");
        collect.setIcon("ep:upload");
        LoginUser.MenuVO allocate = new LoginUser.MenuVO();
        allocate.setPath("/fund/allocate");
        allocate.setTitle("资金下拨");
        allocate.setIcon("ep:download");
        fund.setChildren(List.of(collect, allocate));

        LoginUser.MenuVO payment = new LoginUser.MenuVO();
        payment.setPath("/payment");
        payment.setTitle("支付结算");
        payment.setIcon("ep:money");

        LoginUser.MenuVO budget = new LoginUser.MenuVO();
        budget.setPath("/budget");
        budget.setTitle("资金预算");
        budget.setIcon("ep:document");

        LoginUser.MenuVO report = new LoginUser.MenuVO();
        report.setPath("/report");
        report.setTitle("报表分析");
        report.setIcon("ep:data-analysis");

        LoginUser.MenuVO system = new LoginUser.MenuVO();
        system.setTitle("系统管理");
        system.setIcon("ep:setting");
        LoginUser.MenuVO userMgr = new LoginUser.MenuVO();
        userMgr.setPath("/system/user");
        userMgr.setTitle("用户管理");
        userMgr.setIcon("ep:user");
        LoginUser.MenuVO roleMgr = new LoginUser.MenuVO();
        roleMgr.setPath("/system/role");
        roleMgr.setTitle("角色管理");
        roleMgr.setIcon("ep:setting");
        LoginUser.MenuVO menuMgr = new LoginUser.MenuVO();
        menuMgr.setPath("/system/menu");
        menuMgr.setTitle("菜单配置");
        menuMgr.setIcon("ep:menu");
        system.setChildren(List.of(userMgr, roleMgr, menuMgr));

        user.setMenus(List.of(dashboard, account, fund, payment, budget, report, system));
        return user;
    }
}
