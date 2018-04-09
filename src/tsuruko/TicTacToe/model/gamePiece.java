package tsuruko.TicTacToe.model;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class gamePiece extends StackPane {
	private Node myShape;
	private player myPlayer;
	
	private int index;
	
	gamePiece() {
		index = 0;
	}
	
	gamePiece(String shapeChosen, int idx) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			Circle circle = new Circle();
	        circle.setRadius(50);
	        circle.setFill(Color.WHITESMOKE);
	        circle.setStroke(Color.BLACK);
	        circle.setStrokeWidth(5);
	        
	        myShape = circle;
		} else {
			myShape = new xShape(100);
		}
		this.getChildren().add(myShape);
		this.index = idx;
	}
	
	public void setPlayer (player p) {
		myPlayer = p;
	}
	
	public player getPlayer () {
		return myPlayer;
	}
	
	public boolean playedBy (player p) {
		return myPlayer.equals(p);
	}
	
	public int getCellNum () {
		return index;
	}
}
