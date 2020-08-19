package com.example.demo.message;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

/**
 * @author wangxg3
 */
@Configuration
public class JmsRpcServiceServerConfig {


    @Bean
    public Destination jmsRpcDst() {
        return new ActiveMQQueue("jms.rpc.queue");
    }

    @Bean
    public IMsgRpcService msgRpcService() {
        return new MsgRpcServiceImpl();
    }


    @Bean
    public JmsInvokerServiceExporter jmsInvokerServiceExporter(IMsgRpcService msgRpcService, Destination jmsRpcDst) {
        JmsInvokerServiceExporter jmsInvokerServiceExporter = new JmsInvokerServiceExporter();
        jmsInvokerServiceExporter.setService(msgRpcService);
        jmsInvokerServiceExporter.setServiceInterface(IMsgRpcService.class);

        return jmsInvokerServiceExporter;
    }

    public DefaultMessageListenerContainer  jmsMsgRpcLi


}
