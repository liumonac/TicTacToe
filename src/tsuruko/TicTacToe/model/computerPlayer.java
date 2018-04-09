package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

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
    	for (Node n : gameBoard.getChildren()) {
    		if (n.getClass() == GameCell.class) {
    			GameCell cell = (GameCell) n;

    			if (!cell.isEmpty()) {
    				return false;
    			}
    		}
    	}
    	return true;
	}
	
	//helper for determining move
	//return index of first move played, -1 if this isn't the second move of the game
	private IntPair isSecondMove (TicTacToeGame myGame) {
		int moveCount = 0;
		int x = 0;
		int y = 0;
		IntPair result = null;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GameCell cell = myGame.getGameCell(i, j);
    			if (!cell.getChildren().isEmpty()) {
    				moveCount += 1;
    				x = i;
    				y = j;
    			}
			}
		}

    	if (moveCount == 1) {
    		result = new IntPair (x, y);
    	}
    	return result;
	}
	
	//Grid Indexes
	// 0,0 | 0,1 | 0,2        0 | 1 | 2
	// 1,0 | 1,1 | 1,2		  3 | 4 | 5
	// 2,0 | 2,1 | 2,2	      6 | 7 | 8
	public void makeMove (TicTacToeGame myGame, player humanPlayer) {
		GameCell cellPicked = null;
		
		//if opening choose a random move for more fun
		if (isFirstMove(myGame.getGameBoard())) {
			int i = rand.nextInt(2);
			int j = rand.nextInt(2);
	    	
	    	cellPicked = myGame.getGameCell (i, j);
	    	cellPicked.playPiece(this);
	    	return;
		}

		//if opening move going second
		IntPair moveIdx = isSecondMove(myGame);
		if (moveIdx != null) {
			//Player O must always respond to a corner opening with a center mark 
			if (moveIdx.equals(0,0) || moveIdx.equals(0,2) || moveIdx.equals(2,0) || moveIdx.equals(2,2)) {
		    	cellPicked = myGame.getGameCell (1, 1);
		    	cellPicked.playPiece(this);
				return;
			}
			
			//An edge opening must be answered either with a center mark, 
			//  a corner mark next to the X, or an edge mark opposite the X. 
			if (moveIdx.equals(0,1) || moveIdx.equals(1,0)|| moveIdx.equals(2,1) || moveIdx.equals(1,2)) {
		    	//4 options.  Keep games different by randomly picking one of the 4 options
		    	int chooseOption = rand.nextInt(4);
		    	
		    	cellPicked = myGame.getGameCell (1, 1);

		    	switch (chooseOption) {
		        	case 0:  
		        		if (moveIdx.equals(0,1) || moveIdx.equals(1,0) ) {
		            		cellPicked = myGame.getGameCell (0, 0);
		            	} else {
		            		cellPicked = myGame.getGameCell (2, 2);
		            	}
		                break;
		            case 1:
		            	if (moveIdx.equals(1,0) || moveIdx.equals(2,1) ) {
		            		cellPicked = myGame.getGameCell (2, 0);
		            	} else {
		            		cellPicked = myGame.getGameCell (0, 2);
		            	}
		            	break;
		            case 2:
		            	if (moveIdx.equals(1,0)) {
		            		cellPicked = myGame.getGameCell (1, 2);
				    	} else if (moveIdx.equals(1,2)) {
				    		cellPicked = myGame.getGameCell (1, 0);
		            	} else if (moveIdx.equals(2,1)) {
		            		cellPicked = myGame.getGameCell (0, 1);
			         	} else if (moveIdx.equals(0,1)) {
			         		cellPicked = myGame.getGameCell (2, 1);
				    	}
		            	break;
		        }
		    	
			    cellPicked.playPiece(this);
				return;
			//If X plays center opening move, O should take corner
			} else if (moveIdx.equals(1, 1)) {
				//Add randomness to corner picked
				int pickIdx = rand.nextInt(4);
				if (pickIdx == 0) {
					cellPicked = myGame.getGameCell(0, 0);
				} else if (pickIdx == 1) {
					cellPicked = myGame.getGameCell(0, 2);
				} else if (pickIdx == 2) {
					cellPicked = myGame.getGameCell(2, 0);
				} else if (pickIdx == 3) {
					cellPicked = myGame.getGameCell(2, 2);
				}
				cellPicked.playPiece(this);
		    	return;
			}
		}
		
		//check if I win with next move
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cellPicked = myGame.getGameCell(i, j);
    			if (cellPicked.isEmpty()) {
    				cellPicked.playPiece(this);
    	        	if (myGame.playerHasWon(this)) {
    	        		return;
    	        	} else {
    	        		cellPicked.clearPiece();
    	        	}
    			}
			}
		}

    	//check if we need to block the opponent
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cellPicked = myGame.getGameCell(i, j);
    			if (cellPicked.isEmpty()) {
    				cellPicked.playPiece(humanPlayer);
    	        	if (myGame.playerHasWon(humanPlayer)) {
    	        		cellPicked.clearPiece();
    	        		cellPicked.playPiece(this);
    	        		return;
    	        	} else {
    	        		cellPicked.clearPiece();
    	        	}
    			}
			}
		}
    	
    	//Fork: Create an opportunity where the player has two threats to win (two non-blocked lines of 2).
    	
    	//Blocking an opponent's fork: If there is only one possible fork for the opponent, the player should block it. Otherwise, the player should block any forks in any way that simultaneously allows them to create two in a row. Otherwise, the player should create a two in a row to force the opponent into defending, as long as it doesn't result in them creating a fork. For example, if "X" has two opposite corners and "O" has the center, "O" must not play a corner in order to win. (Playing a corner in this scenario creates a fork for "X" to win.)
    	
    	//Center: A player marks the center. (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)

		cellPicked = myGame.getGameCell(1, 1);
    	if (cellPicked.isEmpty()) {
    		cellPicked.playPiece(this);
    		return;
    	}
    	
    	//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
    	//Add functionality to pick a random corner if multiple are already played by opponent
    	ArrayList<IntPair> opponentCorners = new ArrayList<>();

    	GameCell opponentCell = myGame.getGameCell(2, 2);
    	cellPicked = myGame.getGameCell(0, 0);
     	if (! cellPicked.isEmpty() && opponentCell.isPlayedBy(humanPlayer)) {
     			opponentCorners.add(new IntPair(0, 0));
     	}

    	opponentCell = myGame.getGameCell(2, 0);
     	cellPicked = myGame.getGameCell(0, 2);
     	if (! cellPicked.isEmpty() && opponentCell.isPlayedBy(humanPlayer)) {
 			opponentCorners.add(new IntPair(0, 2));
     	}
    	opponentCell = myGame.getGameCell(0, 2);
     	cellPicked = myGame.getGameCell(2, 0);
     	if (! cellPicked.isEmpty() && opponentCell.isPlayedBy(humanPlayer)) {
 			opponentCorners.add(new IntPair(2, 0));
     	}

    	opponentCell = myGame.getGameCell(0, 0);
     	cellPicked = myGame.getGameCell(2, 2);
     	if (! cellPicked.isEmpty() && opponentCell.isPlayedBy(humanPlayer)) {
 			opponentCorners.add(new IntPair(2, 2));
     	}
     	
    	if (opponentCorners.size() > 1) {
    		int pickIdx = rand.nextInt(opponentCorners.size());
    		IntPair coordPicked = opponentCorners.get(pickIdx);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	} else if (opponentCorners.size() == 1) {
    		IntPair coordPicked = opponentCorners.get(0);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	}
    			
    	//Empty corner: The player plays in a corner square.
    	//Add functionality to pick a random corner if multiple are empty
    	ArrayList<IntPair> emptyCorners = new ArrayList<>();
    	
    	cellPicked = myGame.getGameCell(0, 0);
    	if (cellPicked.isEmpty()) {
    		emptyCorners.add(new IntPair(0, 0));
    	}
    	cellPicked = myGame.getGameCell(0, 2);
    	if (cellPicked.isEmpty()) {
    		emptyCorners.add(new IntPair(0, 2));
    	}
    	cellPicked = myGame.getGameCell(2, 0);
    	if (cellPicked.isEmpty()) {
    		emptyCorners.add(new IntPair(2, 0));
    	}
    	cellPicked = myGame.getGameCell(2, 2);
    	if (cellPicked.isEmpty()) {
    		emptyCorners.add(new IntPair(2, 2));
    	}
    	
    	if (emptyCorners.size() > 1) {
    		int pickIdx = rand.nextInt(emptyCorners.size());
    		IntPair coordPicked = emptyCorners.get(pickIdx);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	} else if (emptyCorners.size() == 1) {
    		IntPair coordPicked = emptyCorners.get(0);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	}
    	
    	//Empty side: The player plays in a middle square on any of the 4 sides.
    	//Add functionality to pick a random corner if multiple are empty
    	ArrayList<IntPair> emptyEdges = new ArrayList<>();
    	
    	cellPicked = myGame.getGameCell(0, 1);
    	if (cellPicked.isEmpty()) {
    		emptyEdges.add(new IntPair(0, 1));
    	}
    	cellPicked = myGame.getGameCell(1, 0);
    	if (cellPicked.isEmpty()) {
    		emptyEdges.add(new IntPair(1, 0));
    	}
    	cellPicked = myGame.getGameCell(2, 1);
    	if (cellPicked.isEmpty()) {
    		emptyEdges.add(new IntPair(2, 1));
    	}
    	cellPicked = myGame.getGameCell(1, 2);
    	if (cellPicked.isEmpty()) {
    		emptyEdges.add(new IntPair(1, 2));
    	}
    	
    	if (emptyEdges.size() > 1) {
    		int pickIdx = rand.nextInt(emptyEdges.size());
    		IntPair coordPicked = emptyEdges.get(pickIdx);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	} else if (emptyEdges.size() == 1) {
    		IntPair coordPicked = emptyEdges.get(0);
    		cellPicked = myGame.getGameCell (coordPicked.getX(), coordPicked.getY());
    		cellPicked.playPiece(this);
    		return;
    	}

		//Catch All: choose next empty cell in Grid Index order
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cellPicked = myGame.getGameCell(i, j);
    			if (cellPicked.isEmpty()) {
    				cellPicked.playPiece(this);
    	        	return;
    			}
			}
		}

	}
	
}
