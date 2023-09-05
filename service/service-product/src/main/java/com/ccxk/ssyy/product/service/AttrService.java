package com.ccxk.ssyy.product.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.Attr;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface AttrService extends IService<Attr> {

    List<Attr> getAttrListByGroupId(Long attrGroupId);
}
