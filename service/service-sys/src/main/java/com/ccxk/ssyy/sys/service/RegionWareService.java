package com.ccxk.ssyy.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.sys.RegionWare;
import com.ccxk.ssyy.vo.sys.RegionWareQueryVo;

public interface RegionWareService extends IService<RegionWare> {
    IPage<RegionWare> selectPageRegionWare(Page<RegionWare> regionWarePage, RegionWareQueryVo regionWareQueryVo);

    boolean savaRegionWare(RegionWare regionWare);


    boolean updateStatus(Long id, Integer status);
}
