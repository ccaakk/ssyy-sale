package com.ccxk.ssyy.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccxk.ssyy.constant.RedisConst;
import com.ccxk.ssyy.enums.SkuCheckStatus;
import com.ccxk.ssyy.exception.SsyyException;
import com.ccxk.ssyy.model.product.SkuAttrValue;
import com.ccxk.ssyy.model.product.SkuImage;
import com.ccxk.ssyy.model.product.SkuInfo;
import com.ccxk.ssyy.model.product.SkuPoster;
import com.ccxk.ssyy.mq.constant.MqConst;
import com.ccxk.ssyy.mq.service.RabbitService;
import com.ccxk.ssyy.product.mapper.SkuInfoMapper;
import com.ccxk.ssyy.product.service.SkuAttrValueService;
import com.ccxk.ssyy.product.service.SkuImageService;
import com.ccxk.ssyy.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.product.service.SkuPosterService;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.result.ResultCodeEnum;
import com.ccxk.ssyy.vo.product.SkuInfoQueryVo;
import com.ccxk.ssyy.vo.product.SkuInfoVo;
import com.ccxk.ssyy.vo.product.SkuStockLockVo;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private SkuInfoService skuInfoService;
    //海报
    @Autowired
    private SkuPosterService skuPosterService;

    //图片
    @Autowired
    private SkuImageService skuImagesService;

    //平台属性
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;
    
    @Override
    public IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo) {
        Long categoryId = skuInfoQueryVo.getCategoryId();
        String keyword = skuInfoQueryVo.getKeyword();
        String skuType = skuInfoQueryVo.getSkuType();
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(categoryId)) {
            wrapper.eq(SkuInfo::getCategoryId, categoryId);
        }
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(SkuInfo::getSkuName, keyword);
        }
        if (!StringUtils.isEmpty(skuType)) {
            wrapper.like(SkuInfo::getSkuType, skuType);
        }
        wrapper.orderByDesc(SkuInfo::getId);
        IPage<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, wrapper);
        return skuInfoPage;
    }

    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //添加基本信息
        SkuInfo skuInfo = new SkuInfo();
        //相同属性名加入
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.insert(skuInfo);
        
        //保存图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(skuInfo.getId());
            }
            skuImagesService.saveBatch(skuImagesList);
        }

        //保存海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)){
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //保存平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    @Override
    public SkuInfoVo getSkuInfoVo(Long id) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();

        //根据id查询基本信息
        SkuInfo skuInfo = baseMapper.selectById(id);
        //根据id查询图片列表信息
        List<SkuImage> skuImageList = skuImagesService.getImageListBySkuId(id);
        //根据id查询商品海报列表信息
        List<SkuPoster> skuPosterList = skuPosterService.getPosterListBySkuId(id);
        //根据id查询商品属性列表
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.getAttrValueListBySkuId(id);
        //封装所有数据
        BeanUtils.copyProperties(skuInfo,skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        //修改sku基本信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.updateById(skuInfo);
        Long skuId = skuInfoVo.getId();
        //修改海报信息
        LambdaQueryWrapper<SkuPoster> wrapperSkuPoster = new LambdaQueryWrapper<>();
        wrapperSkuPoster.eq(SkuPoster::getSkuId,skuId);
        skuPosterService.remove(wrapperSkuPoster);
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)){
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuId);
            }
            skuPosterService.saveBatch(skuPosterList);
        }
        //修改图片信息
        LambdaQueryWrapper<SkuImage> wrapperSkuImage = new LambdaQueryWrapper<>();
        wrapperSkuImage.eq(SkuImage::getSkuId,skuId);
        skuImagesService.remove(wrapperSkuImage);
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage : skuImagesList) {
                skuImage.setSkuId(skuId);
            }
            skuImagesService.saveBatch(skuImagesList);
        }
        //修改商品属性
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId,skuId));
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuId);
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    @Override
    public void removeSkuInfoById(Long id) {
        //删除skuInfo里的数据
        skuInfoService.removeById(id);
        //删除Image
        skuImagesService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId,id));
        //删除海报
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId,id));
        //删除商品属性
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId,id));
    }

    @Override
    public void removeSkuInfoByIds(List<Long> idList) {
        //删除skuInfo里的数据
        skuInfoService.removeByIds(idList);

        //删除Image
        for (Long id : idList) {
            skuImagesService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId,id));
        }

        //删除海报
        for (Long id : idList) {
            skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId,id));
        }

        //删除商品属性
        for (Long id : idList) {
            skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId,id));
        }
    }

    @Override
    public void check(Long skuId, Integer status) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        skuInfo.setCheckStatus(status);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public void publish(Long skuId, Integer status) {
        //商品上架
        if (status == 1){
            SkuInfo skuInfo = baseMapper.selectById(skuId);
            skuInfo.setPublishStatus(status);
            baseMapper.updateById(skuInfo);
            //整合MQ
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_UPPER, skuId);
        }
        //商品下架
        else {
            SkuInfo skuInfo = baseMapper.selectById(skuId);
            skuInfo.setPublishStatus(status);
            baseMapper.updateById(skuInfo);
            //TODO
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT,MqConst.ROUTING_GOODS_LOWER,skuId);
        }
    }

    @Override
    public void isNewPerson(Long skuId, Integer status) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        skuInfo.setIsNewPerson(status);
        baseMapper.updateById(skuInfo);
    }

    @Override
    public List<SkuInfo> findSkuInfoList(List<Long> skuIdList) {
        List<SkuInfo> skuInfoList = this.listByIds(skuIdList);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(SkuInfo::getSkuName, keyword);
        List<SkuInfo> skuInfoList = baseMapper.selectList(wrapper);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findNewPersonList() {
        //分页排序获取新人专享商品库存量前三的商品
        Page<SkuInfo> pageParam = new Page<>(1,3);
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getIsNewPerson, 1)
                .eq(SkuInfo::getPublishStatus, 1)
                .orderByDesc(SkuInfo::getLockStock);
        IPage<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, wrapper);
        List<SkuInfo> result = skuInfoPage.getRecords();
        return result;
    }

    //验证和锁定库存
    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList,
                                String orderNo) {
        //1 判断skuStockLockVoList集合是否为空
        if(CollectionUtils.isEmpty(skuStockLockVoList)) {
            throw new SsyyException(ResultCodeEnum.DATA_ERROR);
        }

        //2 遍历skuStockLockVoList得到每个商品，验证库存并锁定库存，具备原子性
        skuStockLockVoList.stream().forEach(skuStockLockVo -> {
            this.checkLock(skuStockLockVo);
        });

        //3 只要有一个商品锁定失败，所有锁定成功的商品都解锁
        boolean flag = skuStockLockVoList.stream()
                .anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock());
        if(flag) {
            //所有锁定成功的商品都解锁
            skuStockLockVoList.stream().filter(SkuStockLockVo::getIsLock)
                    .forEach(skuStockLockVo -> {
                        baseMapper.unlockStock(skuStockLockVo.getSkuId(),
                                skuStockLockVo.getSkuNum());
                    });
            //返回失败的状态
            return false;
        }

        //4 如果所有商品都锁定成功了，redis缓存相关数据，为了方便后面解锁和减库存
        redisTemplate.opsForValue()
                .set(RedisConst.SROCK_INFO+orderNo,skuStockLockVoList);
        return true;
    }


    //2 遍历skuStockLockVoList得到每个商品，验证库存并锁定库存，具备原子性
    private void checkLock(SkuStockLockVo skuStockLockVo) {
        //获取锁
        //公平锁
        RLock rLock =
                this.redissonClient.getFairLock(RedisConst.SKUKEY_PREFIX + skuStockLockVo.getSkuId());
        //加锁
        rLock.lock();

        try {
            //验证库存
            SkuInfo skuInfo =
                    baseMapper.checkStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum());
            //判断没有满足条件商品，设置isLock值false，返回
            if(skuInfo == null) {
                skuStockLockVo.setIsLock(false);
                return;
            }
            //有满足条件商品
            //锁定库存:update
            Integer rows =
                    baseMapper.lockStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum());
            if(rows == 1) {
                skuStockLockVo.setIsLock(true);
            }
        } finally {
            //解锁
            rLock.unlock();
        }
    }

    @Override
    public void minusStock(String orderNo) {
        //从redis获取锁定库存信息
        List<SkuStockLockVo> skuStockLockVoList =
                (List<SkuStockLockVo>)redisTemplate.opsForValue().get(RedisConst.SROCK_INFO + orderNo);
        if(CollectionUtils.isEmpty(skuStockLockVoList)) {
            return;
        }
        //遍历集合，得到每个对象，减库存
        skuStockLockVoList.forEach(skuStockLockVo -> {
            baseMapper.minusStock(skuStockLockVo.getSkuId(),skuStockLockVo.getSkuNum());
        });

        //删除redis数据
        redisTemplate.delete(RedisConst.SROCK_INFO + orderNo);
    }

}
