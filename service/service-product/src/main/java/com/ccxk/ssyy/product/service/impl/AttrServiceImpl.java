package com.ccxk.ssyy.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ccxk.ssyy.model.product.Attr;
import com.ccxk.ssyy.product.mapper.AttrMapper;
import com.ccxk.ssyy.product.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {

    @Override
    public List<Attr> getAttrListByGroupId(Long attrGroupId) {
        List<Attr> list = baseMapper.selectList(new LambdaQueryWrapper<Attr>().eq(Attr::getAttrGroupId, attrGroupId));
        return list;
    }
}
