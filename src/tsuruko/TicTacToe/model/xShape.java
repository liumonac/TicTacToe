package tsuruko.TicTacToe.model;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class xShape extends StackPane {

    private final Line line = new Line();
    private final Line line2 = new Line();

	public xShape () {
	    line.setStartX(0);
	    line.setStartY(0);
	    line.setEndX(50);
	    line.setEndY(50);
	    line.setStrokeWidth(5);
	    
	    line2.setStartX(50);
	    line2.setStartY(0);
	    line2.setEndX(0);
	    line2.setEndY(50);
	    line2.setStrokeWidth(5);
	    
	    this.getChildren().add(line);
	    this.getChildren().add(line2);
	}
	
	public xShape (int lengthPos) {
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
	}
}
