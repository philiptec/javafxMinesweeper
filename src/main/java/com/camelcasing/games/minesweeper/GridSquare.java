package com.camelcasing.games.minesweeper;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class GridSquare{

		private Button gridSquare;
		private int row = -1;
		private int column = -1;
		private boolean isMine = false;
		private int nextToCount = 0;
		private int size = 30;
		private boolean revealed = false;
		private boolean isFlagged = false;
		
	public GridSquare(int row, int column){
		this.row = row;
		this.column = column;
		gridSquare = new Button();
		gridSquare.setBorder(new Border(new BorderStroke(
				Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(5), BorderStroke.THIN)));
		gridSquare.setId("click");
		gridSquare.setOnMouseExited(me -> gridSquare.setBorder(new Border(new BorderStroke(
				Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(5), BorderStroke.THIN))));
		gridSquare.setOnMouseEntered(me -> gridSquare.setBorder(new Border(new BorderStroke(
				Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.MEDIUM))));
		gridSquare.setMinSize(size, size);
		gridSquare.setMaxSize(size, size);
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
		revealed = true;
	}
	
	public boolean isRevealed(){
		return revealed;
	}
	
	public boolean isFlagged(){
		return isFlagged;
	}
	
	public void setFlagged(boolean b){
		isFlagged = b;
	}
	
	public void drawFlag(){
		Group g = new Group();
		Line pole = new Line();
		int q = size / 5;
		pole.setStartX(q);
		pole.setStartY(q);
		pole.setEndX(q);
		pole.setEndY(q * 3);
		
		Rectangle flag = new Rectangle();
		flag.setFill(Color.RED);
		flag.setX(q);
		flag.setY(q);
		flag.setWidth(10);
		flag.setHeight(5);
		
		g.getChildren().addAll(pole, flag);
		gridSquare.setGraphic(g);
	}
	
	public void removeFlag(){
		gridSquare.setGraphic(null);
	}
	
	public int getColumn(){
		return column;
	}
	
	@Override
	public String toString(){
		return "index " + row + " X " + column + (isMine ? " is a mine" : " not a mine and next to " + nextToCount + " mines");
	}
}
