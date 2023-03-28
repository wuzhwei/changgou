package com.changgou.web.gateway.service;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getJtiFromCookie(ServerHttpRequest request){
        HttpCookie httpCookie = request.getCookies().getFirst("uid");
        if (ObjectUtil.isNotEmpty(httpCookie)){
            return httpCookie.getValue();
        }
        return null;
    }

    /**
     * 令牌
     * @param jti
     * @return
     */
    public String getJwtFromRedis(String jti){
        return stringRedisTemplate.boundValueOps(jti).get();
    }
}
