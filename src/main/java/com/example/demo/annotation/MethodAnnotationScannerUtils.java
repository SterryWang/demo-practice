package com.example.demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodAnnotationScannerUtils {

	public  static boolean singleMethodScan(Method method, Class<? extends Annotation> annotationClass) {
		return method.isAnnotationPresent(annotationClass);
	}

	public static  boolean allMethodsScan(Object obj, Class<? extends Annotation> annotationClass) {

		for(Method method :obj.getClass().getMethods()) {
			if(method.isAnnotationPresent(annotationClass)) {
				return true;
			}
		}
      return false;
	}

}
