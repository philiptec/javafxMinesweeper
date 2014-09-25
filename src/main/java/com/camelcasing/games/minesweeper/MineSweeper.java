package com.camelcasing.games.minesweeper;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;

public class MineSweeper extends Application{

		public final static Logger logger = Logger.getLogger(MineSweeper.class.getName());
		public final static  ApplicationContext beanFactory = new ClassPathXmlApplicationContext("springbeans.xml");
	
		private TimerThread TIMER = new TimerThread();;
		
		private int boardWidth;
		private int boardHeight;
		private String gameBoardSize = "Large";
		private int totalNumberOfSquares;
		private int totalNumberOfMines;
		private GridSquare[][] squares;
		private GridPane board;
		private BorderPane rootPane;
		private BorderPane boardAndInfo;
		private int remainingBombs; 
		private Label remainingBombsLabel;
		private Label timerLabel;
		private boolean timerRunning = false;
		private HBox timerAndBombCount;
		private Stage stage;
		private Scene root;
		private Leaderboard leaderboard;

	@Override
	public void start(Stage stage) throws Exception{
		this.stage = stage;
		leaderboard = new Leaderboard();
		createGUI();
		createMenuItems();
		createGrid();
		assignBombs();
		assignNumbers();
		createTimer();
		
		root = new Scene(rootPane);
		root.getStylesheets().add("stylesheet.css");
		stage.setScene(root);
		stage.setTitle("Mine Sweeper");
		stage.show();
	}
	
	private void createGUI(){
		setBoardSize();
		rootPane = (BorderPane)beanFactory.getBean("rootPane");
		boardAndInfo = (BorderPane)beanFactory.getBean("boardAndInfo");
		createBoard();
		remainingBombsLabel = (Label)beanFactory.getBean("label");
		timerLabel = (Label)beanFactory.getBean("timerLabel");
		timerAndBombCount = (HBox)beanFactory.getBean("infoPanel");
		timerAndBombCount.getChildren().addAll(remainingBombsLabel, timerLabel);
	}
	
	private void createMenuItems(){
		
		Menu gameMenu = (Menu)beanFactory.getBean("gameMenu");
		MenuItem resetMenuItem = (MenuItem)beanFactory.getBean("resetMenuItem");
		resetMenuItem.setOnAction(e -> reset());
		resetMenuItem.setAccelerator(KeyCombination.valueOf("Enter"));
		
		MenuItem exitMenuItem = (MenuItem)beanFactory.getBean("exitMenuItem");
		exitMenuItem.setOnAction(e -> System.exit(0));
		exitMenuItem.setAccelerator(KeyCombination.valueOf("q"));
		
		MenuItem leaderBoard = new MenuItem("Leaderboard");
		leaderBoard.setOnAction(e -> {
			
		});
		
		
		Menu timerMenu = new Menu("Timer");
		MenuItem pause = new MenuItem("Pause");
		pause.setOnAction(e -> {
			timerRunning = false;
			TIMER.pause();
		});
		pause.setAccelerator(KeyCombination.valueOf("p"));
		timerMenu.getItems().add(pause);
		
		Menu boardSize = new Menu("Board Size");
		MenuItem small = new MenuItem("Small");
		MenuItem medium = new MenuItem("Medium");
		MenuItem large = new MenuItem("Large");
		
		small.setOnAction(e -> {
			gameBoardSize = "Small";
			setBoardSize();
			reset();
		});
		medium.setOnAction(e -> {
			gameBoardSize = "Medium";
			setBoardSize();
			reset();
		});
		large.setOnAction(e -> {
			gameBoardSize = "Large";
			setBoardSize();
			reset();
		});
		
		boardSize.getItems().addAll(small, medium, large);
		
		gameMenu.getItems().addAll(resetMenuItem, leaderBoard, exitMenuItem);
		((MenuBar)beanFactory.getBean("topMenuBar")).getMenus().addAll(gameMenu, boardSize, timerMenu);
	}
	
	private void setBoardSize(){
		switch(gameBoardSize){
		case "Large":
			boardWidth = 30;
			boardHeight = 19;
			break;
		case "Medium":
			boardWidth = 20;
			boardHeight = 11;
			break;
		case "Small":
			boardWidth = 10;
			boardHeight = 10;
			break;
		}
	}
	
	private void createGrid(){
		squares = new GridSquare[boardWidth][boardHeight];
		for(int i = 0; i < squares.length; i++){
			for(int j = 0; j < squares[i].length; j++){
				GridSquare gs = new GridSquare(i, j);
				squares[i][j] = gs;
				
				gs.getGridSquare().setOnAction(ae -> {
					startTimerIfNotRunning();
					if(!gs.isFlagged())checkSquare(gs);
					checkWon();
				});
				
				gs.getGridSquare().setOnMouseClicked(me -> {
					if(me.getButton() == javafx.scene.input.MouseButton.SECONDARY){
						Button b = (Button) me.getSource();
						if(!gs.isFlagged() && !gs.isRevealed()){
							b.setStyle("-fx-text-fill: #ff0000");
							gs.setFlagged(true);
							gs.drawFlag();
							remainingBombs--;
							remainingBombsLabel.setText("Bombs remaining = " + remainingBombs);
						}else if(gs.isFlagged()){
							gs.removeFlag();
							gs.setFlagged(false);
							remainingBombs++;
							remainingBombsLabel.setText("Bombs remaining = " + remainingBombs);
						}
						startTimerIfNotRunning();
						checkWon();
					}
				});
				board.add(squares[i][j].getGridSquare(), i, j);
			}
		}
	}
	
	private void checkWon(){
		if(remainingBombs == 0){
			logger.debug("checked if won");
			for(int i = 0; i < squares.length; i++){
				for(int j = 0; j < squares[i].length; j++){
					GridSquare gs = squares[i][j];
					if((!gs.isRevealed() && !gs.isFlagged())){
						return;
					}
				}
			}
			pauseTimerAndDeactivateSquares();
			remainingBombsLabel.setText("YOU WIN!");
			int time = TIMER.getTime();
			
			switch(gameBoardSize){
			case "Large":
				leaderboard.setLargeBest(time);
				break;
			case "Medium":
				leaderboard.setMediumBest(time);
				break;
			case "Small":
				leaderboard.setSmallBest(time);
				break;
			}
		}
	}
	
	private void startTimerIfNotRunning(){
		if(!timerRunning){
			timerRunning = true;
			TIMER.startTimer();
		}
	}
	
	private void checkSquare(GridSquare gridSquare){
		if(gridSquare.isMine()){
			gameOver(gridSquare);
			return;
		}
		String s = gridSquare.getNextToCount();
			if(s.equals("0")){
				checkSurrounding(gridSquare);
			}
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
		gs.getGridSquare().setStyle("-fx-background-color: #f1f1f1");
		gs.getGridSquare().setText(gs.getNextToCount());
		gs.reveal();
		if(s.equals("0")){
			checkSurrounding(gs);
		}
	}
	
	private void gameOver(GridSquare gs){
		logger.debug("GameOver");
		pauseTimerAndDeactivateSquares();
		gs.drawExplosionGraphic();
		remainingBombsLabel.setText("BOOM!");
	}
	
	private void pauseTimerAndDeactivateSquares(){
		TIMER.pause();
		for(int i = 0; i < squares.length; i++){
			for(int j = 0; j < squares[i].length; j++){
				Button b = squares[i][j].getGridSquare();
				b.setOnAction(e -> {});
				b.setOnMouseClicked(e -> {});
				squares[i][j].showBomb();
			}
		}
	}
	
	private void assignBombs(){
		logger.debug("bombAssignment started");
		totalNumberOfSquares = boardWidth * boardHeight;
		totalNumberOfMines = totalNumberOfSquares / 5;
		remainingBombs = totalNumberOfMines;
		remainingBombsLabel.setText("Bombs remaining = " + remainingBombs);
		int w = -1;
		int h = -1;
			if(logger.isDebugEnabled()){
				logger.debug("created " + boardWidth + " X " + boardHeight + " board with " + totalNumberOfMines + " mines");
			}
		for(int i = 0; i < totalNumberOfMines; i++){
				do{
					w = ThreadLocalRandom.current().nextInt(boardWidth);
					h = ThreadLocalRandom.current().nextInt(boardHeight);
				}while(squares[w][h].isMine());
			squares[w][h].setMine(true);
		}
		logger.debug("bombAssignment completed");
	}
	
	private void assignNumbers(){
		logger.debug("numberAssignment started");
		for(int i = 0; i < squares.length; i++){
			assign:
			for(int j = 0; j < squares[i].length; j++){
					if(squares[i][j].isMine()){
						squares[i][j].setNextToCount(-1);
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
					squares[i][j].setNextToCount(count);
			}
		}
		logger.debug("numberAssignment completed");
	}
	
	public GridSquare getFromIndex(int r, int c){
		try{
			return squares[r][c];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	private void createTimer(){
		TIMER.run();
	}
	
	private void createBoard(){
		board = new GridPane();
		board.setPadding(new Insets(10, 30, 30, 30));
		board.setAlignment(Pos.CENTER);
		boardAndInfo.setCenter(board);
		rootPane.setCenter(boardAndInfo);
	}
	
	public void reset(){
		createBoard();
		createGrid();
		assignBombs();
		assignNumbers();
		timerRunning = false;
		TIMER.pause();
		timerLabel.setText("Timer: 00:00:00");
		TIMER.reset();
		stage.setScene(root);
		logger.debug("game reset");
	}
	
	public static void main(String[] args){
		Application.launch(args);
	}
}
