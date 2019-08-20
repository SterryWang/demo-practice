package com.example.demo.dynamicProxy.tests;

import java.lang.reflect.Proxy;

import org.junit.Test;

import com.example.demo.dynamicProxy.HelloWorld;
import com.example.demo.dynamicProxy.IHelloWorld;
import com.example.demo.dynamicProxy.LoggerHandler;

public class JavaDynamicProxyTests {

	/**
	 * 代码复现动态代理增强的实现过程
	 */
	@Test
	public void simpleProxyTest() {
		// 第一步，创建被代理对象
		IHelloWorld hw = new HelloWorld();
		hw.sayHello();
		// 第二步，创建handler
		LoggerHandler handler = new LoggerHandler(hw);
		// 第三步，利用被代理对象+hander创建代理对象，代理对象实际上是和被代理对象实现了一个共同的接口，但其实是动态创建了一个不同于被代理类的另一个新的类
		try {
			IHelloWorld proxy = (IHelloWorld) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					HelloWorld.class.getInterfaces(), handler);
			proxy.sayHello();
			proxy.sayHello2();
			proxy.sayHello("alone");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
