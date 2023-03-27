package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;

import java.util.List;


public interface SpuService {
    /**
     * 新增
     * @param goods
     */
    void add(Goods goods);
    /**
     * 根据ID查询SPU
     *
     * @param id Spu id
     * @return Spu信息
     */
    Spu findById(String id);

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
    /**
     * 审核
     * @param id
     */
    void audit(String id);

    /**
     * 下架商品
     * @param id
     */
    void pull(String id);

    /**
     * 上架商品
     * @param id
     */
    void push(String id);

    /**
     * 逻辑上的删除商品
     * @param id
     */
    void delete(String id);

    /**
     * 恢复数据
     * @param id
     */
    void restore(String id);
    /**
     * 物理删除
     * @param id
     */
    void realDelete(String id);


}
