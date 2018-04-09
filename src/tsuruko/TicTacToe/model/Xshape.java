package tsuruko.TicTacToe.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Xshape extends GameShape {

    private final Line line = new Line();
    private final Line line2 = new Line();
    private int lengthPos = 0;

	public Xshape () {
		lengthPos = 50;
	    line.setStartX(0);
	    line.setStartY(0);
	    line.setEndX(lengthPos);
	    line.setEndY(lengthPos);
	    line.setStrokeWidth(5);
	    
	    line2.setStartX(lengthPos);
	    line2.setStartY(0);
	    line2.setEndX(0);
	    line2.setEndY(lengthPos);
	    line2.setStrokeWidth(5);
	    
	    this.getChildren().add(line);
	    this.getChildren().add(line2);
	    this.myShape = line;
	}
	
	public Xshape (int lengthPos) {
		this.lengthPos = lengthPos;
	    line.setStartX(0.0f);
	    line.setStartY(0.0f);
	    line.setEndX(lengthPos);
	    line.setEndY(lengthPos);
	    line.setStrokeWidth(5);
	    
	    line2.setStartX(lengthPos);
	    line2.setStartY(0.0f);
	    line2.setEndX(0.0f);
	    line2.setEndY(lengthPos);
	    line2.setStrokeWidth(5);

	    this.getChildren().add(line);
	    this.getChildren().add(line2);
	    this.myShape = line;
	}
	
	public void setLengthPos (int lengthPos) {
		this.lengthPos = lengthPos;
	    line.setEndX(lengthPos);
	    line.setEndY(lengthPos);

	    line2.setStartX(lengthPos);
	    line2.setEndY(lengthPos);
	}
	
	public int getLengthPos () {
		return lengthPos;
	}
	
	public void setStrokeColor (Color color) {
		super.setStrokeColor(color);
		line2.setStroke(color);
	}
}
