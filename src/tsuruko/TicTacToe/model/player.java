package tsuruko.TicTacToe.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class player {
	public static final int XSHAPE = 0;
	public static final int CIRCLE = 1;
	
    private ObservableList<Node> playerMoves = FXCollections.observableArrayList();
	

	private int shapeUsed = 0;
	private Node playerShape;

	public player () {
		playerShape = new xShape(100);
		shapeUsed = XSHAPE;
	}
	
	public player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;

			Circle circle = new Circle();
	        circle.setRadius(50);
	        circle.setFill(Color.WHITESMOKE);
	        circle.setStroke(Color.BLACK);
	        circle.setStrokeWidth(5);
	        
	        playerShape = circle;
		} else {
			shapeUsed = XSHAPE;
			playerShape = new xShape(100);
		}
	}
	
	public String getShapeUsed () {
		if (shapeUsed == XSHAPE ) {
			return "X";
		} else {
			return "O";
		}
	}
	
	public Node makeMove () {
		Node newShape;
		if (shapeUsed == XSHAPE ) {
			newShape = new xShape(100);
		} else {
			Circle circle = new Circle();
	        circle.setRadius(50);
	        circle.setFill(Color.WHITESMOKE);
	        circle.setStroke(Color.BLACK);
	        circle.setStrokeWidth(5);
	        
			newShape = circle;
		}
		playerMoves.add(newShape);
		return newShape;
	}
	
	
}
