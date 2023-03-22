package com.changgou.goods.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: wzw
 * @Description: 商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goods {
    /**
     * spu
     */
    private Spu spu;

    /**
     * sku集合
     */
    private List<Sku> skuList;
}