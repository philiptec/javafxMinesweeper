package com.camelcasing.games.minesweeper;

public class Time {

		private int seconds = 0;
		private int minutes = 0;
		private int hours = 0;
	
	public void incrementSeconds(){
		if(seconds == 59){
			if(minutes == 59){
				hours++;
				minutes = 0;
				seconds = 0;
			}else{
				minutes++;
				seconds = 0;
			}
		}else{
			seconds++;
		}
	}
	
	public void reset(){
		seconds = 0;
		minutes = 0;
		hours = 0;
	}
	
	public int compare(int compareToValue){
		int thisTime = convertTimeToSeconds();
		if(thisTime > compareToValue){
			return 1;
		}else if(thisTime == compareToValue){
			return 0;
		}else{
			return -1;
		}
	}
	
	public int convertTimeToSeconds(){
		return 3600 * hours + 60 * minutes + seconds;
	}
	
	public int getHours(){
		return hours;
	}
	
	public int getMinutes(){
		return minutes;
	}
	
	public int getSeconds(){
		return seconds;
	}
	
	@Override
	public String toString(){
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
