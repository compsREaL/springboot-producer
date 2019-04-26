package com.real.rabbit.springboot.producer;

import com.real.rabbit.springboot.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: mabin
 * @create: 2019/4/26 11:38
 */
@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrder(Order order) throws Exception{

        //correlationData消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getId());
        rabbitTemplate.convertAndSend("order-exchange","order.abcd",order,correlationData);
    }
}
