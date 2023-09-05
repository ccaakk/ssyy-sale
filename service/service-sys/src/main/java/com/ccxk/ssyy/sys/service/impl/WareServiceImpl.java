package com.ccxk.ssyy.sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.model.sys.Ware;
import com.ccxk.ssyy.sys.mapper.WareMapper;
import com.ccxk.ssyy.sys.service.WareService;
import org.springframework.stereotype.Service;

@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {
}
