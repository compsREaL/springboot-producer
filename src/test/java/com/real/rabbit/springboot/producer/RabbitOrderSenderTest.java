package com.real.rabbit.springboot.producer;

import com.real.rabbit.springboot.entity.Order;
import com.real.rabbit.springboot.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @author: mabin
 * @create: 2019/4/26 11:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitOrderSenderTest {

    @Autowired
    private OrderSender rabbitOrderSender;

    @Test
    public void sendOrder() throws Exception{
        Order order = new Order();
        order.setId("201904260000000001");
        order.setName("test-order");
        order.setMessageId(System.currentTimeMillis()+ "$" + UUID.randomUUID().toString());
        rabbitOrderSender.sendOrder(order);
    }

    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder(){
        Order order=new Order();
        order.setId("20190426202600000003");
        order.setName("test");
        order.setMessageId(System.currentTimeMillis()+"$"+UUID.randomUUID().toString());
        orderService.createOrder(order);
    }
}