package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@EnableAspectJAutoProxy//此注解可加可不加，因为是SPRING 默认是将它开启的
@SpringBootApplication(scanBasePackages="com.example.demo",exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("启动类启动啦！");
		SpringApplication.run(DemoApplication.class, args);
		
	}
}
