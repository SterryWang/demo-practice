package com.example.demo.message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxg3
 * ActiveMQ消息代理配置类
 */
@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class ActiveMQConfig {

    /**
     * ActiveMQ连接工厂
     * @param properties
     * @return
     */
    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        //packages的设置是sonar提醒的，是考虑安全性的问题
        ActiveMQProperties.Packages packages = properties.getPackages();
        if (packages.getTrustAll() != null) {
            connectionFactory.setTrustAllPackages(packages.getTrustAll());
        }
        if (!packages.getTrusted().isEmpty()) {
            connectionFactory.setTrustedPackages(packages.getTrusted());
        }
       //设置消息代理服务器的地址，其实还可以用户密码，超时时间等，不赘述
        if (properties.getBrokerUrl() != null) {
            connectionFactory.setBrokerURL(properties.getBrokerUrl());
        }


        return connectionFactory;
    }


}
