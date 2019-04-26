package com.real.rabbit.springboot.service;

import com.real.rabbit.springboot.constant.Constants;
import com.real.rabbit.springboot.dao.BrokerMessageLogDao;
import com.real.rabbit.springboot.dao.OrderDao;
import com.real.rabbit.springboot.entity.BrokerMessageLog;
import com.real.rabbit.springboot.entity.Order;
import com.real.rabbit.springboot.producer.RabbitOrderSender;
import com.real.rabbit.springboot.utils.FastJsonConvertUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/4/26 17:55
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private BrokerMessageLogDao brokerMessageLogDao;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;



    public void createOrder(Order order){

        Date orderTime = new Date();
        //业务数据入库
        orderDao.insert(order);
        //构建消息日志记录对象
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        brokerMessageLog.setMessageId(order.getMessageId());
        //把order对象转化为json字符串
        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
        brokerMessageLog.setStatus("0");
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogDao.insert(brokerMessageLog);
        rabbitOrderSender.sendOrder(order);
    }

}
