package tsuruko.TicTacToe.model;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/*********************************************
 * 
 * Draw an "O"
 * 
 *********************************************/
public class Oshape extends GameShape {

    private final Circle circle = new Circle();
    private double radius = 0;
    
	public Oshape (double width, double height) {
		super();

		radius = (Math.min(width, height)/2) - PADDING;
        circle.setRadius(0);
        circle.setFill(Color.color(0,0,0,0));  //transparent
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);

        this.getChildren().add(circle);
        this.myShape = circle;
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setSize (double width, double height) {
		radius = (Math.min(width, height)/2) - PADDING;
		circle.setRadius(radius);
	}

    /*********************************************
     * 
     * Animation
     * 
     *********************************************/
	public Timeline startAnimation() {
		playingAnimation = true;
		
		circle.setRadius(0);
		
		final KeyValue val = new KeyValue(circle.radiusProperty(), radius);
		final KeyFrame keyFrame = new KeyFrame(Duration.millis(500), val);

		timeline.getKeyFrames().add(keyFrame);
		
		return timeline;
	}
}
