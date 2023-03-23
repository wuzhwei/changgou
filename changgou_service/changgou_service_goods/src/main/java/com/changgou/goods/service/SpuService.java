package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;

import java.util.List;


public interface SpuService {
    /**
     * 新增
     * @param goods
     */
    void add(Goods goods);

    /**
     * 根据ID查询商品
     * @param id
     * @return
     */
    Goods findGoodsById(String id);
    /***
     * 修改数据
     * @param goods
     */
    void update(Goods goods);


}
