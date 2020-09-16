package com.example.demo.message.amqp;

import com.example.demo.entity.Employee;

/**
 * @author wangxg3
 * 基于AMQP的Employee 消息传输服务接口
 */
public interface IRabbitEmployeeMsgService {




    void send(Employee e);
}
