package com.ccxk.ssyy.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccxk.ssyy.model.activity.ActivityInfo;
import com.ccxk.ssyy.model.activity.ActivityRule;
import com.ccxk.ssyy.model.activity.ActivitySku;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author ccxk
 * @since 2023-08-10
 */
@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    List<Long> selectSkuIdListExist(@Param("skuIdList") List<Long> skuIdList);

    List<ActivityRule> findActivityRule(@Param("skuId") Long skuId);

    List<ActivitySku> selectCartActivity(List<Long> skuIdList);
}
