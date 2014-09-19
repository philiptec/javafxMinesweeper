package com.camelcasing.games.minesweeper;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerThread extends Thread {

		private Time time;
		private Label timerLabel = (Label) MineSweeper.beanFactory.getBean("timerLabel");
		private Timeline timer;
		
	@Override
	public void run(){
		time = new Time();
		timer = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			time.incrementSeconds();
			timerLabel.setText("Timer: " + time);	
		}));
		timer.setCycleCount(Animation.INDEFINITE);
	}
	
	public void startTimer(){
		timer.play();
	}
	
	public void pause(){
		timer.pause();
	}
	
	public void reset(){
		time.reset();
	}
}
