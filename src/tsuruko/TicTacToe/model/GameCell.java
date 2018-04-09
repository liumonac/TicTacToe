package tsuruko.TicTacToe.model;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameCell extends StackPane {
	private Node myShape;
	private player myPlayer;
	private boolean isEmpty = true;
	
	private int index;
	
	public GameCell() {
		index = 0;
		this.getChildren().clear();
		isEmpty = true;
	}
	
	public GameCell(String shapeChosen, int idx) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
	        myShape = createCircle();
		} else {
			myShape = new xShape(100);
		}
		this.getChildren().add(myShape);
		this.index = idx;
	}
	
	public void setPlayer (player p) {
		myPlayer = p;
	}
	
	public void playPiece (player p) {
		if (isEmpty) {
			myPlayer = p;
			if (myPlayer.getShapeUsed() == player.CIRCLE) {
		        myShape = createCircle();
			} else {
				myShape = new xShape(100);
			}
			isEmpty = false;
			this.getChildren().add(myShape);
		}
	}
	
	public player getPlayer () {
		return myPlayer;
	}
	
	public boolean isPlayedBy (player p) {
		if (myPlayer == null) {
			return false;
		}
		return myPlayer.equals(p);
	}
	
	public int getCellNum () {
		return index;
	}
	
	public void clearPiece() {
		this.getChildren().clear();
		this.isEmpty = true;
		this.myPlayer = null;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	private Circle createCircle() {
		Circle circle = new Circle();
        circle.setRadius(50);
        circle.setFill(Color.WHITESMOKE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);
        
        return circle;
	}
}
