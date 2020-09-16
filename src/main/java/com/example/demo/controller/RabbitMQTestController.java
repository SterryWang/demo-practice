package com.example.demo.controller;

import com.example.demo.message.amqp.RabbitBindings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangxg3
 */
@RestController
@RequestMapping("/test/rabbitmq")
public class RabbitMQTestController {
    @Resource
    RabbitTemplate myRabbitTemplate;

    @GetMapping("/todirect")
    public String sendToDirect() {
        myRabbitTemplate.convertAndSend(RabbitBindings.DIRECT_EXCHANGE, RabbitBindings.ROUTINGKEY_A, "此消息发往direct");
        return "发送成功";

    }

    @GetMapping("/tofanout")
    public String sendingToFanoutExchange() {
        myRabbitTemplate.convertAndSend(RabbitBindings.FANOUT_EXCHANGE, null, "此条信息发往fanout");
        return "发送成功";

    }

    @GetMapping("/totopic")
    public String sendingToTopic() {
        myRabbitTemplate.convertAndSend(RabbitBindings.TOPIC_EXCHANGE, RabbitBindings.ROUTINGKEY_A, "此条信息发往topic exchange");
        return "发送成功！";
    }

}
