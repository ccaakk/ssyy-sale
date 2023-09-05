package com.ccxk.ssyy.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccxk.ssyy.exception.SsyyException;
import com.ccxk.ssyy.model.sys.RegionWare;
import com.ccxk.ssyy.result.ResultCodeEnum;
import com.ccxk.ssyy.sys.mapper.RegionWareMapper;
import com.ccxk.ssyy.sys.service.RegionWareService;
import com.ccxk.ssyy.vo.sys.RegionWareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {


    @Override
    public IPage<RegionWare> selectPageRegionWare(Page<RegionWare> regionWarePage, RegionWareQueryVo regionWareQueryVo) {
        String keyword = regionWareQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like(RegionWare::getRegionName,keyword).or().like(RegionWare::getWareName,keyword);
        }
        Page<RegionWare> regionWarePage1 = baseMapper.selectPage(regionWarePage, wrapper);
        return regionWarePage1;
    }

    @Override
    public boolean savaRegionWare(RegionWare regionWare) {
        Long regionId = regionWare.getRegionId();
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionWare::getRegionId,regionId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new SsyyException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
        return true;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
        return true;
    }

}
