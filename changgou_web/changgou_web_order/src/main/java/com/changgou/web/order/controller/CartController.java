package com.changgou.web.order.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    /**
     * 查询购物车信息
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("items",cartFeign.list());
        return "cart";
    }

    /**
     * 添加商品进购物车
     * @param id
     * @param number
     * @return
     */
    public Result<Map<String,Object>> add(String id,Integer number){
        cartFeign.addCart(id,number);
        return Result.<Map<String,Object>>builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("添加购物车成功")
                .data(cartFeign.list()).build();
    }


}
