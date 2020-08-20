package com.example.demo.message;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * 基于jms的RPC服务的客户端配置
 *
 * @author wangxg3
 */
@Configuration
public class JmsRpcClientConfig {
    /**
     * jms的Rpc调用的代理bean工厂
     * 该工厂会通过代理技术，
     * 实例化一个实现了接口{@link  JmsInvokerProxyFactoryBean#setServiceInterface(Class xInterface)} XInterface的代理类
     *
     * @param cf
     * @param jmsRpcDst
     * @return
     */
    @Bean
    public JmsInvokerProxyFactoryBean jmsRpcClient(@Qualifier("MyActiveMQCF") ConnectionFactory cf, Queue jmsRpcDst) {
        JmsInvokerProxyFactoryBean jmsInvokerProxyFactoryBean = new JmsInvokerProxyFactoryBean();
        //注入工厂
        jmsInvokerProxyFactoryBean.setConnectionFactory(cf);
        //注入目标队列
        jmsInvokerProxyFactoryBean.setQueue(jmsRpcDst);
        //注入
        //jmsInvokerProxyFactoryBean.setServiceInterface(IMsgRpcService.class);
        jmsInvokerProxyFactoryBean.setServiceInterface(IMsgRpcServiceClientCopy.class);


        return jmsInvokerProxyFactoryBean;

    }


}
