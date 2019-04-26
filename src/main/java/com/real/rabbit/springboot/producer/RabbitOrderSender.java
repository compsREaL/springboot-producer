package com.real.rabbit.springboot.producer;

import com.real.rabbit.springboot.constant.Constants;
import com.real.rabbit.springboot.dao.BrokerMessageLogDao;
import com.real.rabbit.springboot.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/4/26 15:48
 */
@Component
public class RabbitOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogDao brokerMessageLogDao;

    final ConfirmCallback comfirmCallBack = new RabbitTemplate.ConfirmCallback(){

        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.err.println("correlationData:"+correlationData);
            String messageId = correlationData.getId();
            if (b){
                //如果confirm返回成功则进行更新
                brokerMessageLogDao.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS,new Date());
            } else {
                //失败则进行后续操作：重试或者补偿等手段
                System.out.println("异常处理");
            }
        }
    };

    //发送消息方法调用：构建自定义对象消息
    public void sendOrder(Order order){
        rabbitTemplate.setConfirmCallback(comfirmCallBack);
        //消息唯一ID
        CorrelationData correlationData= new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange1","order.ABC",order,correlationData);
    }
}
