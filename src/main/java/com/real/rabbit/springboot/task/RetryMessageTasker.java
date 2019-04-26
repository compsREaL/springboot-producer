package com.real.rabbit.springboot.task;

import com.real.rabbit.springboot.constant.Constants;
import com.real.rabbit.springboot.dao.BrokerMessageLogDao;
import com.real.rabbit.springboot.entity.BrokerMessageLog;
import com.real.rabbit.springboot.entity.Order;
import com.real.rabbit.springboot.producer.RabbitOrderSender;
import com.real.rabbit.springboot.utils.FastJsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/26 18:13
 */
@Component
public class RetryMessageTasker {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;
    @Autowired
    private BrokerMessageLogDao brokerMessageLogDao;

    @Scheduled(initialDelay = 3000,fixedDelay = 10000)
    public void reSend(){
        System.err.println("-------定时任务开始----------");
        List<BrokerMessageLog> brokerMessageLogList = brokerMessageLogDao.query4StatusAndTimeoutMessage();
        brokerMessageLogList.forEach(brokerMessageLog -> {
            if (brokerMessageLog.getTryCount()>=3){
                brokerMessageLogDao.changeBrokerMessageLogStatus(brokerMessageLog.getMessageId(), Constants.ORDER_SEND_FAILURE,new Date());
            } else {
                brokerMessageLogDao.update4ReSend(brokerMessageLog.getMessageId(),new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(brokerMessageLog,Order.class);
                try {
                    rabbitOrderSender.sendOrder(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("---------异常处理---------");
                }
            }
        });
    }
}
