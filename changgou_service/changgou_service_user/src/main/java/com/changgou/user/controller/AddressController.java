package com.changgou.user.controller;

import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.Address;
import com.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 根据当前的登录用户名获取与之相关的收件人地址
     * @return  登录人所属收件人地址
     */
    @GetMapping("/list")
    public Result<List<Address>> list(){
        //获取当前的登录人名称
        String username = tokenDecode.getUserInfo().get("username");
        //查询收件人地址信息返回
        return Result.<List<Address>>builder()
                .flag(true)
                .code(StatusCode.OK)
                .message("查询成功")
                .data(addressService.list(username)).build();

    }
}
