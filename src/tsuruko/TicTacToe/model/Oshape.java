package tsuruko.TicTacToe.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Oshape extends GameShape {

    private final Circle circle = new Circle();
    private int radius = 0;
    
	public Oshape () {
		this.radius = 50;
        circle.setRadius(radius);
        circle.setFill(Color.web("#add8e6"));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);
        
        this.getChildren().add(circle);
        this.myShape = circle;
	}
	
	public Oshape (int radius) {
		this.radius = radius;
        circle.setRadius(radius);
        circle.setFill(Color.web("#add8e6"));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(5);
        
        this.getChildren().add(circle);
        this.myShape = circle;
	}
	
	public void setLengthPos (int r) {
		this.radius = r;
		
        circle.setRadius(radius);
	}
	
	public int getRadius () {
		return radius;
	}
}
