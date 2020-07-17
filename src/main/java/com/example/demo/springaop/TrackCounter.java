package com.example.demo.springaop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 使用切面来统计每个磁道播放的历史次数
 * 
 * @author wangxg3
 *
 */

  @Aspect
  
  @Component
 

public class TrackCounter {

	public Map<Integer, Integer> successCount = new HashMap<Integer, Integer>();

	public Map<Integer, Integer> failedCount = new HashMap<Integer, Integer>();

	/**
	 * 这个例子将演示目标类方法参数入参为多个，且又返回的时候，如何获取入参和返回值，完成数据共享
	 * 
	 * @param trackNum
	 * @param speedUp
	 * @param result
	 */
	@AfterReturning(value = "execution(* com.example.demo.springaop.CompactDisc.playTrack(int,int)) && args(trackNum,speedUp)", returning = "result")
	public void countTrack(int trackNum, int speedUp, boolean result) {
		System.out.println("刚使用" + speedUp + "倍速播放完成NO." + trackNum + "磁道");
		if (result) {
			System.out.println("播放结果为成功");
			successCount.put(trackNum, getCurrentSuccCount(trackNum)+1);
		} else {
			System.out.println("播放结果为失败");
			failedCount.put(trackNum, getCurrentFailedCount(trackNum)+1);
		}

	}

	public int getCurrentSuccCount(int trackNum) {
		return successCount.containsKey(trackNum) ? successCount.get(trackNum) : 0;

	}

	public int getCurrentFailedCount(int trackNum) {

		return failedCount.getOrDefault(trackNum,0);

	}

}
