package tsuruko.TicTacToe.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class player {
	public static final String XSHAPE = "X";
	public static final String CIRCLE = "O";
	
    private ObservableList<playerShape> playerMoves = FXCollections.observableArrayList();
	
    private String shapeUsed = "X";
    private String playerName = "Player 1";

	public player () {
		shapeUsed = XSHAPE;
	}
	
	public player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;
		} else {
			shapeUsed = XSHAPE;
		}
	}

	public player (String shapeChosen, String playerName) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;
		} else {
			shapeUsed = XSHAPE;
		}
		this.playerName = playerName;
	}
	
	public String getPlayerName () {
		return playerName;
	}
	
	public String getShapeUsed () {
		return shapeUsed;
	}

	public playerShape makeMove (int colIndex, int rowIndex) {
		playerShape newShape = new playerShape(shapeUsed);
		
		newShape.setPlayer(this);

		playerMoves.add(newShape);
		return newShape;
	}
	
	
}
