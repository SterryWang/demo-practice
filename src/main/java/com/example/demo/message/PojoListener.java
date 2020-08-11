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


    @JmsListener(containerFactory = "pojoJmsListenerContainer",destination = "jms.queue")
    public void receiveMsg(Message msg){
       log.info("pojolistener 接收到消息:{}",msg);
       log.info("延时中。。。");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("延时完成，该则消息处理结束！");
    }

  /* @JmsListener(containerFactory = "myJmsListenerContainer", destination = "jms.topic")
    public void receiveMsg(Employee e) {
        log.info("pojolistener 接收到消息:{}" , e);
    }*/
}
