package com.ccxk.ssyy.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccxk.ssyy.model.product.Category;
import com.ccxk.ssyy.vo.product.CategoryQueryVo;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author ccxk
 * @since 2023-08-06
 */
public interface CategoryService extends IService<Category> {

    IPage<Category> selectPageCategory(Page<Category> pageParam, CategoryQueryVo categoryQueryVo);

    List<Category> findAllList();

    List<Category> findCategoryList(List<Long> skuIdList);
}
