package com.changgou.goods.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/spu")
@RestController
public class SpuController {
    @Autowired
    SpuService spuService;
    @PostMapping
    public Result add(@RequestBody Goods goods){
        spuService.add(goods);
        return new Result().builder().flag(true).code(StatusCode.OK).message("添加成功").build();
    }

    /**
     * 根据SpuId查询goods商品   goods=spu+skuList
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        Goods goods = spuService.findGoodsById(id);
        return Result.builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("查询成功")
                .data(goods).build();
    }

    /**
     * 根据spuId更新商品信息
     * @param goods
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Goods goods,@PathVariable String id){
        spuService.update(goods);
        return Result.builder().flag(true).code(StatusCode.OK).message("修改成功").build();
    }

}
