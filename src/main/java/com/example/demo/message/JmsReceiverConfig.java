package com.example.demo.message;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsReceiverConfig {

    @Bean
    public JmsListenerContainerFactory pojoJmsListenerContainer(@Qualifier("consumerCachingConnectionFactory") ConnectionFactory cf) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        //注入连接工厂
        factory.setConnectionFactory(cf);
        //设置默认域为订阅模式
        factory.setPubSubDomain(false);

        //设置持久化订阅，需要consumerCachingConnectionFactory()设置clientID;
        // factory.setClientId("pojolistenercontainer");
        factory.setSubscriptionDurable(true);

        //DestinationResolver有默认值 DynamicDestinationResolver,所以无需设置
        //factory.setDestinationResolver(destinationResolver());

        //设置事务
         factory.setSessionTransacted(true);
        //开启并发,也就是设置多消费者，它和持久化订阅是冲突的，因为持久化订阅要求clientID唯一
        //factory.setConcurrency("2");

        //每个线程处理n个消息后使用新线程，可以不设置
        // factory.setMaxMessagesPerTask(2);


        return factory;

    }


    @Bean
    public ConnectionFactory consumerCachingConnectionFactory(ConnectionFactory cf) {
        CachingConnectionFactory ccf = new CachingConnectionFactory(cf);
        //设置会话缓存数量，如果监听器容器开启了并发，可以提高会话缓存数量来提高效率
        ccf.setSessionCacheSize(50);
        //持久化订阅需要设置clientID,
        ccf.setClientId("lalala");

        return ccf;

    }


}
