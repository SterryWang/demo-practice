package com.example.demo.message;


import com.sun.jndi.ldap.pool.PooledConnectionFactory;
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
        factory.setConnectionFactory(cf);
        //factory.setPubSubDomain(true);

      //factory.setClientId("pojolistenercontainer");
      //  factory.setSubscriptionDurable(true);
        //DestinationResolver有默认值 DynamicDestinationResolver,所以无需设置
        //factory.setDestinationResolver(destinationResolver());
        // factory.setSessionTransacted(true);
      factory.setConcurrency("2");
      factory.setMaxMessagesPerTask(2);


        return factory;

    }


    @Bean
    public  ConnectionFactory  consumerCachingConnectionFactory(ConnectionFactory  cf){
      CachingConnectionFactory ccf  = new CachingConnectionFactory(cf);
      ccf.setSessionCacheSize(50);

      //ccf.setClientId("lalala");

      return ccf ;

    }


}
