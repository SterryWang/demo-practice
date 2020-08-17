package com.example.demo.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 测试SPRING BOOT 读取和使用自定义配置文件
 */
@PropertySource(value="classpath:test.properties")
@ConfigurationProperties(prefix= "test.properties")
@Component
public class TestProperties {

    private String url;
    private String user;
    private String  passwd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString() {
        return "TestProperties{" +
                "url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
