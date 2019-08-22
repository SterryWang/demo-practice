package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
@EnableAspectJAutoProxy//此注解可加可不加，因为是SPRING 默认是将它开启的
@SpringBootApplication(scanBasePackages="com.example.demo")
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("启动类启动啦！");
		SpringApplication.run(DemoApplication.class, args);
		
	}
}
