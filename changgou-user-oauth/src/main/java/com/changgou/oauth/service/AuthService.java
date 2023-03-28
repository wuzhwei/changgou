package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;
/**
* @Description: 身份验证服务
 */
public interface AuthService {
    /**
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
