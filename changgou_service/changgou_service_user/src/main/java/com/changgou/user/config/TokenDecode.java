package com.changgou.user.config;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenDecode {
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY="public.key";
    private static String publickey ="";

    /**
     * 获取用户信息
     * @return
     */
    public Map<String,String> getUserInfo(){
        //获取授权信息
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return dcodeToken(details.getTokenValue());
    }

    /**
     * 读取令牌信息
     * @param tokenValue
     * @return
     */
    private Map<String, String> dcodeToken(String tokenValue) {
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(tokenValue,new RsaVerifier(getPubKey()));
        //获取Jwt原始内容
        String claims = jwt.getClaims();
        return JSON.parseObject(claims,Map.class);


    }

    /**
     * 获取非对称加密公钥 Key
     * @return  公钥  Key
     */

    private String getPubKey() {
        if (!StringUtils.isEmpty(publickey)){
            return publickey;
        }
        try {
            Resource resource = new ClassPathResource(PUBLIC_KEY);
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            publickey = br.lines().collect(Collectors.joining("\n"));
            return publickey;
        }catch (IOException ioe){
            return null;
        }
    }
}
