package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.ESManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsDelListener {
    @Autowired
    private ESManagerService esManagerService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_DEL_QUEUE)
    public void receiveMessage(String spuId){
        log.info("接收到新下架商品id:{}",spuId);
        //查询skuList,并从索引库删除数据
        esManagerService.delDataBySpuId(spuId);
    }

}
