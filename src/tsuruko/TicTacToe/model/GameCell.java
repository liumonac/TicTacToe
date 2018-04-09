package tsuruko.TicTacToe.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameCell extends StackPane {
	private GameShape myShape;
	private Player myPlayer;
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
	        myShape = new Oshape();
		} else {
			myShape = new Xshape(100);
		}
		this.getChildren().add(myShape);
		this.index = idx;
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setPlayer (Player p) {
		myPlayer = p;
	}
	
	public void setColor (Color color) {
		myShape.setStrokeColor(color);
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public Player getPlayer () {
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
	public void playPiece (Player p) {
		if (isEmpty) {
			myPlayer = p;
			if (myPlayer.getShapeUsed() == Player.CIRCLE) {
		        myShape = new Oshape();
			} else {
				myShape = new Xshape(100);
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
	public boolean isPlayedBy (Player p) {
		if (myPlayer == null) {
			return false;
		}
		return myPlayer.equals(p);
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
}
