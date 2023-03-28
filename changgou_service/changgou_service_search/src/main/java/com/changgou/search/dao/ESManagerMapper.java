package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @Description: es操作商品接口
 */
@Component
public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
