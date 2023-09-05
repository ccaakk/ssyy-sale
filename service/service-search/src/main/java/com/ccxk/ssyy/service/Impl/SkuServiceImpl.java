package com.ccxk.ssyy.service.Impl;

import com.ccxk.ssyy.auth.AuthContextHolder;
import com.ccxk.ssyy.client.product.ProductFeignClient;
import com.ccxk.ssyy.enums.SkuType;
import com.ccxk.ssyy.model.product.Category;
import com.ccxk.ssyy.model.product.SkuInfo;
import com.ccxk.ssyy.model.search.SkuEs;
import com.ccxk.ssyy.repository.SkuRepository;
import com.ccxk.ssyy.service.SkuService;
import com.ccxk.ssyy.vo.search.SkuEsQueryVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.ccxk.ssyy.activity.client.ActivityFeignClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuService {

   @Autowired
   private RedisTemplate redisTemplate;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Override
    public List<SkuEs> findHotSkuList() {
        //0代表第一页
        PageRequest pageable = PageRequest.of(0, 10);
        Page<SkuEs> pageModel = skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    @Override
    public Page<SkuEs> search(PageRequest pageable, SkuEsQueryVo skuEsQueryVo) {
        //1.向SkuEsQueryVo里设置wareId,当前登录用户的仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        //2.调用skuRepository,根据SpringData命名规则定义方法,进行条件查询
        ////判断关键字是否为空,如果为空,就根据仓库id+分类id查询
        //如果不为空,则根据关键字+仓库id+分类id查询
        Page<SkuEs> pageModel = null;
        String keyword = skuEsQueryVo.getKeyword();
        if (StringUtils.isEmpty(keyword)){
            //根据仓库id+分类id查询
            pageModel = skuRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(),skuEsQueryVo.getWareId(),pageable);
        }else {
            //多添加keyword条件
            pageModel = skuRepository.findByWareIdAndKeyword(skuEsQueryVo.getWareId(),skuEsQueryVo.getKeyword(),pageable);
        }
        //3.查询商品参加优惠活动
        List<SkuEs> skuEsList = pageModel.getContent();
        if (!CollectionUtils.isEmpty(skuEsList)){
            //遍历SkuEsList获取所有SkuId
            List<Long> skuIdList = skuEsList.stream().map(SkuEs::getId).collect(Collectors.toList());
            //根据skuId列表远程调用,调用service-activity得到数据
            //返回Map<Long,List<String>>
            Map<Long,List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList); //TODO
            //封装到skuEs
            if (skuIdToRuleListMap != null){
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }

        return pageModel;
    }

    //更新商品热度
    @Override
    public void incrHotScore(Long skuId) {
        String key = "hotScore";
        //redis保存数据，每次+1
        Double hotScore = redisTemplate.opsForZSet().incrementScore(key, "skuId:" + skuId, 1);
        //规则
        if(hotScore%10==0) {
            //更新es
            Optional<SkuEs> optional = skuRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            skuRepository.save(skuEs);
        }
    }

    @Override
    public void upperSku(Long skuId) {
        //根据skuId获取相关信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo == null){
            return;
        }
        //封装数据到SkuEs数据中
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());

        //调用方法添加到Es
        SkuEs skuEs = new SkuEs();
        if (category!=null){
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }

        //封装sku信息
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(Objects.equals(skuInfo.getSkuType(), SkuType.COMMON.getCode())) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        skuRepository.save(skuEs);
    }

    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

}
