package com.camelcasing.games.minesweeper;

import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MineSweeper extends Application{

		public final static Logger logger = Logger.getLogger(MineSweeper.class.getName());
	
		private int boardWidth = 16;
		private int boardHeight = 16;
		private int totalSquares;
		private int numberOfMines;
		private GridSquare[][] buttons;
		private GridPane backPanel;
		private BorderPane back;
		private Random rnd = new Random();
		private Stage stage;
		private int remainingBombs; 
		private Label label;
		
	@Override
	public void start(Stage stage) throws Exception{
		
		this.stage = stage;
		
		createGUI();
		createGrid();
		assignBombs();
		assignNumbers();
		
		Scene root = new Scene(back);
		root.getStylesheets().add("stylesheet.css");
		stage.setScene(root);
		stage.setTitle("Mine Sweeper");
		stage.show();
	}
	
	private void createGUI(){
		back = new BorderPane();
		backPanel = new GridPane();
		backPanel.setAlignment(Pos.CENTER);
		backPanel.setPadding(new Insets(30));
		label = new Label();
		label.setPadding(new Insets(10, 0, 0, 0));
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(label);
		back.setCenter(backPanel);
		back.setTop(hbox);
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
				gs.getGridSquare().setOnMouseClicked(me -> {
					if(me.getButton() == javafx.scene.input.MouseButton.SECONDARY){
						Button b = (Button) me.getSource();
						if(b.getText().equals("")){
							b.setStyle("-fx-text-fill: #ff0000");
							b.setText("F");
							remainingBombs--;
							label.setText("Bombs remaining = " + remainingBombs);
						}else if(b.getText().equals("F")){
							b.setText("");
							remainingBombs++;
							label.setText("Bombs remaining = " + remainingBombs);
						}
					}
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
		if(gridSquare.getGridSquare().getText().equals("F")) return;
		if(gridSquare.isMine()) gameOver();
		String s = gridSquare.getNextToCount();
			if(s.equals("0")){
				checkSurrounding(gridSquare);
			}
		gridSquare.getGridSquare().setStyle("-fx-background-color: #ffffff");
		gridSquare.getGridSquare().setText(s);
		gridSquare.reveal();
	}
	
	private void checkSurrounding(GridSquare gridS){
		int r = gridS.getRow();
		int c = gridS.getColumn();
		GridSquare gs;
		
		if((gs = getFromIndex(r-1, c)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r-1, c+1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r, c+1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r+1, c+1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r+1, c)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r+1, c-1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r, c-1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
		if((gs = getFromIndex(r-1, c-1)) != null && !gs.isRevealed()) revealAndCheck(gs, gs.getNextToCount());
	}
	
	private void revealAndCheck(GridSquare gs, String s){
		if(s.equals("F")) return; 
		gs.getGridSquare().setStyle("-fx-background-color: #ffffff");
		gs.getGridSquare().setText(gs.getNextToCount());
		gs.reveal();
		if(s.equals("0")){
			checkSurrounding(gs);
		}
	}
	
	private void gameOver(){
		logger.debug("GameOver");
		JOptionPane.showMessageDialog(null, "GAME OVER");
		reset();
	}
	
	private void assignBombs(){
		logger.debug("bombAssignment started");
		totalSquares = boardWidth * boardHeight;
		numberOfMines = totalSquares / 5;
		remainingBombs = numberOfMines;
		label.setText("Bombs remaining = " + remainingBombs);
		int w = -1;
		int h = -1;
			if(logger.isDebugEnabled()){
				logger.debug("created " + boardWidth + " X " + boardHeight + " board with " + numberOfMines + " mines");
			}
		for(int i = 0; i < numberOfMines; i++){
				do{
					w = rnd.nextInt(boardWidth);
					h = rnd.nextInt(boardHeight);
				}while(buttons[w][h].isMine());
			buttons[w][h].setMine(true);
		}
		logger.debug("bombAssignment completed");
	}
	
	private void assignNumbers(){
		logger.debug("numberAssignment started");
		for(int i = 0; i < buttons.length; i++){
			assign:
			for(int j = 0; j < buttons[i].length; j++){
					if(buttons[i][j].isMine()){
						buttons[i][j].setNextToCount(-1);
						continue assign;
					}
					int count = 0;
						GridSquare gs;
						if((gs = getFromIndex(i-1, j)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i-1, j+1)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i, j+1)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i+1, j+1)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i+1, j)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i+1, j-1)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i, j-1)) != null && gs.isMine()) count++;
						if((gs = getFromIndex(i-1, j-1)) != null && gs.isMine()) count++;
					buttons[i][j].setNextToCount(count);
			}
		}
		logger.debug("numberAssignment completed");
	}
	
	public GridSquare getFromIndex(int r, int c){
		try{
			return buttons[r][c];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public void reset(){
		createGUI();
		createGrid();
		assignBombs();
		assignNumbers();
		
		Scene root = new Scene(back);
		root.getStylesheets().add("stylesheet.css");
		stage.setScene(root);
	}
	
	public static void main(String[] args){
		Application.launch(args);
	}
}
