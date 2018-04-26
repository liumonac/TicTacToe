package tsuruko.TicTacToe.model;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/*********************************************
 * 
 * Draw an "X"
 * 
 *********************************************/
public class Xshape extends GameShape {

    private final Line line = new Line();
    private final Line line2 = new Line();
    private double length = 0;

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public Xshape (double width, double height) {
		super();
		
		length = Math.min(width, height) - PADDING - PADDING;  //pad both sides

		line.setStrokeWidth(5);
	    
		line.setStartX(0);
	    line.setStartY(0);
	    line.setEndX(0);
	    line.setEndY(0);
        
	    line2.setStrokeWidth(5);
	    
		line2.setStartX(length);
	    line2.setStartY(0);
	    line2.setEndX(length);
	    line2.setEndY(0);

	    this.getChildren().add(line);
	    this.getChildren().add(line2);
	    this.myShape = line;
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setStrokeColor (Color color) {
		super.setStrokeColor(color);
		line2.setStroke(color);
	}
	
	public void setSize (double width, double height) {
		length = Math.min(width, height) - PADDING - PADDING;  //pad both sides

	    line.setEndX(length);
	    line.setEndY(length);
	    
		line2.setStartX(length);
	    line2.setEndY(length);
	}

    /*********************************************
     * 
     * Animation
     * 
     *********************************************/
	public Timeline startAnimation() {
		playingAnimation = true;
		
	    line.setEndX(0);
	    line.setEndY(0);
	    
	    line2.setEndX(length);
	    line2.setEndY(0);
	    
		final KeyValue xValue1 = new KeyValue(line.endXProperty(), length);
		final KeyValue yValue1 = new KeyValue(line.endYProperty(), length);
		final KeyFrame xKeyFrame1 = new KeyFrame(Duration.millis(500), xValue1);
		final KeyFrame yKeyFrame1 = new KeyFrame(Duration.millis(500), yValue1);
		
		final KeyValue xValue2 = new KeyValue(line2.endXProperty(), 0);
		final KeyValue yValue2 = new KeyValue(line2.endYProperty(), length);
		final KeyFrame xKeyFrame2 = new KeyFrame(Duration.millis(500), xValue2);
		final KeyFrame yKeyFrame2 = new KeyFrame(Duration.millis(500), yValue2);
		
		timeline.getKeyFrames().add(xKeyFrame1);
		timeline.getKeyFrames().add(xKeyFrame2);
		timeline.getKeyFrames().add(yKeyFrame1);
		timeline.getKeyFrames().add(yKeyFrame2);
		
		return timeline;
	}
}
