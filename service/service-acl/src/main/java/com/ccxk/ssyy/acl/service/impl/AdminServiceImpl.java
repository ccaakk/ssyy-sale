package com.ccxk.ssyy.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.acl.mapper.AdminMapper;
import com.ccxk.ssyy.acl.service.AdminService;
import com.ccxk.ssyy.model.acl.Admin;
import com.ccxk.ssyy.vo.acl.AdminQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public IPage<Admin> selectAdminPage(Page<Admin> page, AdminQueryVo adminQueryVo) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        String userName = adminQueryVo.getUsername();
        if (!StringUtils.isEmpty(userName)){
            wrapper.like(Admin::getUsername,userName);
        }
        Page<Admin> adminPage = baseMapper.selectPage(page, wrapper);
        return adminPage;
    }
}
