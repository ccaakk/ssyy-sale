package com.ccxk.ssyy.repository;

import com.ccxk.ssyy.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {

    Page<SkuEs> findByOrderByHotScoreDesc(PageRequest pageable);

    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, PageRequest pageable);

    Page<SkuEs> findByWareIdAndKeyword(Long wareId, String keyword, PageRequest pageable);
}
