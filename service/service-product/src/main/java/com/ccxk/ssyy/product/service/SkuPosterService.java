package com.ccxk.ssyy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.SkuPoster;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface SkuPosterService extends IService<SkuPoster> {

    List<SkuPoster> getPosterListBySkuId(Long id);
}
