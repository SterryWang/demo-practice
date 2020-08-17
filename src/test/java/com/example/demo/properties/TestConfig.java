package com.example.demo.properties;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@ComponentScan(basePackages = "com.example.demo.properties")
//@EnableConfigurationProperties(TestProperties.class)
public class TestConfig{
    private TestBean testBean;
    private TestProperties properties;
    public TestConfig(TestBean testBean,   @Nullable TestProperties  properties){
        this.testBean=testBean;
        this.properties=properties;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public TestProperties getProperties(){
        return properties;
    }

    @Override
    public String toString() {
        return "TestConfig{" +
                "testBean=" + testBean +
                ", properties=" + properties +
                '}';
    }
}
