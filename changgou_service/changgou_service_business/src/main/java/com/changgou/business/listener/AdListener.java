package com.changgou.business.listener;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 广告数据变换监听类
 */
@Component
@Slf4j
public class AdListener {
    @RabbitListener(queues = "ad_update_queue")
    public void receiveMessage(String message){
        log.info( "接收到的消息为：{}", message );
        //发起远程调用
        String url = "http://192.168.31.6/ad_update?position=" + message;
        String s = HttpUtil.get( url );
        log.info( "返回结果为：{}", s );

    }
}
