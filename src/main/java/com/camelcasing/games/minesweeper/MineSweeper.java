package com.camelcasing.games.minesweeper;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MineSweeper extends Application{

		private final static Logger logger = Logger.getLogger(MineSweeper.class.getName());
	
		private int boardWidth = 16;
		private int boardHeight = 16;
		private int totalSquares;
		private int numberOfMines;
		private GridSquare[][] buttons;
		private GridPane backPanel;
		
		
	@Override
	public void start(Stage stage) throws Exception{
		
		createGUI();
		createGrid();
		assignBombs();
		assignNumbers();
		
		Scene root = new Scene(backPanel);
		root.getStylesheets().add("stylesheet.css");
		stage.setScene(root);
		stage.setTitle("Mine Sweeper");
		stage.show();
	}
	
	private void createGUI(){
		backPanel = new GridPane();
		backPanel.setAlignment(Pos.CENTER);
		backPanel.setPadding(new Insets(30));
	}
	
	private void createGrid(){
		buttons = new GridSquare[boardWidth][boardHeight];
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[i].length; j++){
				GridSquare gs = createGridSquare(i, j);
				buttons[i][j] = gs;
				gs.getGridSquare().setOnAction(ae -> {
					Button b = (Button) ae.getSource();
					b.setOnMouseEntered(me -> {
						b.setBorder(new Border(new BorderStroke(
								Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
					});
					checkSquare(gs);
				});
				backPanel.add(buttons[i][j].getGridSquare(), i, j);
			}
		}
	}
	
	private GridSquare createGridSquare(int row, int column){
		GridSquare gridSquare = new GridSquare(row, column);

		return gridSquare;
	}
	
	private void checkSquare(GridSquare gridSquare){
		if(gridSquare.isMine()) gameOver();
		
	}
	
	private void gameOver(){
		logger.debug("GameOver");
	}
	
	private void assignBombs(){
		totalSquares = boardWidth * boardHeight;
		numberOfMines = totalSquares / 5;
			if(logger.isDebugEnabled()){
				logger.debug("created " + boardWidth + " X " + boardHeight + " board with " + numberOfMines + " mines");
			}
	}
	
	private void assignNumbers(){
		
	}
	
	public static void main(String[] args){
		Application.launch(args);
	}
}
