package com.example.demo.springaop.tests;



import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.springaop.CompactDisc;
import com.example.demo.springaop.TrackCounter1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class })
public class TestTrackCounter {
	@Resource
	CompactDisc compactDisc;
	
	@Resource
	TrackCounter1 trackCounter1;
	@Test
	public void testTrackCounter() {
		
		System.out.println("播放的最终结果为："+compactDisc.playTrack(1,2));
		System.out.println("播放的最终结果为："+compactDisc.playTrack(1,2));
		System.out.println("播放的最终结果为："+compactDisc.playTrack(1,2));
		System.out.println("播放的最终结果为："+compactDisc.playTrack(2,1));	
		System.out.println("播放的最终结果为："+compactDisc.playTrack(2,1));
		
		assertEquals(3, trackCounter1.getCurrentSuccCount(1));
		
		
		

	}
}
