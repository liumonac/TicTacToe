package tsuruko.TicTacToe.model;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/*********************************************
 * 
 * Define shapes used to mark the board
 * 
 *********************************************/
abstract public class GameShape extends StackPane {
	protected final static int PADDING = 20;
	
	protected Shape myShape = null;
	protected final Timeline timeline = new Timeline();
	protected boolean playingAnimation = false;

	public GameShape() {
		this.setMinSize(0, 0);
		this.setAlignment(Pos.CENTER);
		playingAnimation = false;
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public boolean isAnimation () {
		return playingAnimation;
	}
	

    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setStrokeColor (Color color) {
		if (myShape != null) {
			myShape.setStroke(color);
		}
	}
	
	public void stopAnimation() {
		playingAnimation = false;
	}
	
    /*********************************************
     * 
     * Abstract required functions
     * 
     *********************************************/
	abstract public void setSize (double width, double height);
	abstract public Timeline startAnimation ();
}
