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
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJjaGFuZ2dvdTEyMzQ1NiIsImlkIjoiMSJ9.HQgdWg0NcZVqDbc2ynKXCbkOKaoARPMCCUlF3ycegi1IcDfaksrW28NAl4Ug43uROPgyJkzt1EoN4ekzyp0JRrnZh_IhVN8IQjq2wkYEPKogv6iKpZclAYVm5f4H58AB8JsT3iZWgAT_OQb_6mbxEulxC8cqGLMUhTmjW3pC5UTzSe7l5qZQy81rZRZSg94Zv6mVv3tmHLM-_bjbC1-_s8nCXqDSJuvB3uQjwZsBLbJkdPnQg_7LzSR8uV02zgUbdkWLA3V0qwmSzNUryxN9UtdXkNlmMZwBsmIiL4TZTBZ7dnIwKZawJ1flGs1s_n-psSMsPOFEpwzkInhx3UsPow";
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
