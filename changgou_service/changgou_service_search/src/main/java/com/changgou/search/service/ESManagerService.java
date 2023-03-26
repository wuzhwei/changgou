package com.changgou.search.service;

/**
 * @Description: es 服务
 */
public interface ESManagerService {
    /**
     * 创建索引库结构
     */
    void createMappingAndIndex();
    /**
     * 导入全部数据进入es
     */
    void importAll();

    /**
     * 根据spuId查询skuList,添加进索引库
     *
     * @param spuId 商品id
     */
    void importDataBySpuId(String spuId);

    /**
     * 根据spuid查询skuList,从索引库删除
     *
     * @param spuId 商品id
     */
    void delDataBySpuId(String spuId);
}