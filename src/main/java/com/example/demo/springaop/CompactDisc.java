package com.example.demo.springaop;

import org.springframework.stereotype.Component;
/**
 * 
 * @author wangxg3
 * {@link com.example.demo.springaop.TrackCounter}
 *
 */
@Component
public class CompactDisc {
   public boolean playTrack(int trackNum,int speedUp) {
	   //以几倍速播放磁道
	   System.out.println("play NO."+trackNum+" track  at speedUP "+speedUp+"!");
	   return true;
   }
}
