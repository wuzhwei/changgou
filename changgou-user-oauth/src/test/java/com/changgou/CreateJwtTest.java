package com.changgou;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

public class CreateJwtTest {
    /**
     * 创建令牌测试
     */
    @Test
    public void testCreateToken(){
        //证书文件路径
        String key_location="changgou.jks";
        //秘钥库密码
        String key_password = "changgou";
        //秘钥别名
        String alias = "changgou";
//        访问证书路径
        ClassPathResource classPathResource = new ClassPathResource(key_location);
        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, key_password.toCharArray());
        //读取秘钥对(公钥，私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //获取私钥
        RSAPrivateKey rsaPrivate = (RSAPrivateKey)keyPair.getPrivate();
        //定义PayLoad
        HashMap<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id","1");
        tokenMap.put("name","changgou123456");
        tokenMap.put("roles","ROLE_VIP,ROLE_USER");
        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));
        //取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }

}
