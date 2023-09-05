package com.ccxk.ssyy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.SkuAttrValue;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SkuAttrValue> getAttrValueListBySkuId(Long id);
}
