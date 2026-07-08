package com.company.treasury.service.impl;

import com.company.treasury.common.exception.BizException;
import com.company.treasury.dao.entity.SysUser;
import com.company.treasury.dao.mapper.SysUserMapper;
import com.company.treasury.service.api.SysUserService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser getById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public List<SysUser> listPage(String username, String realName, String phone, Integer status,
                                   int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return sysUserMapper.selectPage(username, realName, phone, status);
    }

    @Override
    @Transactional
    public long createUser(SysUser user) {
        SysUser exist = sysUserMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            throw new BizException(400, "用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        sysUserMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional
    public void updateUser(SysUser user) {
        SysUser exist = sysUserMapper.selectById(user.getId());
        if (exist == null) {
            throw new BizException(404, "用户不存在");
        }
        sysUserMapper.update(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        sysUserMapper.updatePassword(id, passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        // 禁止关闭系统管理员
        if (id == 1L) {
            throw new BizException(400, "当前用户是系统管理员，禁止关闭");
        }
        sysUserMapper.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }
}
