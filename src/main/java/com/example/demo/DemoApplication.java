package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@EnableAspectJAutoProxy//此注解可加可不加，因为是SPRING 默认是将它开启的
@SpringBootApplication(scanBasePackages="com.example.demo",exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class,RabbitAutoConfiguration.class})
//@PropertySource(value= "classpath:test.properties")//用于解析自定义的属性配置文件
public class DemoApplication {



	public static void main(String[] args) {
		System.out.println("启动类启动啦！");
		SpringApplication.run(DemoApplication.class, args);

		
	}

}
