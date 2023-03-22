package com.changgou.goods.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class SpuServiceImpl implements SpuService {
    private static Snowflake snowflake = IdUtil.createSnowflake( 1, 1 );
    @Autowired
    SpuMapper spuMapper;
    @Autowired
    BrandMapper brandMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    SkuMapper skuMapper;
    @Transactional
    @Override
    public void add(Goods goods) {
        Spu spu = goods.getSpu();
        spu.setId(snowflake.nextIdStr());
        spu.setIsDelete("0");
        spu.setIsMarketable("0");
        spu.setStatus("0");
        spuMapper.insertSelective(spu);
        //保存sku集合数据到数据库
        saveSkuList(goods);

    }

    /**
     * 保存sku列表
     * @param goods
     */

    private void saveSkuList(Goods goods) {
        //获取spu对象
        Spu spu = goods.getSpu();
        //当前日期
        Date date = new Date();
        //获取对象品牌
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        //获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //获取sku集合对象
        List<Sku> skuList = goods.getSkuList();
        if (skuList != null){
            for (Sku sku: skuList) {
                //设置sku主键ID
                sku.setId(snowflake.nextIdStr());
                //设置sku规格
                if (sku.getSpec() == null || "".equals(sku.getSpec())){
                    sku.setSpec("{}");
                }
                //设置sku名称(商品名称+规格)
                String name = spu.getName();
                //将规格json字符串转换为Map
                Map<String,String> specMap = JSON.parseObject(sku.getSpec(), Map.class);

                if (specMap != null && specMap.size() > 0){
                    for (String value:
                         specMap.values()) {
                        name+=" "+value;
                    }
                }
                sku.setName(name);//名称
                sku.setSpec(spu.getId());//设置spu的ID
                sku.setCreateTime(date);//创建日期
                sku.setUpdateTime(date);//修改日期
                sku.setCategoryId(category.getId());//商品分类ID
                sku.setCategoryName(category.getName());//商品名称
                sku.setBrandName(brand.getName());//品牌名称
                skuMapper.insertSelective(sku);//插入sku表数据

            }
        }

    }
}
