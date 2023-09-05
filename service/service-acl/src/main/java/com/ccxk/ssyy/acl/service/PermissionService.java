package com.ccxk.ssyy.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.acl.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {
    List<Permission> queryAllPermission();

    boolean removeChildById(Long id);

    List<Permission> findPermissionByRoleId(Long roleId);


    boolean savaRolePermission(Long roleId, Long[] permissionIds);
}
