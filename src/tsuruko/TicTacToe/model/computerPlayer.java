package tsuruko.TicTacToe.model;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class computerPlayer extends player {

	public computerPlayer () {
		this.shapeUsed = CIRCLE;
		this.playerName = "Computer";
	}
	
	public computerPlayer (String shapeChosen) {
		super(shapeChosen);
		this.playerName = "Computer";
	}

	public computerPlayer (String shapeChosen, String playerName) {
		super(shapeChosen, playerName);
	}
	
	//helper for determining move
	private boolean isFirstMove (GridPane gameBoard) {
    	for (Node cell : gameBoard.getChildren()) {
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;

    			if (!child.getChildren().isEmpty()) {
    				return false;
    			}
    		}
    	}
    	return true;
	}
	
	//helper for determining move
	//return index of first move played, -1 if this isn't the second move of the game
	private int isSecondMove (GridPane gameBoard) {
		int moveCount = 0;
		int moveIdx = -1;
    	for (int i = 0; i < gameBoard.getChildren().size(); i++) {
    		Node cell = gameBoard.getChildren().get(i);
    		
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;

    			if (!child.getChildren().isEmpty()) {
    				moveCount += 1;
    				moveIdx = i;
    			}
    		}
    	}
    	
    	if (moveCount != 1) {
    		moveIdx = -1;
    	}
    	return moveIdx;
	}
	
	public void makeMove (GridPane gameBoard, player humanPlayer) {
		//if opening choose a random move for more fun
		if (isFirstMove(gameBoard)) {
	    	Random rand = new Random();
	    	int firstMove = rand.nextInt(8);
	    	
	    	StackPane child = (StackPane) gameBoard.getChildren().get(firstMove);
	    	
	    	child.getChildren().add(this.takeTurn());
	    	return;
		}
		
		//if opening move going second
		
		//and to a center opening with a corner mark. 
		// 0 | 1 | 2
		// 3 | 4 | 5
		// 6 | 7 | 8
		int moveIdx = isSecondMove(gameBoard);
		//Player O must always respond to a corner opening with a center mark 
		if (moveIdx == 0 || moveIdx == 2 || moveIdx == 6 || moveIdx == 8) {
			StackPane cell = (StackPane) gameBoard.getChildren().get(4);
			cell.getChildren().add(this.takeTurn());
			return;
		}
		
		//An edge opening must be answered either with a center mark, 
		//  a corner mark next to the X, or an edge mark opposite the X. 
		if (moveIdx == 1 || moveIdx == 3 || moveIdx == 5 || moveIdx == 7) {
			StackPane cell = (StackPane) gameBoard.getChildren().get(4);
			cell.getChildren().add(this.takeTurn());
			return;
		}
		
		//check if I win with next move
    	for (Node cell : gameBoard.getChildren()) {
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;

    			if (child.getChildren().isEmpty()) {
    				child.getChildren().add(this.takeTurn());
    	        	if (this.hasWon(gameBoard)) {
    	        		return;
    	        	} else {
    	        		child.getChildren().clear();
    	        	}
    			}
    		}
    	}
    	//check if we need to block the opponent
    	for (Node cell : gameBoard.getChildren()) {
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;

    			if (child.getChildren().isEmpty()) {
    				child.getChildren().add(humanPlayer.takeTurn());
    	        	if (humanPlayer.hasWon(gameBoard)) {
    	        		child.getChildren().clear();
    	        		child.getChildren().add(this.takeTurn());
    	        		return;
    	        	} else {
    	        		child.getChildren().clear();
    	        	}
    			}
    		}
    	}
    	
		//choose next empty cell
    	for (Node cell : gameBoard.getChildren()) {
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;
    			if (child.getChildren().isEmpty()) {
    				child.getChildren().add(this.takeTurn());
    	        	return;
    			}
    		}
    	}
	}
	
}
