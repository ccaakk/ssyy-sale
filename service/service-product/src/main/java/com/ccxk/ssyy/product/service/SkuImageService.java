package com.ccxk.ssyy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.SkuImage;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> getImageListBySkuId(Long id);
}
