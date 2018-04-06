package tsuruko.TicTacToe.model;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class playerShape extends StackPane {
	private Node myShape;
	private player myPlayer;
	private int colIdx;
	private int rowIdx;
	
	playerShape() {
		myShape = new xShape(100);
		this.getChildren().add(myShape);
	}
	
	playerShape(String shapeChosen) {
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
	}
	
	playerShape(String shapeChosen, int colIdx, int rowIdx) {
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
		this.colIdx = colIdx;
		this.rowIdx = rowIdx;
		this.getChildren().add(myShape);
	}
	
	public void setPlayer (player p) {
		myPlayer = p;
	}
	
	public player getPlayer () {
		return myPlayer;
	}
}
