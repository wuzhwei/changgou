package com.changgou.goods.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import com.changgou.goods.util.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public List<Sku> findList(Map<String, Object> searchMap) {
        return skuMapper.selectByExample( getExample( searchMap ) );
    }

    /**
     * 条件拼接
     *
     * @param searchMap 查询条件
     * @return example 条件对象
     */
    private Example getExample(@NotNull Map<String, Object> searchMap) {
        Example example = new Example( Sku.class );
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            Condition.share( searchMap, criteria );
            // SPUID
            String spuId = Convert.toStr( searchMap.get( "spuId" ) );
            if (StrUtil.isNotEmpty( spuId )) {
                criteria.andEqualTo( "spuId", spuId );
            }
            // 类目名称
            String categoryName = Convert.toStr( searchMap.get( "categoryName" ) );
            if (StrUtil.isNotEmpty( categoryName )) {
                criteria.andLike( "categoryName", "%" + categoryName + "%" );
            }
            // 品牌名称
            String brandName = Convert.toStr( searchMap.get( "brandName" ) );
            if (StrUtil.isNotEmpty( brandName )) {
                criteria.andLike( "brandName", "%" + brandName + "%" );
            }
            // 规格
            String spec = Convert.toStr( searchMap.get( "spec" ) );
            if (StrUtil.isNotEmpty( spec )) {
                criteria.andLike( "spec", "%" + spec + "%" );
            }
            // 商品状态 1-正常，2-下架，3-删除
            String status = Convert.toStr( searchMap.get( "status" ) );
            if (StrUtil.isNotEmpty( status )) {
                criteria.andEqualTo( "status", status );
            }
            // 价格（分）
            String price = Convert.toStr( searchMap.get( "price" ) );
            if (StrUtil.isNotEmpty( price )) {
                criteria.andEqualTo( "price", price );
            }
            // 库存数量
            String num = Convert.toStr( searchMap.get( "num" ) );
            if (StrUtil.isNotEmpty( num )) {
                criteria.andEqualTo( "num", num );
            }
            // 库存预警数量
            String alertNum = Convert.toStr( searchMap.get( "alertNum" ) );
            if (StrUtil.isNotEmpty( alertNum )) {
                criteria.andEqualTo( "alertNum", alertNum );
            }
            // 重量（克）
            String weight = Convert.toStr( searchMap.get( "weight" ) );
            if (StrUtil.isNotEmpty( weight )) {
                criteria.andEqualTo( "weight", weight );
            }
            // 类目ID
            String categoryId = Convert.toStr( searchMap.get( "categoryId" ) );
            if (StrUtil.isNotEmpty( categoryId )) {
                criteria.andEqualTo( "categoryId", categoryId );
            }
        }
        return example;
    }
}
