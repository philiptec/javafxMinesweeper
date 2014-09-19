package com.camelcasing.games.minesweeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeTest {
	
	@Test
	public void oneMinuteTest(){
		Time time = new Time();
		for(int i = 0; i < 60; i++){
			time.incrementSeconds();
		}
		assertEquals(1, time.getMinutes());
		assertEquals(0, time.getHours());
		assertEquals(0, time.getSeconds());
	}
	
	@Test
	public void convertToSecondsTest(){
		Time time = new Time();
		for(int i = 0; i < 7200; i++){
			time.incrementSeconds();
		}
		assertEquals(2, time.getHours());
		assertEquals(0, time.getMinutes());
		assertEquals(0, time.getSeconds());
		assertEquals(7200, time.convertTimeToSeconds());
	}
}
