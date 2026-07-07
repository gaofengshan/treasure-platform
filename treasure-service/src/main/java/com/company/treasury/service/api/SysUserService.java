package com.company.treasury.service.api;

import com.company.treasury.dao.entity.SysUser;
import java.util.List;

public interface SysUserService {
    SysUser getById(Long id);
    SysUser getByUsername(String username);
    List<SysUser> listPage(String username, String realName, String phone, Integer status, int pageNum, int pageSize);
    long createUser(SysUser user);
    void updateUser(SysUser user);
    void changePassword(Long id, String newPassword);
    void changeStatus(Long id, Integer status);
    void deleteUser(Long id);
}
