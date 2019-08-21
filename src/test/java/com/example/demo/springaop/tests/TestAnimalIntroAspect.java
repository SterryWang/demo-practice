package com.example.demo.springaop.tests;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.springaop.Animal;
import com.example.demo.springaop.Person;

/**
 * 测试引入的AOP功能
 * {@link com.example.demo.springaop.AnimalIntroductionAspect}
 * @author wangxg3
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class })
public class TestAnimalIntroAspect {
	
	
	@Resource
	private  Person girl;
	
	
	@Test
	public void test1() {
		
		girl.likeAnotherPerson();
		
		//测试其引入的ANIMAL接口特性
		
		Animal  femaleAnimal  = (Animal)girl;
		
		femaleAnimal.eat();
		
	}

}
