package com.example.demo.redis;


import com.example.demo.entity.Employee;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * reids配置类，包括缓存管理器，redisTemplate，连接工厂，连接池配置
 * 其实缓存管理器我们再缓存那一章已经学过了其他的缓存管理器:
 * {@link com.example.demo.springcache.CachingConfig}
 *
 *
 */
@Configuration
@EnableCaching
public class RedisConfig {


    @Bean("redisCacheManager")
    @Primary
    public CacheManager redisCacheManager(@Qualifier("jedisCF") RedisConnectionFactory redisCF){
        /**
         * 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
         */
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        //RedisSerializer<Object>  jacksonSeial= new GenericJackson2JsonRedisSerializer();
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        /**
         *  装在redisCacheManager配置类，设置超时时间，序列化器
         */

        //正确写法
        RedisCacheConfiguration  config = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(3))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSeial))
                .disableCachingNullValues();

        /**
         * 错误写法示范：config.entryTtl等后面一系列的方法，并不是类似set方法，它的返回其实是一个new出来的新对象
         * 这样子的话， “return RedisCacheManager.builder(redisCF).cacheDefaults(config).build();”这一句，
         * 依然使用config作为设置类，那么后面那一串设置方法则已经无效了
         */


       /* RedisCacheConfiguration  config = RedisCacheConfiguration
                       .defaultCacheConfig();
         config.entryTtl(Duration.ofMinutes(3));
               .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSeial))
                .disableCachingNullValues();*/
        RedisCacheManager  redisCacheManager  = RedisCacheManager.builder(redisCF).cacheDefaults(config).build();
       //开启事务支持
        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;


    }

    /**
     * 创建redis连接工厂bean
     * 可以设置连接到Redis服务器的参数
     *
     * @return
     */
    @Bean(value = "jedisOldCF")
public RedisConnectionFactory   redisCFOld(){
        //SPRIGN BOOT 1.7以后，jedisConnectionFactory，不再推荐使用了，可以使用RedisStandaloneConfiguration
    JedisConnectionFactory  cf  = new JedisConnectionFactory();

   //设置主机和端口号,在spring 2.0里，这种写法已经过时
    //cf.setHostName("::1");
        cf.setHostName("wslip");
    cf.setPort(6379);
    //cf.setPassword("1234"); //默认没有密码
    return cf;
}


    /**
     * 创建redisTemplate，显然此模板和Employee实体类捆绑了
     *
     * @param cf
     * @return
     */
    @Bean("redisTemplate1")
public RedisTemplate<String, Employee>  redisTemplate1(  @Qualifier("jedisOldCF") RedisConnectionFactory cf)  {
        RedisTemplate<String,Employee>   redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
}

@Bean("redisTemplateN")
    public RedisTemplate<String,Object> redisTemplate(  @Qualifier("jedisCF") RedisConnectionFactory cf)  {
        RedisTemplate<String,Object>  template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        //设置序列化器
  //  redisTemplate.setDefaultSerializer(new StringRedisSerializer());
   // redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
    Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

    ObjectMapper om = new ObjectMapper();
    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jacksonSeial.setObjectMapper(om);

    // 值采用json序列化
    template.setValueSerializer(jacksonSeial);
    //使用StringRedisSerializer来序列化和反序列化redis的key值
    template.setKeySerializer(new StringRedisSerializer());

    // 设置hash key 和value序列化模式
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(jacksonSeial);
    //开启REDIS事务支持
    template.setEnableTransactionSupport(true);


    template.afterPropertiesSet();
        return template;
    }
    /**
     * jedis连接池配置
     * @return
     */
    @Bean("jedisPoolCfg")
public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(100);
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(20);
        //当池内没有可用的连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMaxIdle(30);
        //------其他属性根据需要自行添加-------------
        return jedisPoolConfig;

}

@Bean("jedisCF")
public RedisConnectionFactory   jedisCF(@Qualifier("jedisPoolCfg") JedisPoolConfig  jedisPoolConfig){
    //单机版jedis配置类
    RedisStandaloneConfiguration redisStandaloneConfiguration =
            new RedisStandaloneConfiguration();
    //设置redis服务器的host或者ip地址
    redisStandaloneConfiguration.setHostName("wslip");
    //设置默认使用的数据库
    redisStandaloneConfiguration.setDatabase(0);
    //设置密码
    //redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
    //设置redis的服务的端口号
    redisStandaloneConfiguration.setPort(6379);


    //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
    JedisClientConfiguration jedisClientConfiguration =
           JedisClientConfiguration.builder().usePooling()
            .poolConfig(jedisPoolConfig).build();



    //单机配置 + 客户端配置 = jedis连接工厂
    return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);


    }

}
