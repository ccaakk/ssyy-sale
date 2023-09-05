package com.ccxk.ssyy.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccxk.ssyy.acl.service.AdminService;
import com.ccxk.ssyy.acl.service.RoleService;
import com.ccxk.ssyy.common.utils.MD5;
import com.ccxk.ssyy.model.acl.Admin;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.vo.acl.AdminQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/user")
@Api(tags = "用户接口")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //1.分页用户列表
    @ApiOperation("分页用户列表")
    @GetMapping("{current}/{limit}")
    public Result pageList(@PathVariable Long current,
                           @PathVariable Long limit,
                           AdminQueryVo adminQueryVo){
        Page<Admin> page = new Page<>(current,limit);
        IPage<Admin> adminIPage = adminService.selectAdminPage(page,adminQueryVo);
        return Result.ok(adminIPage);
    }
    //2.增加用户
    @ApiOperation("增加用户")
    @PostMapping("save")
    public Result save(@RequestBody Admin admin){
        //MD5加密
        admin.setPassword(MD5.encrypt(admin.getPassword()));
        boolean is_success = adminService.save(admin);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
    //3.根据id删除用户
    @ApiOperation("根据id删除用户")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean is_success = adminService.removeById(id);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
    //4.根据id修改用户
    @ApiOperation("根据id修改用户")
    @PutMapping("update")
    public Result updateById(@RequestBody Admin admin){
        boolean is_success = adminService.updateById(admin);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
    //5.批量删除用户
    @ApiOperation("批量删除用户")
    @DeleteMapping("batchRemove")
    public Result removeByIds(@RequestBody List<Long> ids){
        boolean is_success = adminService.removeByIds(ids);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //根据用户获取角色数据
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("toAssign/{adminId}")
    public Result getRoles(@PathVariable Long adminId){
        Map<String, Object> roleMap = roleService.findRoleByAdminId(adminId);
        return Result.ok(roleMap);
    }
    //给某个用户分配角色
    @ApiOperation("给某个用户分配角色")
    @PostMapping("doAssign")
    public Result assignRoles(@RequestParam Long adminId,
                              @RequestParam Long[] roleIds){
        roleService.savaAdminRole(adminId,roleIds);
        return Result.ok(null);
    }


}
