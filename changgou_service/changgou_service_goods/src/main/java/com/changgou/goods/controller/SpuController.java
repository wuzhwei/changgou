package com.changgou.goods.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
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
     * 根据ID查询Spu数据
     *
     * @param id Spu id
     * @return Spu信息集合
     */
    @GetMapping("/findSpuById/{id}")
    public Result<Spu> findSpuById(@PathVariable("id") String id) {
        return Result.<Spu>builder()
                .flag( true )
                .code( StatusCode.OK )
                .message( "查询成功" )
                .data( spuService.findById( id ) ).build();
    }

    /**
     * 根据spuId更新商品信息
     * @param goods
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Goods goods,@PathVariable String id){
        goods.getSpu().setId(id);
        spuService.update(goods);
        return Result.builder().flag(true).code(StatusCode.OK).message("修改成功").build();
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable String id){
        spuService.audit(id);
        return new Result();

    }

    /**
     * 下架
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable String id){
        spuService.pull(id);
        return new Result();
    }

    /**
     * 上架
     * @param id
     * @return
     */
    @PutMapping("/push/{id}")
    public Result push(@PathVariable String id){
        spuService.push(id);
        return new Result();
    }

    /**
     * 恢复数据
     * @param id
     * @return
     */
    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable String id){
        spuService.restore(id);
        return new Result();
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    @DeleteMapping("/realDelete/{id}")
    public Result realDelete(@PathVariable String id){
        spuService.realDelete(id);
        return new Result();
    }




}
