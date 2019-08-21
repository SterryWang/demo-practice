package com.example.demo.springaop;

import org.springframework.stereotype.Component;

@Component
public class FemaleAnimal  implements  Animal{

	@Override
	public void eat() {
		System.out.println("我爱吃肉！");
		
	}

}
