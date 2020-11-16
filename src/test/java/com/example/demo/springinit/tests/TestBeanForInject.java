package com.example.demo.springinit.tests;

/**
 * @author ：Sterry
 * @description：用于注入TestInitBean的测试类
 * @date ：2020/11/9 13:28
 */
public class TestBeanForInject {
    String  name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestBeanForInject{" +
                "name='" + name + '\'' +
                '}';
    }
}
