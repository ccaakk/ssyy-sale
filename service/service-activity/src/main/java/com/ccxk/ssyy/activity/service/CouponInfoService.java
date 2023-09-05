package com.ccxk.ssyy.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.activity.CouponInfo;
import com.ccxk.ssyy.model.activity.CouponRange;
import com.ccxk.ssyy.model.order.CartInfo;
import com.ccxk.ssyy.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-10
 */
public interface CouponInfoService extends IService<CouponInfo> {
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId);

    IPage<CouponInfo> selectPageCouponInfo(Page<CouponInfo> pageParam);

    CouponInfo getCouponInfo(String id);

    Map<String, Object> findCouponRuleList(Long id);

    void saveCouponRule(CouponRuleVo couponRuleVo);

    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);

    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);

    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);
}
