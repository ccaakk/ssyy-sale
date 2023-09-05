package com.ccxk.ssyy.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.acl.Role;
import com.ccxk.ssyy.vo.acl.RoleQueryVo;

import java.util.Map;

public interface RoleService extends IService<Role> {
    IPage<Role> selectRolePage(Page<Role> rolePage, RoleQueryVo roleQueryVo);


    Map<String, Object> findRoleByAdminId(Long adminId);


    void savaAdminRole(Long adminId, Long[] roleIds);
}
