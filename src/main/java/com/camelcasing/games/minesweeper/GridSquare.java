package com.camelcasing.games.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GridSquare{

		private Button gridSquare;
		private int row;
		private int column;
		private boolean isMine = false;
		private int nextToCount = -1;
		
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
		gridSquare.setMinSize(30, 30);
		gridSquare.setMaxSize(30, 30);
	}
	
	public boolean isMine(){
		return isMine;
	}

	public void setMine(boolean isMine){
		this.isMine = isMine;
	}

	public int getNextToCount(){
		return nextToCount;
	}

	public void setNextToCount(int nextToCount){
		this.nextToCount = nextToCount;
	}

	public Button getGridSquare(){
		return gridSquare;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
}
