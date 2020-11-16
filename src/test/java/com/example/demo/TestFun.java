package com.example.demo;

import static org.mockito.Matchers.startsWith;

public class TestFun {

	public static void main(String[] args) {
		String x=null;
		if(x instanceof  String){
			x=(String)x;
			System.out.println("x的值为"+x);
			return;
		}

		System.out.println("x的类型不是string");

	}
}
