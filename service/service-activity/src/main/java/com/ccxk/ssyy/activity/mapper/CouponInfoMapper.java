package com.ccxk.ssyy.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccxk.ssyy.model.activity.CouponInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author ccxk
 * @since 2023-08-10
 */
@Repository
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    List<CouponInfo> selectCartCouponInfoList(Long userId);

    List<CouponInfo> selectCouponInfoList(Long id, Long categoryId, Long userId);
}
