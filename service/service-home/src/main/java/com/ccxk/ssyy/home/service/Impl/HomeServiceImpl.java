package com.ccxk.ssyy.home.service.Impl;

import com.ccxk.ssyy.client.product.ProductFeignClient;
import com.ccxk.ssyy.client.search.SkuFeignClient;
import com.ccxk.ssyy.client.user.UserFeignClient;
import com.ccxk.ssyy.home.service.HomeService;
import com.ccxk.ssyy.model.product.Category;
import com.ccxk.ssyy.model.product.SkuInfo;
import com.ccxk.ssyy.model.search.SkuEs;
import com.ccxk.ssyy.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Override
    public Map<String, Object> homeData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        //1.根据userId获取当前登录用户提货地址信息
        //远程调用service-user模块接口获取数据
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        result.put("leaderAddressVo",leaderAddressVo);
        //2.获取所有分类
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList",categoryList);
        //3.获取新人专享的商品
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList",newPersonSkuInfoList);
        //4.获取热销好货
        //获取爆品商品
        List<SkuEs> hotSkuList = skuFeignClient.findHotSkuList();
        //获取sku对应的促销活动标签
//        if(!CollectionUtils.isEmpty(hotSkuList)) {
//            List<Long> skuIdList = hotSkuList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
//            Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);
//            if(null != skuIdToRuleListMap) {
//                hotSkuList.forEach(skuEs -> {
//                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
//                });
//            }
//        }
        result.put("hotSkuList", hotSkuList);

        //5.封装到map返回
        return result;
    }
}
