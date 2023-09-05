package com.ccxk.ssyy.acl.controller;


import com.ccxk.ssyy.acl.service.PermissionService;
import com.ccxk.ssyy.model.acl.Permission;
import com.ccxk.ssyy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "菜单接口")
@RequestMapping("/admin/acl/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //获取权限(菜单/功能)列表
    @ApiOperation("获取权限(菜单/功能)列表")
    @GetMapping
    public Result PermissionService(){
        List<Permission> permissionList = permissionService.queryAllPermission();
        return Result.ok(permissionList);
    }

    //递归删除一个权限项
    @ApiOperation("获取权限(菜单/功能)列表")
    @DeleteMapping("remove/{id}")
    public Result removePermission(@PathVariable Long id){
        boolean is_success = permissionService.removeChildById(id);
        if (is_success) {
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //保存一个权限项
    @ApiOperation("保存一个权限项")
    @PostMapping("save")
    public Result addPermission(@RequestBody Permission permission){
        boolean is_success = permissionService.save(permission);
        if (is_success) {
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //更新一个权限项
    @ApiOperation("更新一个权限项")
    @PutMapping("update")
    public Result updatePermission(@RequestBody Permission permission){
        boolean is_success = permissionService.updateById(permission);
        if (is_success) {
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //7.获取一个角色的所有权限列表
    @ApiOperation(value = "获取一个角色的所有权限列表")
    @GetMapping("toAssign/{roleId}")
    public Result getAssign(@PathVariable Long roleId){
        List<Permission> permission = permissionService.findPermissionByRoleId(roleId);
        return Result.ok(permission);
    }


    //8.给某个角色授权
    @ApiOperation(value = "给某个角色授权")
    @PostMapping("doAssign")
    public Result doAssign(@RequestParam Long roleId,
                           @RequestParam Long[] permissionIds){
        boolean is_success = permissionService.savaRolePermission(roleId,permissionIds);
        if (is_success) {
            return Result.ok(null);
        }
        return Result.fail(null);
    }
}
