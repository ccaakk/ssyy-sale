package com.ccxk.ssyy.sys.controller;

import com.ccxk.ssyy.model.sys.Ware;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.sys.service.WareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api( tags = "仓库管理")
@RestController
@RequestMapping(value="/admin/sys/ware")
public class WareController {

    @Autowired
    private WareService wareService;

    //查询所有仓库列表
    @ApiOperation("查询所有仓库列表")
    @GetMapping("findAllList")
    public Result findAllList(){
        List<Ware> list = wareService.list();
        return Result.ok(list);
    }
}
