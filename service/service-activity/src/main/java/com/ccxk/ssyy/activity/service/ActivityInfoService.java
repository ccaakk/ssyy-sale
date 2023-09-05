package com.ccxk.ssyy.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.activity.ActivityInfo;
import com.ccxk.ssyy.model.activity.ActivityRule;
import com.ccxk.ssyy.model.order.CartInfo;
import com.ccxk.ssyy.model.product.SkuInfo;
import com.ccxk.ssyy.vo.activity.ActivityRuleVo;
import com.ccxk.ssyy.vo.order.CartInfoVo;
import com.ccxk.ssyy.vo.order.OrderConfirmVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-10
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);

    IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam);

    Map<String,Object> findActivityRuleList(Long id);

    void saveActivityRule(ActivityRuleVo activityRuleVo);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);

    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    List<ActivityRule> findActivityRuleBySkuId(Long skuId);
}
