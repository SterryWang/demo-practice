package com.example.demo.message.amqp;

import com.example.demo.entity.Employee;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author wangxg3
 */
@Service
public class RabbitEmployeeMsgServiceImpl implements   IRabbitEmployeeMsgService {
    @Resource(name = "myRabbitTemplate")
    private RabbitTemplate  rabbitTemplate;
    @Override
    public void send(Employee e) {
        rabbitTemplate.convertAndSend(RabbitBindings.DIRECT_EXCHANGE,RabbitBindings.ROUTINGKEY_A,e);


    }
}
