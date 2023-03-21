package com.changgou.goods.controller;

import com.changgou.common.pojo.PageResult;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    public BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",brandList);

    }

    /**
     * 根据id查询品牌数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",brand);

    }

    /**
     * 新增一个品牌数据
     * @param brand
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加成功");

    }

    /**
     * 修改商品
     * @param brand
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Brand brand,@PathVariable Integer id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 根据id删除品牌
     * @param id
     * @return
     */
    @DeleteMapping(value ="/{id}")
    public Result delete(@PathVariable Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Brand> list = brandService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 分页搜索实现
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value="/search/{page}/{size}")
    public Result findPage(@PathVariable int page,@PathVariable int size){
        Page<Brand> pageList = brandService.findPage(page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);

    }

    /**
     * 分页条件搜索实现
     * @param search
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/searchPage/{page}/{size}")
    public Result findPage(@RequestParam Map search,@PathVariable int page,@PathVariable int size){
        Page<Brand> pageList = brandService.findPage(search, page, size);
        PageResult<Brand> pageResult = new PageResult<>(pageList.getTotal(), pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


}
