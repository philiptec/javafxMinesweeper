package com.camelcasing.games.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GridSquare{

		private Button gridSquare;
		private int row = -1;
		private int column = -1;
		private boolean isMine = false;
		private boolean markedAsBomb = false;
		private int nextToCount = 0;
		private int size = 30;
		private ImageView redFlag;
		private boolean revealed = false;
		
	public GridSquare(int row, int column, ImageView redFlag){
		this.row = row;
		this.column = column;
		this.redFlag = redFlag;
		//gridSquare.setGraphic(redFlag);
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
	}

	public Button getGridSquare(){
		return gridSquare;
	}
	
	public void setGridSquare(Button b){
		this.gridSquare = b;
	}
	
	public void markAsBomb(){
		markedAsBomb = true;
	}
	
	public void unmarkAsBomb(){
		markedAsBomb = false;
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
	
	public int getColumn(){
		return column;
	}
	
	@Override
	public String toString(){
		return "index " + row + " X " + column + (isMine ? " is a mine" : " not a mine and next to " + nextToCount + " mines");
	}
}
