package com.ccxk.ssyy.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.acl.Admin;
import com.ccxk.ssyy.vo.acl.AdminQueryVo;

public interface AdminService extends IService<Admin> {
    IPage<Admin> selectAdminPage(Page<Admin> page, AdminQueryVo adminQueryVo);
}
