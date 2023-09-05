package com.ccxk.ssyy.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccxk.ssyy.acl.service.PermissionService;
import com.ccxk.ssyy.acl.service.RoleService;
import com.ccxk.ssyy.model.acl.Role;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.vo.acl.RoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    //1.角色列表(分页查询)
    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{current}/{limit}")
    public Result pageList(@PathVariable Long current,
                           @PathVariable Long limit,
                           RoleQueryVo roleQueryVo){
        Page<Role> rolePage = new Page<>(current,limit);
        IPage<Role> pageModel = roleService.selectRolePage(rolePage,roleQueryVo);
        return Result.ok(pageModel);
    }
    //2.根据id查询角色
    @ApiOperation(value = "根据id查询角色")
    @GetMapping("get/{id}")
    public Result getRoleByRoleId(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
    //3.添加角色
    @ApiOperation(value = "添加角色")
    @PostMapping("save")
    public Result save(@RequestBody Role role){
        boolean is_success = roleService.save(role);
        if(is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //4.根据id删除角色
    @ApiOperation(value = "根据id删除角色")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean is_success = roleService.removeById(id);
        if(is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
    //5.修改角色
    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody Role role){
        boolean is_success = roleService.updateById(role);
        if(is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
    //6.批量删除
    @ApiOperation(value = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean is_success = roleService.removeByIds(idList);
        if(is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }

}
