package com.ccxk.ssyy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.acl.mapper.PermissionMapper;
import com.ccxk.ssyy.acl.service.PermissionService;
import com.ccxk.ssyy.acl.service.RolePermissionService;
import com.ccxk.ssyy.acl.utils.PermissionHelper;
import com.ccxk.ssyy.model.acl.Permission;
import com.ccxk.ssyy.model.acl.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


    @Autowired
    private RolePermissionService rolePermissionService;
    @Override
    public List<Permission> queryAllPermission() {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //递归删除菜单
    @Override
    public boolean removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        this.selectChildListById(id,idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public List<Permission> findPermissionByRoleId(Long roleId) {
        //查询所有权限
        List<Permission> allPermissionsList = baseMapper.selectList(null);

        //根据roleId查询到这个角色的所有权限
        List<RolePermission> exitRolePermission = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId).select("permission_id"));
        List<Long> exitPermission = exitRolePermission.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        //封装权限
        List<Permission> assignPermissions = new ArrayList<>();
        for (Permission permission : allPermissionsList) {
            if (exitPermission.contains(permission.getId())){
                //封装成Permission对象
                permission.setSelect(true);
                assignPermissions.add(permission);
            }
        }
        return  PermissionHelper.buildPermission(allPermissionsList);
    }

    @Override
    public boolean savaRolePermission(Long roleId, Long[] permissionIds) {
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId)) continue;
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissions);
        return true;
    }



    //递归获取子节点
    private void selectChildListById(Long id, List<Long> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.forEach(item -> {idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }

}
