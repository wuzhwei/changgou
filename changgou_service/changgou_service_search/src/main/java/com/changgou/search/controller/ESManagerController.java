package com.changgou.search.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ESManagerController {
    @Autowired
    private ESManagerService esManagerService;
    //创建索引库结构
    @GetMapping("/create")
    public Result create(){
        esManagerService.createMappingAndIndex();
        return new Result(true, StatusCode.OK,"创建索引结构成功");
    }
    @GetMapping("/importAll")
    public Result importAll(){
        esManagerService.importAll();
        return new Result(true,StatusCode.OK,"导入全部数据库成功");
    }
    @GetMapping("/importId/{id}")
    public Result importId(@PathVariable String id){
        esManagerService.importDataBySpuId(id);
        return new Result(true,StatusCode.OK,"导入全部数据库成功");
    }
}
