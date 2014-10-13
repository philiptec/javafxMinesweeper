package com.camelcasing.games.minesweeper;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class GridSquare{

		private Button gridSquare;
		private int row = -1;
		private int column = -1;
		private boolean isMine = false;
		private int nextToCount = 0;
		private boolean isRevealed = false;
		private boolean isFlagged = false;
		
		private final int SIZE = 30;
		
	public GridSquare(int row, int column){
		this.row = row;
		this.column = column;
		gridSquare = new Button();
		gridSquare.setMinSize(SIZE, SIZE);
		gridSquare.setMaxSize(SIZE, SIZE);
	}
	
	public boolean isMine(){
		return isMine;
	}

	public void setMine(boolean isMine){
		this.isMine = isMine;
	}

	public String getNextToCount(){
		return Integer.toString(nextToCount);
	}

	public void setNextToCount(int nextToCount){
		this.nextToCount = nextToCount;
		gridSquare.setId("button" + nextToCount);
	}

	public Button getGridSquare(){
		return gridSquare;
	}
	
	public void setGridSquare(Button b){
		this.gridSquare = b;
	}
	
	public int getRow(){
		return row;
	}
	
	public void reveal(){
		gridSquare.setGraphic(null);
		isRevealed = true;
		setFlagged(false);
		gridSquare.setStyle("-fx-background-color: #f1f1f1");
		gridSquare.setText(getNextToCount());
	}
	
	public boolean isRevealed(){
		return isRevealed;
	}
	
	public boolean isFlagged(){
		return isFlagged;
	}
	
	public void setFlagged(boolean b){
		isFlagged = b;
	}
	
	public void drawFlag(){
		Group g = new Group();
		Line pole = (Line)MineSweeper.beanFactory.getBean("pole");
		Rectangle redFlag = (Rectangle)MineSweeper.beanFactory.getBean("redFlag");
		g.getChildren().addAll(pole, redFlag);
		gridSquare.setGraphic(g);
	}
	
	public void drawExplosionGraphic(){
		Group g = new Group();
		Circle bc = new Circle(15.0, 15.0, 10);
		Circle sc = new Circle(15.0, 15.0, 6);
		Circle wc = new Circle(15.0, 15.0, 2.5);
		bc.setFill(Color.RED);
		sc.setFill(Color.YELLOW);
		wc.setFill(Color.RED);
		g.getChildren().addAll(bc, sc, wc);
		gridSquare.setGraphic(g);
	}
	
	public void showBomb(){
		if(isMine && !isFlagged){
			Group g = new Group();
			Circle bombCircle = (Circle)MineSweeper.beanFactory.getBean("bombCircle");
			Circle fuseCircle = (Circle)MineSweeper.beanFactory.getBean("fuseCircle");
			Arc fuse = (Arc)MineSweeper.beanFactory.getBean("fuse");
			g.getChildren().addAll(bombCircle, fuseCircle, fuse);
			gridSquare.setGraphic(g);
		}else if(isFlagged && !isMine){
			Group g = new Group();
			Line l1 = (Line)MineSweeper.beanFactory.getBean("forwardLine");
			Line l2 = (Line)MineSweeper.beanFactory.getBean("backwardLine");
			g.getChildren().addAll(l1, l2);
			gridSquare.setGraphic(g);
		}
	}
	
	public void removeFlag(){
		gridSquare.setGraphic(null);
		isFlagged = false;
	}
	
	public int getColumn(){
		return column;
	}
	
	@Override
	public String toString(){
		return "index " + row + " X " + column + (isMine ? " is a mine," : " not a mine and next to " + nextToCount + " mines,")
				+ (isRevealed ? " is revealed," : " is not revealed,") + " and" + (isFlagged ? " is flagged" : " is not flagged");
	} 
}
