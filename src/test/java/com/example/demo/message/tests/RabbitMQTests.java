package com.example.demo.message.tests;


import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import com.example.demo.message.amqp.IRabbitEmployeeMsgService;
import com.example.demo.message.amqp.RabbitBindings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RabbitMQTests {
    private static Logger log = LoggerFactory.getLogger(RabbitMQTests.class);
    @Resource
    IRabbitEmployeeMsgService rabbitEmployeeMsgService;
    @Resource(name = "myRabbitTemplate")
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testSend() throws InterruptedException {
        Employee e = new Employee();
        e.setName("jack");
        e.setAge(18);
        rabbitEmployeeMsgService.send(e);
        log.info("雇员信息发送完成！");
        //给监听器时间接收消息
        Thread.sleep(10000);

    }

    @Test
    public void testSendStr() throws InterruptedException {
        rabbitTemplate.convertAndSend(RabbitBindings.DIRECT_EXCHANGE, RabbitBindings.ROUTINGKEY_B, "你好啊");
        log.info("字符串信息发送完成！");
        //给监听器时间接收消息
        Thread.sleep(10000);

    }

    @Test
    public void testRecv() {

        Employee e = (Employee) rabbitTemplate.receiveAndConvert(RabbitBindings.QUEUE_A);
        log.info("接收到的信息为{}", e);
    }
}
