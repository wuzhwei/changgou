package com.changgou.goods.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
