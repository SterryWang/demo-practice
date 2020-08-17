package com.example.demo.message;


import com.example.demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * 不实现messageListener接口的监听器
 */
@Component
public class PojoListener {

    private static Logger log = LoggerFactory.getLogger(PojoListener.class);


    @JmsListener(containerFactory = "pojoJmsListenerContainer", destination = "jms.topic")
    public void receiveMsg(Message msg) {
        log.info("pojolistener 接收到消息:{}", msg);
        if(true){
            throw new RuntimeException("故意抛出异常，请检查事务是否回滚！");
        }
        if(true){
            throw  new  RuntimeException("故意抛出的异常！");
        }
        log.info("延时中。。。");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("延时完成，该则消息处理结束！");
    }
/*
   @JmsListener(containerFactory = "pojoJmsListenerContainer", destination = "jms.topic")
    public void receiveMsg(Employee e) {
        log.info("pojolistener 接收到消息:{}" , e);
    }*/
}
