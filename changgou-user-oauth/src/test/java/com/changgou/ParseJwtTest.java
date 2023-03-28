package com.changgou;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    /**
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJjaGFuZ2dvdSIsImlkIjoiMSJ9.bxqS1nasL_4d2hN9N1g_lT2WVqVjvIx51UXBU5zPD-PqMAmDT0ChN4LLfQviP9jTBGpp7ZkRAz0yLl1d68BP0SFtRjILfB6Gvm2YQ2xsFHBAbpTOSdOV8plNXR4JFPZXSxzAyaQyJai7wsVI2OqOI-f3krqKxazGvxhbRVBX8DZY-cijm92cjV7tBPCYeo9csMd_LTbXYXWGBZ9s-YNqvC6g6zKuSVCN-xEcrhrRfwHOSXvRwAMalGczufyQH3KkXAjESf4Vy-u5F0TxUi1Lm4Y6ilvLbQjHYbkZa6sXVso2ezemxXN14_qseI0FnrMes_lwgXaXeJu9HR7quo4Qsg";
        //公钥
        String publicKey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApGFI3RpSYQ5lkg1+prsU1qM/GsDyrXGKB76cv+H9uMpYufDZtnWqH09ivoULCuYeRJetsBuZlfdCVM5ZcXQXK5wmS3za7SvMAzayiu23IUx1x8USR6GG7EkeD4BwEjbMsR745CAESpdhj7lbIEJ2s8dNaCNu/zs5oKiHkBs1xXUauQfEmIJgWU4w9iHYOqEIRTBRyRNKbkr/lK1H8qK5b1hAy6/xTOHRo1R/QObIXJ+gl77xPgjGwLR94+PpHkNRYhbLyTa12hdGiEfYMLXH7HMUEtCzCnUkGHTNydggda2Soebuvw0DGqUda4qNrJm7Vo6utyH3ZMlWjkUz/qJGoQIDAQAB-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //Jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }
}
