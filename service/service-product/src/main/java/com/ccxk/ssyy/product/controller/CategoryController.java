package com.ccxk.ssyy.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccxk.ssyy.model.product.Category;
import com.ccxk.ssyy.product.service.CategoryService;
import com.ccxk.ssyy.result.Result;
import com.ccxk.ssyy.vo.product.CategoryQueryVo;
import com.ccxk.ssyy.vo.product.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品三级分类 前端控制器
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
@RestController
@RequestMapping("/admin/product/category")
@Api(tags = "商品接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //商品分类列表
    @ApiOperation("商品分类列表")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       CategoryQueryVo categoryQueryVo){
        Page<Category> pageParam = new Page<>(page, limit);
        IPage<Category> pageModel = categoryService.selectPageCategory(pageParam,categoryQueryVo);
        return Result.ok(pageModel);
    }

    //新增商品
    @ApiOperation(value = "新增商品分类")
    @PostMapping("save")
    public Result sava(@RequestBody Category category){
        boolean is_success = categoryService.save(category);
        if (is_success){
            return  Result.ok(null);
        }
        return Result.fail(null);
    }

    //修改商品
    @ApiOperation(value = "修改商品分类")
    @PutMapping("update")
    public Result updateById(@RequestBody Category category){
        boolean is_success = categoryService.updateById(category);
        if (is_success){
            return  Result.ok(null);
        }
        return Result.fail(null);
    }

    //删除商品
    @ApiOperation(value = "删除商品分类")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = categoryService.removeById(id);
        if (is_success){
            return  Result.ok(null);
        }
        return Result.fail(null);
    }

    //选中删除多个商品
    @ApiOperation(value = "根据id列表删除商品分类")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean is_success = categoryService.removeByIds(ids);
        if (is_success){
            return  Result.ok(null);
        }
        return Result.fail(null);
    }

    //获取商品信息
    @ApiOperation(value = "获取商品分类信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return Result.ok(category);
    }

    //获取商品分类
    @ApiOperation(value = "获取全部商品分类")
    @GetMapping("findAllList")
    public Result findAllList(){
        return Result.ok(categoryService.findAllList());
    }
}

