package com.camelcasing.games.minesweeper;

import java.util.prefs.Preferences;

public class Leaderboard{

		public final Preferences prefs = Preferences.userNodeForPackage(getClass());
	
		private int largeBest;
		private int mediumBest;
		private int smallBest;
		
	public Leaderboard(){
		Thread t = new Thread(() -> {
			largeBest = prefs.getInt("Large", 1000000000);
			mediumBest = prefs.getInt("Medium", 1000000000);
			smallBest = prefs.getInt("Small", 1000000000);
			displayLeaderboard();
		});
		t.start();
	}

	public void displayLeaderboard(){
		MineSweeper.logger.debug("\nLargeBest = " + largeBest + "\n" + 
				"MediumBest = " + mediumBest + "\n" +
				"SmallBest = " + smallBest);
	}

	public int getLargeBest() {
		return largeBest;
	}

	public void setLargeBest(int largeBest){
		if(largeBest < this.largeBest){
			this.largeBest = largeBest;
			prefs.putInt("Large", largeBest);
			MineSweeper.logger.debug("added " + largeBest + " to large");
		}
	}

	public int getMediumBest() {
		return mediumBest;
	}

	public void setMediumBest(int mediumBest) {
		if(mediumBest < this.mediumBest){
			this.mediumBest = mediumBest;
			prefs.putInt("Medium", mediumBest);
			MineSweeper.logger.debug("added "+ mediumBest + " to medium");
		}
	}

	public int getSmallBest() {
		return smallBest;
	}

	public void setSmallBest(int smallBest) {
		if(smallBest < this.smallBest){
			this.smallBest = smallBest;
			prefs.putInt("Small", smallBest);
			MineSweeper.logger.debug("added " + smallBest + " to small");
		}
	}
}
