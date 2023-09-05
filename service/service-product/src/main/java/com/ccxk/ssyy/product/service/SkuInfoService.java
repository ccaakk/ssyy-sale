package com.ccxk.ssyy.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.SkuInfo;
import com.ccxk.ssyy.vo.product.SkuInfoQueryVo;
import com.ccxk.ssyy.vo.product.SkuInfoVo;
import com.ccxk.ssyy.vo.product.SkuStockLockVo;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface SkuInfoService extends IService<SkuInfo> {

    public void minusStock(String orderNo);

    IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    void saveSkuInfo(SkuInfoVo skuInfoVo);


    SkuInfoVo getSkuInfoVo(Long id);

    void updateSkuInfo(SkuInfoVo skuInfoVo);

    void removeSkuInfoById(Long id);

    void removeSkuInfoByIds(List<Long> idList);

    void check(Long skuId, Integer status);

    void publish(Long skuId, Integer status);

    void isNewPerson(Long skuId, Integer status);

    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    List<SkuInfo> findNewPersonList();

    Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo);
}
