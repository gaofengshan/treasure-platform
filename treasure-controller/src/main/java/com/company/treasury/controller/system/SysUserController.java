package com.company.treasury.controller.system;

import com.company.treasury.common.model.PasswordDTO;
import com.company.treasury.common.model.PageReq;
import com.company.treasury.common.model.StatusDTO;
import com.company.treasury.common.result.PageResult;
import com.company.treasury.common.result.Result;
import com.company.treasury.dao.entity.SysUser;
import com.company.treasury.service.api.SysUserService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping("/page")
    public Result<PageResult<SysUser>> page(SysUser query, PageReq pageReq) {
        List<SysUser> list = sysUserService.listPage(
                query.getUsername(), query.getRealName(), query.getPhone(),
                query.getStatus(), pageReq.getPageNum(), pageReq.getPageSize());
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return Result.success(PageResult.success(
                pageInfo.getList(), pageInfo.getTotal(),
                pageInfo.getPageNum(), pageInfo.getPageSize()));
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @PostMapping
    public Result<Long> create(@RequestBody SysUser user) {
        long userId = sysUserService.createUser(user);
        return Result.success(userId);
    }

    @PutMapping
    public Result<Void> update(@RequestBody SysUser user) {
        sysUserService.updateUser(user);
        return Result.success();
    }

    @PutMapping("/{id}/password")
    public Result<Void> changePassword(@PathVariable Long id, @RequestBody PasswordDTO dto) {
        sysUserService.changePassword(id, dto.getPassword());
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestBody StatusDTO dto) {
        sysUserService.changeStatus(id, dto.getStatus());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.success();
    }
}
