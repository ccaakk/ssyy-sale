package com.ccxk.ssyy.service;

import com.ccxk.ssyy.model.search.SkuEs;
import com.ccxk.ssyy.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SkuService {
    void upperSku(Long skuId);

    void lowerSku(Long skuId);

    List<SkuEs> findHotSkuList();

    Page<SkuEs> search(PageRequest pageable, SkuEsQueryVo skuEsQueryVo);

    void incrHotScore(Long skuId);
}
