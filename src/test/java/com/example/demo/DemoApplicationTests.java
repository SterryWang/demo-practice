package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.dynamicProxy.HelloWorld;
import com.example.demo.dynamicProxy.IHelloWorld;
@RunWith(JUnit4.class)
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
		IHelloWorld hw = new HelloWorld();
	    System.out.println(hw.getClass());
	}
    
}
