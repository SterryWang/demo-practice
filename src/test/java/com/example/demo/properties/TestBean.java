package com.example.demo.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class TestBean {
    @Value("${test.properties.user}")
    private String username;
    @Value("${test.properties.url}")
    private String url;
    @Value("${test.properties.passwd}")
    private String password;

    public void sayHello() {
        System.out.println("TestBean sayHello...");
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void start() {
        System.out.println("TestBean 初始化。。。");
    }

    public void cleanUp() {
        System.out.println("TestBean 销毁。。。");
    }
}
