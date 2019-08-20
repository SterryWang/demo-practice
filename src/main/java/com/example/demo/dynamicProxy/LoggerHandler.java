package com.example.demo.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;

/**
 * 基于自定义注解的动态增强,这样，代理方法在invoke时，需要对自己的注解进行解释，选择执行何种逻辑
 * hander 里面实现了被代理类的功能增强，并且hander以功能进行区分，对任何的被代理类，都具有普适性
 * 
 * @author Administrator
 *
 */
public class LoggerHandler implements InvocationHandler {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerHandler.class);

	private Object target;

	public LoggerHandler(Object target) {
		this.target = target;
	}

	/**
	 * 
	 * 代理方法执行时要再检查一次，如果被代理对象target的原方法上有注解就执行，没有注解则不执行动态增强，仅执行被代理对象的原方法
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		

		boolean hasAnnotation = target.getClass().getMethod(method.getName(), method.getParameterTypes())
				.isAnnotationPresent(LogHanderAnnotation.class);

		Object result = null;
		Method  originMethod= target.getClass().getMethod(method.getName(), method.getParameterTypes());
		
		
		if (hasAnnotation) {
			System.out.println(originMethod.getName()+"确实有主注解");

			LogHanderAnnotation logHanderAnnotation = originMethod.getDeclaredAnnotation(LogHanderAnnotation.class);
			if (!logHanderAnnotation.ignore()) {
				log.debug("ignore=false,执行动态增强");
				log.info("开始记录日志，方法即将进入调用！");

				result = method.invoke(target, args);

				log.info("日志打印结束结束，方法调用结束！");

			} else {
				log.debug("ignore=true,不执行动态增强仅执行原方法");
				result = method.invoke(target, args);

			}

		} else {
			log.debug(originMethod.getName()+"该方法上没有@LogHanderAnnotation注解，故不执行动态增强，仅执行原方法");
			result = method.invoke(target, args);

		}
		return result;

	}

}
