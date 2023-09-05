package com.ccxk.ssyy.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccxk.ssyy.model.sys.RegionWare;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.sys.service.RegionWareService;
import com.ccxk.ssyy.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "开通区域接口")
@RequestMapping("/admin/sys/regionWare")
public class RegionWareController {

    @Autowired
    private RegionWareService regionWareService;

    //开通区域列表
    @ApiOperation("开通区域列表")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       RegionWareQueryVo regionWareQueryVo){

        Page<RegionWare> regionWarePage = new Page<>(page, limit);
        IPage<RegionWare> regionModel = regionWareService.selectPageRegionWare(regionWarePage,regionWareQueryVo);
        return Result.ok(regionModel);
    }

    //添加开通区域
    @ApiOperation("添加开通区域")
    @PostMapping("save")
    public Result addRegionWare(@RequestBody RegionWare regionWare){
        boolean is_success = regionWareService.savaRegionWare(regionWare);
        if (is_success) {
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //删除开通区域
    @ApiOperation("删除开通区域")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean is_success = regionWareService.removeById(id);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }


    //取消开通
    @ApiOperation("取消开通")
    @PostMapping("updateStatus/{id}/{status}")
    public Result cancelRegion(@PathVariable Long id,
                               @PathVariable Integer status){
        boolean is_success = regionWareService.updateStatus(id,status);
        if (is_success){
            return Result.ok(null);
        }
        return Result.fail(null);
    }
}
