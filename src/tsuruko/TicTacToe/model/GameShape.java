package tsuruko.TicTacToe.model;

import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

abstract public class GameShape extends StackPane {
	protected final static int PADDING = 20;
	
	protected Shape myShape = null;
	protected final Timeline timeline = new Timeline();

	public void setStrokeColor (Color color) {
		if (myShape != null) {
			myShape.setStroke(color);
		}
	}
	
	abstract public void setSize (double width, double height);
}
