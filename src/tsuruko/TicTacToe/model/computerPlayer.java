package tsuruko.TicTacToe.model;

import java.util.ArrayList;
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
		StackPane cellPicked;
		int cellNum = 0;
		
		//if opening choose a random move for more fun
		if (isFirstMove(gameBoard)) {
			cellNum = rand.nextInt(9);
	    	
	    	cellPicked = (StackPane) gameBoard.getChildren().get(cellNum);
	    	cellPicked.getChildren().add(this.playPiece(cellNum));
	    	return;
		}
		
		//if opening move going second
		int moveIdx = isSecondMove(gameBoard);
		//Player O must always respond to a corner opening with a center mark 
		if (moveIdx == 0 || moveIdx == 2 || moveIdx == 6 || moveIdx == 8) {
			cellNum = 4;
			return;
		}
		
		//An edge opening must be answered either with a center mark, 
		//  a corner mark next to the X, or an edge mark opposite the X. 
		if (moveIdx == 1 || moveIdx == 3 || moveIdx == 5 || moveIdx == 7) {
	    	//4 options.  Keep games different by randomly picking one of the 4 options
	    	int chooseOption = rand.nextInt(4);
	    	
	    	 switch (chooseOption) {
	            case 0:  
	            	if (moveIdx == 1 || moveIdx == 3 ) {
	            		cellNum = 0;
	            	} else {
	            		cellNum = 8;
	            	}
	                break;
	            case 1:
	            	if (moveIdx == 3 || moveIdx == 7 ) {
	            		cellNum = 6;
	            	} else {
	            		cellNum = 2;
	            	}
	            	break;
	            case 2:
	            	if (moveIdx == 3) {
	            		cellNum = 5;
	            	} else if (moveIdx == 5) {
	            		cellNum = 3;
		         	} else if (moveIdx == 1) {
		         		cellNum = 7;
			    	} else if (moveIdx == 7) {
			    		cellNum = 1;
			    	}
	            	break;
	            default:
	            	cellNum = 4;
	        }
	    	 
		    cellPicked = (StackPane) gameBoard.getChildren().get(cellNum);
		    cellPicked.getChildren().add(this.playPiece(cellNum));
			return;
		}
		
		//check if I win with next move
		for (int i = 0; i < gameBoard.getChildren().size(); i++) {
			Node cell = gameBoard.getChildren().get(i);
    		if (cell.getClass() == StackPane.class) {
    			cellPicked = (StackPane) cell;

    			if (cellPicked.getChildren().isEmpty()) {
    				cellPicked.getChildren().add(this.playPiece(i));
    	        	if (this.hasWon(gameBoard)) {
    	        		return;
    	        	} else {
    	        		cellPicked.getChildren().clear();
    	        	}
    			}
    		}
    	}
    	
    	//check if we need to block the opponent
		for (int i = 0; i < gameBoard.getChildren().size(); i++) {
			Node cell = gameBoard.getChildren().get(i);
    		if (cell.getClass() == StackPane.class) {
    			cellPicked = (StackPane) cell;

    			if (cellPicked.getChildren().isEmpty()) {
    				cellPicked.getChildren().add(humanPlayer.playPiece(i));
    	        	if (humanPlayer.hasWon(gameBoard)) {
    	        		cellPicked.getChildren().clear();
    	        		cellPicked.getChildren().add(this.playPiece(i));
    	        		return;
    	        	} else {
    	        		cellPicked.getChildren().clear();
    	        	}
    			}
    		}
    	}
    	
    	//Fork: Create an opportunity where the player has two threats to win (two non-blocked lines of 2).
    	
    	//Blocking an opponent's fork: If there is only one possible fork for the opponent, the player should block it. Otherwise, the player should block any forks in any way that simultaneously allows them to create two in a row. Otherwise, the player should create a two in a row to force the opponent into defending, as long as it doesn't result in them creating a fork. For example, if "X" has two opposite corners and "O" has the center, "O" must not play a corner in order to win. (Playing a corner in this scenario creates a fork for "X" to win.)
    	
    	//Center: A player marks the center. (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)
		cellPicked = (StackPane) gameBoard.getChildren().get(4);
    	if (cellPicked.getChildren().isEmpty()) {
    		cellPicked.getChildren().add(this.playPiece(4));
    		return;
    	}
    	
    	//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
    	//Add functionality to pick a random corner if multiple are already played by opponent
    	ArrayList<Integer> opponentCorners = new ArrayList<>();
    	 
    	cellPicked = (StackPane) gameBoard.getChildren().get(0);
     	if (! cellPicked.getChildren().isEmpty() ) {
     		gamePiece checkPlayer = (gamePiece) (cellPicked.getChildren().get(0));
     		if (checkPlayer.playedBy(humanPlayer)) {
     			opponentCorners.add(0);
     		}
     	}
     	
     	cellPicked = (StackPane) gameBoard.getChildren().get(2);
     	if (! cellPicked.getChildren().isEmpty() ) {
     		gamePiece checkPlayer = (gamePiece) (cellPicked.getChildren().get(0));
     		if (checkPlayer.playedBy(humanPlayer)) {
     			opponentCorners.add(2);
     		}
     	}
     	
     	cellPicked = (StackPane) gameBoard.getChildren().get(6);
     	if (! cellPicked.getChildren().isEmpty() ) {
     		gamePiece checkPlayer = (gamePiece) (cellPicked.getChildren().get(0));
     		if (checkPlayer.playedBy(humanPlayer)) {
     			opponentCorners.add(6);
     		}
     	}
     	
     	cellPicked = (StackPane) gameBoard.getChildren().get(8);
     	if (! cellPicked.getChildren().isEmpty() ) {
     		gamePiece checkPlayer = (gamePiece) (cellPicked.getChildren().get(0));
     		if (checkPlayer.playedBy(humanPlayer)) {
     			opponentCorners.add(8);
     		}
     	}
     	
    	if (opponentCorners.size() > 1) {
    		int pickIdx = rand.nextInt(opponentCorners.size());
    		cellPicked = (StackPane) gameBoard.getChildren().get(pickIdx);
    		cellPicked.getChildren().add(this.playPiece(pickIdx));
    		return;
    	} else if (opponentCorners.size() == 1) {
    		cellPicked = (StackPane) gameBoard.getChildren().get(opponentCorners.get(0));
    		cellPicked.getChildren().add(this.playPiece(opponentCorners.get(0)));
    		return;
    	}
    			
    	//Empty corner: The player plays in a corner square.
    	//Add functionality to pick a random corner if multiple are empty
    	ArrayList<Integer> emptyCorners = new ArrayList<>();
    	
    	cellPicked = (StackPane) gameBoard.getChildren().get(0);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyCorners.add(0);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(2);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyCorners.add(2);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(6);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyCorners.add(6);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(8);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyCorners.add(8);
    	}
    	
    	if (emptyCorners.size() > 1) {
    		int pickIdx = rand.nextInt(emptyCorners.size());
    		cellPicked = (StackPane) gameBoard.getChildren().get(pickIdx);
    		cellPicked.getChildren().add(this.playPiece(pickIdx));
    		return;
    	} else if (emptyCorners.size() == 1) {
    		cellPicked = (StackPane) gameBoard.getChildren().get(emptyCorners.get(0));
    		cellPicked.getChildren().add(this.playPiece(emptyCorners.get(0)));
    		return;
    	}
    	
    	//Empty side: The player plays in a middle square on any of the 4 sides.
    	//Add functionality to pick a random corner if multiple are empty
    	ArrayList<Integer> emptyEdges = new ArrayList<>();
    	
    	cellPicked = (StackPane) gameBoard.getChildren().get(1);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyEdges.add(1);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(3);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyEdges.add(3);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(5);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyEdges.add(5);
    	}
    	cellPicked = (StackPane) gameBoard.getChildren().get(7);
    	if (cellPicked.getChildren().isEmpty()) {
    		emptyEdges.add(7);
    	}
    	
    	if (emptyEdges.size() > 1) {
    		int pickIdx = rand.nextInt(emptyEdges.size());
    		cellPicked = (StackPane) gameBoard.getChildren().get(pickIdx);
    		cellPicked.getChildren().add(this.playPiece(pickIdx));
    		return;
    	} else if (emptyEdges.size() == 1) {
    		cellPicked = (StackPane) gameBoard.getChildren().get(emptyEdges.get(0));
    		cellPicked.getChildren().add(this.playPiece(emptyEdges.get(0)));
    		return;
    	}
    	
		//Catch All: choose next empty cell in Grid Index order
		for (int i = 0; i < gameBoard.getChildren().size(); i++) {
			Node cell = gameBoard.getChildren().get(i);
    		if (cell.getClass() == StackPane.class) {
    			cellPicked = (StackPane) cell;
    			if (cellPicked.getChildren().isEmpty()) {
    				cellPicked.getChildren().add(this.playPiece(i));
    	        	return;
    			}
    		}
    	}
	}
	
}
