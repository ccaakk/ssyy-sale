package com.ccxk.ssyy.sys.controller;


import com.ccxk.ssyy.model.sys.Region;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "区域接口")
@RequestMapping("/admin/sys/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    //根据关键字获取地区列表
    @ApiOperation(value = "根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable String keyword){
        List<Region> regionList = regionService.findRegionByKeyword(keyword);
        return Result.ok(regionList);
    }

    //
}
