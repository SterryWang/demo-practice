package com.example.demo.dynamicProxy;

public class HelloWorld  implements  IHelloWorld {
	@LogHanderAnnotation(ignore=true)
	@Override
       public void sayHello() {
    	   System.out.println("hello world!");
       }

	@Override
	@LogHanderAnnotation
	public void sayHello2() {
		System.out.println("hello  world2");
		
	}

	@Override
	public void sayHello(String msg) {
		
		System.out.println("hello world with params"+msg);
		
	}
}
