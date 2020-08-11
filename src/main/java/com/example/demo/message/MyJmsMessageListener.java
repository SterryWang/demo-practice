package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;


/**
 * 一个实现了MessageListener接口的监听器
 */
public class MyJmsMessageListener implements MessageListener {
    private static Logger log = LoggerFactory.getLogger(MyJmsMessageListener.class);

    @Override
    public void onMessage( Message message) {

        log.info("接收到的MESSAGE类型为：{}", message.getClass());
        if (message instanceof TextMessage) {

            try {
                log.info("接收到的信息为：{}", ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else if (message instanceof ObjectMessage) {
            try {
                log.info("接收到的对象为：{}", ((ObjectMessage) message).getObject());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            log.info("接收的信息为：{}", message);
        }

    }
}
