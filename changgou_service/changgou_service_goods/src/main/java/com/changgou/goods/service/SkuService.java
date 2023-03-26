package com.changgou.goods.service;

import com.changgou.goods.pojo.Sku;

import java.util.List;
import java.util.Map;

public interface SkuService {
    /**
     * 多条件搜索Sku数据
     *
     * @param searchMap 条件集合
     * @return Sku数据
     */
    List<Sku> findList(Map<String, Object> searchMap);
}
