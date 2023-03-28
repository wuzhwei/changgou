package com.changgou.oauth.controller;

import com.aliyuncs.utils.StringUtils;
import com.changgou.common.pojo.Result;
import com.changgou.common.pojo.StatusCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;
    @PostMapping("/login")
    public Result login(String username,String password){
        if (StringUtils.isEmpty(username)){
            throw new RuntimeException("用户名不存在");
        }
        if (StringUtils.isEmpty(password)){
            throw new RuntimeException("密码不存在");

        }
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        this.saveJtiToCookie(authToken.getJti());
        return  new Result(true, StatusCode.OK,"登录成功");

    }

    /**
     * 保存cookie
     * @param jti
     */
    private void saveJtiToCookie(String jti) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jti,cookieMaxAge,false);
    }
}
