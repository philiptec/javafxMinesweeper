package com.camelcasing.games.minesweeper;

import java.util.prefs.Preferences;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

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
		});
		t.start();
	}

	public void displayLeaderboard(){
		MineSweeper.logger.debug("\nLargeBest = " + convertSeconds(largeBest) + "\n" + 
				"MediumBest = " + convertSeconds(mediumBest) + "\n" +
				"SmallBest = " + convertSeconds(smallBest));
		
		Text text = new Text("Large Board \t " + convertSeconds(largeBest) + "\n\n" + 
				"Medium Board \t " + convertSeconds(mediumBest) + "\n\n" +
				"Small Board \t " + convertSeconds(smallBest));
		text.setFont(new Font("Ubuntu", 24));
		
		HBox root = new HBox();
		root.setAlignment(Pos.CENTER);
		HBox.setMargin(text, new Insets(50, 50, 50, 50));
		
		root.getChildren().add(text);
		
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}

	public int getLargeBest() {
		return largeBest;
	}
	
	public String convertSeconds(int seconds){
		if(seconds == 1000000000) return "Not Set";
		int hours = seconds / 3600;
		seconds = seconds - hours * 3600;
		int minutes = seconds / 60;
		seconds = seconds - minutes * 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public boolean setLargeBest(int largeBest){
		if(largeBest < this.largeBest){
			this.largeBest = largeBest;
			prefs.putInt("Large", largeBest);
			MineSweeper.logger.debug("added " + largeBest + " to large");
			return true;
		}
		return false;
	}

	public int getMediumBest() {
		return mediumBest;
	}

	public boolean setMediumBest(int mediumBest) {
		if(mediumBest < this.mediumBest){
			this.mediumBest = mediumBest;
			prefs.putInt("Medium", mediumBest);
			MineSweeper.logger.debug("added "+ mediumBest + " to medium");
			return true;
		}
		return false;
	}

	public int getSmallBest() {
		return smallBest;
	}

	public boolean setSmallBest(int smallBest) {
		if(smallBest < this.smallBest){
			this.smallBest = smallBest;
			prefs.putInt("Small", smallBest);
			MineSweeper.logger.debug("added " + smallBest + " to small");
			return true;
		}
		return false;
	}
}
