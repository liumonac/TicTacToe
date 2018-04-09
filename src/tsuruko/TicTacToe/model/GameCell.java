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
	
    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
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
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setPlayer (player p) {
		myPlayer = p;
	}
	
	public void setColor (String color) {
		//if myShape.getClass()
		//myShape.setStroke(color);
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public player getPlayer () {
		return myPlayer;
	}
	
	public int getCellNum () {
		return index;
	}
	
    /*********************************************
     * 
     * Main Functions
     * 
     *********************************************/
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
	
	public void clearPiece() {
		this.getChildren().clear();
		this.isEmpty = true;
		this.myPlayer = null;
	}
	
    /*********************************************
     * 
     * Check State
     * 
     *********************************************/
	public boolean isPlayedBy (player p) {
		if (myPlayer == null) {
			return false;
		}
		return myPlayer.equals(p);
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	
    /*********************************************
     * 
     * Private Helper Functions
     * 
     *********************************************/
	private Circle createCircle() {
		Circle circle = new Circle();
        circle.setRadius(50);
        circle.setFill(Color.WHITESMOKE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);
        
        return circle;
	}
}
