package com.example.demo.springaop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 使用切面来统计每个磁道播放的历史次数, 同样的，我们也可以使用@Around来实现相同的功能
 * 同样的，使用@Aroud 注解，如果被通知对象的方法有返回值，那么Around方法一定要返回，
 * 否则调用方将无法获取返回参数，请特别留意。
 * 
 * @author wangxg3
 * {@link com.example.demo.springaop.TrackCounter}
 *
 */


  @Aspect
  
  @Component
 
public class TrackCounter1 {

	public Map<Integer, Integer> successCount = new HashMap<Integer, Integer>();

	public Map<Integer, Integer> failedCount = new HashMap<Integer, Integer>();

	/**
	 * 这个例子将演示目标类方法参数入参为多个，且又返回的时候，如何获取入参和返回值，完成数据共享
	 * 
	 * @param trackNum
	 * @param speedUp
	 * @param result
	 */
	@Around("execution(* com.example.demo.springaop.CompactDisc.playTrack(int,int))")
	public Object countTrack(ProceedingJoinPoint jp) {
		Object finalresult =null;

		try {

			Object resultObject = null;
			// 获取入参
			int trackNum = (int) jp.getArgs()[0];
			int speedUp = (int) jp.getArgs()[1];
			System.out.println("TrackCounter1:播放前校验入参：trackNum=" + jp.getArgs()[0] + ";speedUp=" + jp.getArgs()[1]);
			// 获取返回
			resultObject = jp.proceed();

			boolean result = false;
			System.out.println("TrackCounter1:刚使用" + speedUp + "倍速播放完成NO." + trackNum + "磁道");
			if (resultObject instanceof Boolean)
				result = (boolean) resultObject;
			    finalresult=result;
			if (result) {
				System.out.println("TrackCounter1:播放结果为成功");
				successCount.put(trackNum, getCurrentSuccCount(trackNum)+1);
			} else {
				System.out.println("TrackCounter1:播放结果为失败");
				failedCount.put(trackNum, getCurrentFailedCount(trackNum)+1);
			}
			
		
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return  finalresult;
	

	}

	public int getCurrentSuccCount(int trackNum) {
		return successCount.containsKey(trackNum) ? successCount.get(trackNum) : 0;

	}

	public int getCurrentFailedCount(int trackNum) {

		return failedCount.containsKey(trackNum) ? failedCount.get(trackNum) : 0;

	}

}
