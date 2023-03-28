package com.changgou.oauth.service.Impl;

import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${auth.ttl}")
    private long ttl;

    /**
     * 申请令牌
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */


    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //申请令牌
        ServiceInstance choose = loadBalancerClient.choose("user-auth");
        URI uri = choose.getUri();
        String url = uri + "/oauth/token";
        MultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        MultiValueMap<Object, Object> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization",this.getHttpBasic(clientId,clientSecret));
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity(body, headers);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = responseEntity.getBody();
        if (map == null || map.size() < 0){
            //申请令牌失败
            throw new RuntimeException("申请令牌失败");

        }
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken((String) map.get("access_token"));
        authToken.setRefreshToken((String)map.get("refresh_token"));
        authToken.setJti((String)map.get("jti"));
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(authToken.getAccessToken(),
                ttl, TimeUnit.SECONDS);
        return authToken;
    }

    private Object getHttpBasic(String clientId, String clientSecret) {
        String value = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic "+new String(encode);
    }
}
