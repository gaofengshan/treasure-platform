package com.company.treasury.dao.mapper;

import com.company.treasury.dao.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(@Param("id") Long id);
    SysUser selectByUsername(@Param("username") String username);
    List<SysUser> selectPage(
            @Param("username") String username,
            @Param("realName") String realName,
            @Param("phone") String phone,
            @Param("status") Integer status
    );
    int insert(SysUser user);
    int update(SysUser user);
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
}
