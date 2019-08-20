package com.example.demo.dynamicProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogHanderAnnotation {
	/**
	 * 开关配置，决定此注解是否生效
	 * @return
	 */
	boolean ignore() default  false;

}
