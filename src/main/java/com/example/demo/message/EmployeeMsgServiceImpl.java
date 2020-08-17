package com.example.demo.message;

import com.example.demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.Message;

/**
 * @author wangxg3
 */

@Component
public class EmployeeMsgServiceImpl  implements   IEmployMsgService{
    private  static Logger  log   = LoggerFactory.getLogger(EmployeeMsgServiceImpl.class);

    JmsOperations jmsOperations;

    public  EmployeeMsgServiceImpl(@Qualifier("MyJmsTemplate") JmsOperations  jmsOperations){
        this.jmsOperations = jmsOperations;
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void sendEmployeeInfo(Employee e, Destination dst) {
        jmsOperations.convertAndSend(dst,e);
        if(true){
            throw new  RuntimeException("故意抛出的异常，测试jms事务！");
        }
    }

    @Override
    @Transactional
    public String recvMsg(Destination dst) {
        Message msg;
        try {
           msg =jmsOperations.receive(dst);
           log.info("接收到的信息为：{}",msg);

        } catch (Exception ex) {
           throw new RuntimeException("接收并转换消息失败！",ex);
        }
        if(true){

            throw new  RuntimeException("故意抛出的异常，测试jms接收消息的事务！");

        }
        return msg.toString();

    }
}
