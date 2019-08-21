package com.example.demo.springaop;

import org.springframework.stereotype.Component;

@Component
public class Girl  implements  Person{

	@Override
	public void likeAnotherPerson() {
		System.out.println("我是女生，我喜欢帅哥！");
		
	}

}
