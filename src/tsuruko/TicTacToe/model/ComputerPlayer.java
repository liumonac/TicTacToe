package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

	private final Random rand = new Random();
	
	public ComputerPlayer () {
		this.shapeUsed = CIRCLE;
		this.playerName = "Computer";
	}
	
	public ComputerPlayer (String shapeChosen) {
		super(shapeChosen);
		this.playerName = "Computer";
	}

	public ComputerPlayer (String shapeChosen, String playerName) {
		super(shapeChosen, playerName);
	}
	
	//helper for determining first Edge move
	//return index of first move played, null if no move has been played yet or
	//more than 1 move has been played
	private IntPair firstMoveEdge (TicTacToeGame myGame) {
		IntPair result = null;
		ArrayList<GameCell> filledCells = myGame.getFilledCells();
		
		if (filledCells.size() == 1) {
        	if (filledCells.get(0).getCellType() == GameCell.EDGE) {
        		result = filledCells.get(0).getIdx();
        	}
    	}

    	return result;
	}
	
	//Helper function for randomizing moves
	private GameCell pickRandomCell(ArrayList<GameCell> list) {
		GameCell result = null;
    	if (list.size() > 0) {
    		int pickIdx = rand.nextInt(list.size());
    		result =list.get(pickIdx);
    	}
    	
    	return result;
	}
	
	//Grid Indexes
	// 0,0 | 0,1 | 0,2        0 | 1 | 2
	// 1,0 | 1,1 | 1,2		  3 | 4 | 5
	// 2,0 | 2,1 | 2,2	      6 | 7 | 8
	//Using Newell and Simon's 1972 tic-tac-toe program Strategy
	public GameCell chooseMove (TicTacToeGame myGame, Player humanPlayer) {
		GameCell cellPicked = null;
		
		//if opening choose a random move for more fun, to avoid always picking center
		// (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)
		if (! myGame.boardIsEmpty()) {
	
			//if opening move going second
			//An edge opening must be answered either with a center mark, 
			//  a corner mark next to the X, or an edge mark opposite the X. 
			//4 options:
			//  Keep games more interesting by randomly picking one of the 4 options
			//  instead of always picking center
			IntPair moveIdx = firstMoveEdge(myGame);
			if (moveIdx != null) {
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
		            	cellPicked = myGame.getOppositeCell(myGame.getGameCell(moveIdx));
		            	break;
		        }
			    return cellPicked;
			}
			
			//check if I win with the next move
			cellPicked = myGame.getWinningMove (this);
			if (cellPicked != null) {
				cellPicked.playPiece(this);
				return cellPicked;
			}
	
	    	//check if we need to block the opponent's winning move
			cellPicked = myGame.getWinningMove (humanPlayer);
			if (cellPicked != null) {
				return cellPicked;
			}
	    	
	    	//Fork: Create an opportunity where the player has two threats to win (two non-blocked lines of 2).
			//Need to look ahead 2 moves
	    	
	    	//Blocking an opponent's fork: If there is only one possible fork for the opponent, the player should block it. Otherwise, the player should block any forks in any way that simultaneously allows them to create two in a row. Otherwise, the player should create a two in a row to force the opponent into defending, as long as it doesn't result in them creating a fork. For example, if "X" has two opposite corners and "O" has the center, "O" must not play a corner in order to win. (Playing a corner in this scenario creates a fork for "X" to win.)
	    	
	    	//Center: A player marks the center.
			cellPicked = myGame.getGameCell(1, 1);
	    	if (cellPicked.isEmpty()) {
	    		return cellPicked;
	    	}
	    	
	    	//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
	    	//Add functionality to pick a random corner if multiple are already played by opponent
	    	ArrayList<GameCell> emptyCorners = myGame.getEmptyCells(GameCell.CORNER);
	    	ArrayList<GameCell> possibleMoves = new ArrayList<>();
	    	
	    	for (GameCell cell : emptyCorners) {
	    		GameCell oppositeCell = myGame.getOppositeCell(cell);
	    		if (oppositeCell.isPlayedBy(humanPlayer)) {
	    			possibleMoves.add(cell);
	    		}
	    	}

	    	cellPicked = pickRandomCell(possibleMoves);
	    	if (cellPicked != null) {
	    		return cellPicked;
	    	}

	    	//Empty corner: The player plays in a corner square.
	    	//Add functionality to pick a random corner if multiple are empty  	
	    	cellPicked = pickRandomCell(emptyCorners);
	    	if (cellPicked != null) {
	    		return cellPicked;
	    	}
	    	
	    	//Empty side: The player plays in a middle square on any of the 4 sides.
	    	//Add functionality to pick a random edge if multiple are empty
	    	cellPicked = pickRandomCell(myGame.getEmptyCells(GameCell.EDGE));
	    	if (cellPicked != null) {
	    		return cellPicked;
	    	}

		}

		//Catch All: choose a random empty cell
		cellPicked = pickRandomCell(myGame.getEmptyCells());
    	
    	return cellPicked;
	}
}
