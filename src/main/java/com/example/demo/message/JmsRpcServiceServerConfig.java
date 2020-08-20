package com.example.demo.message;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    /**
     * 实例化消息目的地
     *
     * @return
     */
    @Bean
    public Queue jmsRpcDst() {
        return new ActiveMQQueue("jms.rpc.queue");
    }

    /**
     * 实例化待导出的RPC服务
     *
     * @return
     */
    @Bean
    public IMsgRpcService msgRpcService() {
        return new MsgRpcServiceImpl();
    }

    /**
     * 实例化JMS RPC服务 exporter
     * 本质上也是一个消息监听器
     *
     * @param msgRpcService
     * @param jmsRpcDst
     * @return
     */
    @Bean
    public JmsInvokerServiceExporter jmsInvokerServiceExporter(IMsgRpcService msgRpcService, Destination jmsRpcDst) {
        JmsInvokerServiceExporter jmsInvokerServiceExporter = new JmsInvokerServiceExporter();
        jmsInvokerServiceExporter.setService(msgRpcService);
        jmsInvokerServiceExporter.setServiceInterface(IMsgRpcService.class);


        return jmsInvokerServiceExporter;
    }

    /**
     * 消息监听器容器
     *
     * @param cf
     * @param jmsRpcDst
     * @param jmsInvokerServiceExporter
     * @return
     */
    @Bean
    public DefaultMessageListenerContainer rpcMsgListenerContainer(@Qualifier("MyActiveMQCF") ConnectionFactory cf, Destination jmsRpcDst, JmsInvokerServiceExporter jmsInvokerServiceExporter) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setDestination(jmsRpcDst);
        container.setConnectionFactory(cf);
        container.setMessageListener(jmsInvokerServiceExporter);

        return container;
    }


}
