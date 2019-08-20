package com.example.demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

/**
 * 测试自定义注解，以及多重注解的鉴别，从而选择性的执行方法
 * 可以将之前学习到的动态代理结合起来，注解+动态代理，实现注解式的动态代理
 * @author Administrator
 *
 */
public class SelfDefineTestUtil {

	public static void main(String[] args) throws Exception {

		CalculateTest calculateTest = new CalculateTest();
		runSelfDefineTest(calculateTest);

	}
   /**
    * SelfDefinedTestS注解的解释执行器，可以和后面一个比较，有所不同，但效果相同
    * @param obj
    * @throws Exception
    */
	private static void runSelfDefineTest(Object obj) throws Exception {
		System.out.println("共计有几个方法："+obj.getClass().getDeclaredMethods().length);

		for (Method method : obj.getClass().getDeclaredMethods()) {
			// 获取当前方法上的所有注解
			Annotation[] annotations = method.getDeclaredAnnotations();
			System.out.println("当前方法名称为"+method.getName());

			if (annotations != null && annotations.length > 0) {
				for (Annotation annotation : annotations) {
					System.out.println(method.getName() + "的当前注解的类为" + annotation.annotationType());
					// 判断当前方法上所有注解的属性，选择性执行
					
					if (annotation.annotationType().equals(SelfDefinedTest.class)) {
						SelfDefinedTest selfDefinedTest = (SelfDefinedTest) annotation;
						System.out.println(method.getName() + "方法上含有@SelfDefinedTest注解，");
						if (selfDefinedTest.ignore()) {
							System.out.println("该注解属性为ignore=true,故当前方法" + method.getName() + "不执行测试");
						} else {
							System.out.println("该注解属性 ignore = false,故当前方法" + method.getName() + "执行测试:");
							method.invoke(obj,null);
						}
					}
					if (annotation.annotationType().equals(Test.class)) {
						Test Test = (Test) annotation;
						System.out.println(method.getName() + "方法上含有@Test注解，故暂时不执行测试");

					}
				}

			}

		}

	}
	
	/**
	 * 方法注解执行器：利用反射的原理负责解释添加在某个类的某个方法上的自定义测试注解@SelfDefinedTest
	 *
	 * @param obj
	 * @throws Exception
	 */

	private static void runSelfDefineTest2(Object obj) throws Exception {
		System.out.println("共计有几个方法："+obj.getClass().getDeclaredMethods().length);
        //首先第一步获取对象的方法，也可以获取指定方法getDeclaredMethod
		for (Method method : obj.getClass().getDeclaredMethods()) {

              //第二步，判断指定方法上是否含有某个指定类型的注解
			if(method.isAnnotationPresent(SelfDefinedTest.class)) {
				System.out.println(method.getName() + "方法上含有@SelfDefinedTest注解，");
				//第三步，这个注解其实是一个对象，注解中的方法的返回值可以自定义，这个返回值通常用于被注解的类的对象的方法上，实现动态增强；获取这个注解对象
				SelfDefinedTest  selfDefinedTest  = method.getDeclaredAnnotation(SelfDefinedTest.class);
				if (selfDefinedTest.ignore()) {
					System.out.println("该注解属性为ignore=true,故当前方法" + method.getName() + "不执行测试");
				} else {
					System.out.println("该注解属性 ignore = false,故当前方法" + method.getName() + "执行测试:");
					method.invoke(obj,null);
				}
				
				
			}
			if(method.isAnnotationPresent(Test.class)) {
				System.out.println(method.getName() + "方法上含有@Test注解，故暂时不执行测试");
				
			}

			
					if (method.isAnnotationPresent(Test.class)) {
						
						System.out.println(method.getName() + "方法上含有@Test注解，故暂时不执行测试");

					}
				

			}

		}

	}


