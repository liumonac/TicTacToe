package tsuruko.TicTacToe.model;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class computerPlayer extends player {

	private final Random rand = new Random();
	
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
	
	//Grid Indexes
	// 0 | 1 | 2
	// 3 | 4 | 5
	// 6 | 7 | 8
	public void makeMove (GridPane gameBoard, player humanPlayer) {
		StackPane movePicked;
		
		//if opening choose a random move for more fun
		if (isFirstMove(gameBoard)) {
	    	int firstMove = rand.nextInt(9);
	    	
	    	movePicked = (StackPane) gameBoard.getChildren().get(firstMove);
	    	
	    	movePicked.getChildren().add(this.takeTurn());
	    	return;
		}
		
		//if opening move going second
		int moveIdx = isSecondMove(gameBoard);
		//Player O must always respond to a corner opening with a center mark 
		if (moveIdx == 0 || moveIdx == 2 || moveIdx == 6 || moveIdx == 8) {
			movePicked = (StackPane) gameBoard.getChildren().get(4);
			movePicked.getChildren().add(this.takeTurn());
			return;
		}
		
		//An edge opening must be answered either with a center mark, 
		//  a corner mark next to the X, or an edge mark opposite the X. 
		if (moveIdx == 1 || moveIdx == 3 || moveIdx == 5 || moveIdx == 7) {
	    	//4 options.  Keep games different by randomly picking one of the 4 options
	    	int chooseOption = rand.nextInt(4);
	    	
	    	movePicked = (StackPane) gameBoard.getChildren().get(4);
	    	
	    	 switch (chooseOption) {
	            case 0:  
	            	if (moveIdx == 1 || moveIdx == 3 ) {
	            		movePicked = (StackPane) gameBoard.getChildren().get(0);
	            	} else {
	            		movePicked = (StackPane) gameBoard.getChildren().get(8);
	            	}
	                break;
	            case 1:
	            	if (moveIdx == 3 || moveIdx == 7 ) {
	            		movePicked = (StackPane) gameBoard.getChildren().get(6);
	            	} else {
	            		movePicked = (StackPane) gameBoard.getChildren().get(2);
	            	}
	            	break;
	            case 2:
	            	if (moveIdx == 3) {
	            		movePicked = (StackPane) gameBoard.getChildren().get(5);
	            	} else if (moveIdx == 5) {
	            		movePicked = (StackPane) gameBoard.getChildren().get(3);
		         	} else if (moveIdx == 1) {
		         		movePicked = (StackPane) gameBoard.getChildren().get(7);
			    	} else if (moveIdx == 7) {
			    		movePicked = (StackPane) gameBoard.getChildren().get(1);
			    	}
	            	break;
	        }
	    	 movePicked.getChildren().add(this.takeTurn());
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
    	
    	//Fork: Create an opportunity where the player has two threats to win (two non-blocked lines of 2).
    	//Blocking an opponent's fork: If there is only one possible fork for the opponent, the player should block it. Otherwise, the player should block any forks in any way that simultaneously allows them to create two in a row. Otherwise, the player should create a two in a row to force the opponent into defending, as long as it doesn't result in them creating a fork. For example, if "X" has two opposite corners and "O" has the center, "O" must not play a corner in order to win. (Playing a corner in this scenario creates a fork for "X" to win.)
    	
    	//Center: A player marks the center. (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)
    	movePicked = (StackPane) gameBoard.getChildren().get(4);
    	if (movePicked.getChildren().isEmpty()) {
    		movePicked.getChildren().add(this.takeTurn());
    		return;
    	}
    	
    	//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
    	//Empty corner: The player plays in a corner square.
    	//Empty side: The player plays in a middle square on any of the 4 sides.
    	
		//Placeholder: choose next empty cell in Grid Index order
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
