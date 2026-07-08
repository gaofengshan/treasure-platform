package com.company.treasury.service.impl;

import com.company.treasury.common.exception.BizException;
import com.company.treasury.common.model.LoginUser;
import com.company.treasury.dao.entity.SysUser;
import com.company.treasury.dao.mapper.SysUserMapper;
import com.company.treasury.security.jwt.JwtTokenProvider;
import com.company.treasury.service.api.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            throw new BizException(401, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException(401, "用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BizException(401, "用户已停用，请联系系统管理员启用");
        }
        return jwtTokenProvider.createToken(user.getId(), username);
    }

    @Override
    public void logout(String token) {
        log.info("用户退出登录: token={}", token);
    }

    @Override
    public LoginUser getUserInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BizException(401, "用户不存在");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(String.valueOf(user.getId()));
        loginUser.setUsername(user.getUsername());
        loginUser.setRealName(user.getRealName());
        loginUser.setAvatar("");
        loginUser.setEmail(user.getEmail());
        loginUser.setPhone(user.getPhone());
        loginUser.setPermissions(List.of("*:*:*"));

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

        loginUser.setMenus(List.of(dashboard, account, fund, payment, budget, report, system));
        return loginUser;
    }
}
