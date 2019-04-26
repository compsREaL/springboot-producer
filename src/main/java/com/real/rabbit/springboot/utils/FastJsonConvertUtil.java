package com.real.rabbit.springboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.rabbit.springboot.entity.BrokerMessageLog;
import com.real.rabbit.springboot.entity.Order;

/**
 * @author: mabin
 * @create: 2019/4/26 18:02
 */
public class FastJsonConvertUtil {
    public static String convertObjectToJSON(Object object){
        try {
            String text = JSON.toJSONString(object);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Order convertJSONToObject(BrokerMessageLog brokerMessageLog, Class<Order> orderClass) {
        Order order = JSONObject.parseObject(brokerMessageLog.getMessage(),orderClass);
        return order;
    }
}
