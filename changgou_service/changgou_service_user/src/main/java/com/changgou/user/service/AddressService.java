package com.changgou.user.service;

import com.changgou.user.pojo.Address;

import java.util.List;

/**
 * 地址服务
 */
public interface AddressService {
    /**
     * 根据当前登录用户名获取与之相关的收件人地址
     * @param username
     * @return
     */
    List<Address> list(String username);
}
