package com.changgou.goods.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import tk.mybatis.mapper.entity.Example;

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
     * 根据ID查询商品
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //设置查询条件
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        //查询SKU 列表
        List<Sku> skuList = skuMapper.selectByExample(example);
        //封装返回
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

    @Override
    @Transactional
    public void update(Goods goods) {
        //取出spu部分
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);
        //删除原sku列表
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",spu.getId());
        skuMapper.deleteByExample(example);
        //保存sku列表
        saveSkuList(goods);

    }

    /**
     * 审核操作
     * @param id
     */

    @Override
    @Transactional
    public void audit(String id) {
        //查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null){
            throw new RuntimeException("当前商品不存在");

        }
        //判断当前spu是否处于删除状态
        if ("1".equals(spu.getIsDelete())){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //不处于删除状态，修改审核状态为1,上下架状态为1
        spu.setStatus("1");
        spu.setIsMarketable("1");
        //执行修改操作
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 下架
     * @param id
     */

    @Override
    @Transactional
    public void pull(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null){
            throw new RuntimeException("当前商品不存在");
        }
        //判断商品是否处于删除状态
        if ("1".equals(spu.getIsDelete())){
            throw new RuntimeException("当前商品处于删除状态");
        }
        //商品处于未删除状态的话,则修改上下架状态为已下架(0)
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Transactional
    @Override
    public void push(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getStatus().equals("1")){
            throw new RuntimeException("尚未通过审核的商品不能上架!");

        }
        //上架状态
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 逻辑上的删除操作
     * @param id
     */
    @Override
    public void delete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //检查商品是否下架的商品
        if (!spu.getIsMarketable().equals("0")){
            throw new RuntimeException("必须先下架才能删除");
        }
        spu.setIsMarketable("1");//逻辑删除
        spu.setStatus("0");//未审核
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 恢复商品
     * @param id
     */
    @Override
    public void restore(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //检查是否删除的商品
        if (!spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品未删除");
        }
        spu.setIsDelete("0");//未删除
        spu.setStatus("0");//未审核
        spuMapper.updateByPrimaryKeySelective(spu);


    }

    @Override
    public void realDelete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //检查是否删除的商品
        if(!spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品未删除！");
        }
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 添加sku数据
     *
     * @param goods 商品信息
     */
    private void saveSkuList(Goods goods) {
        Spu spu = goods.getSpu();
        // 查询分类对象
        Category category = categoryMapper.selectByPrimaryKey( spu.getCategory3Id() );
        // 查询品牌对象
        Brand brand = brandMapper.selectByPrimaryKey( spu.getBrandId() );

        // 获取sku集合
        List<Sku> skuList = goods.getSkuList();
        if (ObjectUtil.isNotEmpty( skuList )) {
            // 遍历sku集合,循环填充数据并添加到数据库中
            for (Sku sku : skuList) {
                // 设置skuId
                sku.setId( snowflake.nextIdStr() );
                // 设置sku规格数据
                String skuSpec = sku.getSpec();
                if (StrUtil.isEmpty( skuSpec )) {
                    sku.setSpec( "{}" );
                }
                // 设置sku名称(spu名称+规格)
                StringBuilder spuName = new StringBuilder( spu.getName() );
                Map<String, String> specMap = JSON.parseObject( skuSpec, Map.class );
                if (ObjectUtil.isNotEmpty( specMap )) {
                    for (String value : specMap.values()) {
                        spuName.append( " " ).append( value );
                    }
                }
                sku.setName( spuName.toString() );
                // 设置spuId
                sku.setSpuId( spu.getId() );
                // 设置创建与修改时间
                //FIXME: 2020/2/16 18:04 此处时间应采用数据库时间函数
                sku.setCreateTime( new Date() );
                sku.setUpdateTime( new Date() );
                // 设置商品分类id
                sku.setCategoryId( category.getId() );
                // 设置商品分类名称
                sku.setCategoryName( category.getName() );
                // 设置品牌名称
                sku.setBrandName( brand.getName() );
                // 将sku添加到数据库
                skuMapper.insertSelective( sku );
            }
        }
    }
}
