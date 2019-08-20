package com.example.demo.annotation;

import org.junit.Test;

public class CalculateTest {

	  @Test
	  public void testAdd() {
		  System.out.println("add 1");
		  
	  }
	  
	  @SelfDefinedTest(ignore=true)
	  public void testSubtractIgnore() {
		  System.out.println("ignore");
	  }
	  
	  @SelfDefinedTest()
	  public void testSubtract() {
		  System.out.println("sub 1");
	  }
	  
}
