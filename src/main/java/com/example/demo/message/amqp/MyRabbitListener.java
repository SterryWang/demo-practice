package com.example.demo.message.amqp;


import com.example.demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wangxg3
 */
@Component
public class MyRabbitListener {
    private static Logger log = LoggerFactory.getLogger(MyRabbitListener.class);


    @RabbitListener(queues = RabbitBindings.QUEUE_A, containerFactory = "myRabbitListenerContainer")
    public void receiver1(Message e) {
        log.info("监听器1从队列A接收到消息：{}", e);
    }


    @RabbitListener(queues = RabbitBindings.QUEUE_B, containerFactory = "myRabbitListenerContainer")
    public void receiver2(Message msg) {
        log.info("监听器2从队列B接收到消息：{}", msg);
    }

}
