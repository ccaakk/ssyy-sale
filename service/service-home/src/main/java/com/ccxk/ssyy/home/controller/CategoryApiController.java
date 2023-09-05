package com.ccxk.ssyy.home.controller;


import com.ccxk.ssyy.client.product.ProductFeignClient;
import com.ccxk.ssyy.model.product.Category;
import com.ccxk.ssyy.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @ApiOperation(value = "获取分类信息")
    @GetMapping("category")
    public Result index() {
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        return  Result.ok(categoryList);
    }
}
