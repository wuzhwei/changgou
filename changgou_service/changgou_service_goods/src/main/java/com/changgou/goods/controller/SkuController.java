package com.changgou.goods.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 多条件搜索Sku数据
     *
     * @param searchMap 搜索条件
     * @return Sku数据
     */
    @GetMapping("/search")
    public Result<List<Sku>> findList(@RequestParam Map<String, Object> searchMap) {
        return Result.<List<Sku>>builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "查询成功" )
                .data( skuService.findList( searchMap ) ).build();
    }
    /**
     * 查询商品
     *
     * @param spuId 商品id
     * @return 商品数据
     */
    @GetMapping("/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId) {
        Map<String, Object> searchMap = new HashMap<>( 0 );
        if (!"all".equals( spuId )) {
            searchMap.put( "spuId", spuId );
        }
        searchMap.put( "status", "1" );
        return skuService.findList( searchMap );
    }
}
