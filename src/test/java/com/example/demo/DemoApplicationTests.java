package com.example.demo;

import com.example.demo.dynamicProxy.HelloWorld;
import com.example.demo.dynamicProxy.IHelloWorld;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(JUnit4.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class },webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {



	@Test
	public void contextLoads() {
		IHelloWorld hw = new HelloWorld();
	    System.out.println(hw.getClass());
	}





    
}
