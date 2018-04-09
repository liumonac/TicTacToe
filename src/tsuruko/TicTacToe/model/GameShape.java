package tsuruko.TicTacToe.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

abstract public class GameShape extends StackPane {
	
	protected Shape myShape = null;

	public void setStrokeColor (Color color) {
		if (myShape != null) {
			myShape.setStroke(color);
		}
	}
}
