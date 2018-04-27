package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tsuruko.TicTacToe.util.*;

public class TicTacToeGame {
	//Player Variables
    private Player player1;
    private Player player2;
    
    private Player currentPlayer;
    private Player humanPlayer;
    private Player computerPlayer;

    private boolean useComputerPlayer = false;
    
    private GameOptions AiType = GameOptions.NewellSimon;
	private GameStatus gameStatus = GameStatus.NONE;
    
    //Board Variables
	//avoids having to cast node using getChildren()
    private GameCell[] gameBoard = new GameCell[9];
    private ArrayList<GameCell> filledCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> emptyCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> winCells = new ArrayList<GameCell>();

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public TicTacToeGame() {
    	player1 = new Player(PlayerType.PLAYER1);
    	player2 = new Player(PlayerType.PLAYER2);
	}
	
	public TicTacToeGame(GridPane display) {
    	this();

    	//save the gridPane's cell children to avoid having to case Node 
    	//and avoid dealing with the scene group child for future logic
    	int i = 0;
    	for (Node node : display.getChildren()) {
    		if (node.getClass() == GameCell.class) {
    			gameBoard[i] = (GameCell) node;
    			emptyCells.add(gameBoard[i]);
    			i++;
    		}
    	}
    	
    	setSize(display.getWidth(), display.getHeight());
	}
	
    /*********************************************
     * 
     * General Game Board Getters
     * 
     *********************************************/
	public String getCurrentPlayerTurn() {
		String message = currentPlayer.getName() + "'s Turn ";
		message = message + "(" + currentPlayer.getShape() + ")";
		return message;		
	}
	
	public String getGameStatus() {
		String message = currentPlayer.getName() + " wins!";
		
    	if (useComputerPlayer) {
    		if (currentPlayer.equals(computerPlayer)) {
    			message = "You lost!";
    		} else {
    			message = "You win!";
    		}
    	}
    	
		if (gameStatus == GameStatus.DRAW) {
			message = "It's a draw!";
		}
		
    	return message;
	}
	
    /*********************************************
     * 
     * General Game Board Functionality
     * 
     *********************************************/
	public void newGame(boolean useAi) {
		useComputerPlayer = useAi;
		newGame();
	}
	
	public void newGame() {
		clearBoard();
		
		//clear computer since we're re-picking who goes first
		if (computerPlayer != null) {
			computerPlayer.setPlayerName (false);
		}
		
		currentPlayer = player1;

    	Random rand = new Random();
    	int whoFirst = rand.nextInt(2);
    	
    	if (whoFirst == 0) {
    		computerPlayer = player2;
    		humanPlayer    = player1;
    	} else {
    		computerPlayer = player1;
    		humanPlayer    = player2;
    	}
    	
		computerPlayer.setPlayerName (useComputerPlayer);

        GameCell cell = processComputerMove();
        if (cell != null) {
        	Timeline compT = cell.getMyShape().startAnimation();
        	compT.setOnFinished(computerEvent -> {
        		cell.getMyShape().stopAnimation();
        		toggleCurrentPlayer();
        	});
        	compT.play();
       	}
        	
	}
	
	public String toggleComputerPlayer() {
		String status = "AI OFF";
		
		useComputerPlayer = !useComputerPlayer;
		if (useComputerPlayer) {
			status = "AI ON";
		}

		computerPlayer.setPlayerName (useComputerPlayer);

		GameCell computerMove = processComputerMove();
    	if (computerMove != null) {
    		Timeline compT = computerMove.getMyShape().startAnimation();
    		compT.setOnFinished(computerEvent -> {
    			computerMove.getMyShape().stopAnimation();
    		});
    		compT.play();
    	}
    	
		return status;
	}
	
    public void toggleCurrentPlayer() {
    	if (currentPlayer.equals(player1)) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
    	
    /*********************************************
     * 
     * Process Player Moves
     * 
     *********************************************/
    public boolean processHumanMove (GameCell cell) {
    	if (cell.isEmpty()) {
        	fillCell (cell);
        	
        	Timeline humanT = cell.getMyShape().startAnimation();

    		humanT.setOnFinished(humanEvent -> {
    			cell.getMyShape().stopAnimation();
    			
    			if (!gameOver()) {
    				toggleCurrentPlayer();
	        		GameCell computerMove = processComputerMove();
		        	if (computerMove != null) {
		        		Timeline compT = computerMove.getMyShape().startAnimation();
		        		compT.setOnFinished(computerEvent -> {
		        			computerMove.getMyShape().stopAnimation();
		        			toggleCurrentPlayer();
		        		});
		        		compT.play();
		        	}
    			}
        		
    		});
    		
    		humanT.play();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private GameCell processComputerMove() {
    	//don't play if there are no more moves
    	if (emptyCells.isEmpty()) {
    		return null;
    	}
    	
    	//don't play if both players are human
    	if (! useComputerPlayer) {
    		return null;
    	}
    	
    	//don't play if it's not the computer's turn
    	if (! currentPlayer.equals(computerPlayer)) {
    		return null;
    	}
    	
    	GameCell cell;
    	if (AiType == GameOptions.NewellSimon) {
    		cell = chooseMoveNS();
    	} else {
    		cell = chooseMoveMinimax();
    	}
    	
    	if (cell != null) {
        	fillCell (cell);
    	} else {
    		System.out.println("Error: No computer move selected");
    	}
    	
    	return cell;
    }
    
    /*********************************************
     * 
     * Game Cell Setters
     * 
     *********************************************/
	public void setSize(double width, double height) {
		double cellWidth = width / 3.0;
		double cellHeight = height / 3.0;
		
		for (GameCell cell : gameBoard) {
			cell.setSize (cellWidth, cellHeight);
		}
	}
	
	public void setAiType(GameOptions option) {
		AiType = option;
	}
    
    /*********************************************
     * 
     * AI using Minimax Algorithm with Recursion
     * 
     * Recursion Steps:
     * 1. Terminal state: +10 if computer wins, -10 if human wins, 0 if its a draw
     * 2. for all of the available moves:
     * 3. call Minimax function
     * 4. return the best value
     * 
     *********************************************/
    private int MinimaxRecurse (Player player, int idx, ArrayList<GameCell> movesLeft) {
    	if (movesLeft.isEmpty()) {
    		return 0;
    	}
    	
    	GameCell tryMove = movesLeft.get(idx);
    	tryMove.playPiece(player);

    	if (playerHasWon(humanPlayer)) {
    		tryMove.clearPiece();
    		return -10;
    	} else if (playerHasWon(computerPlayer)) {
    		tryMove.clearPiece();
    		return 10;
    		//Draw
    	} else if (emptyCells.isEmpty()) {
    		tryMove.clearPiece();
    		return 0;
    	}
    	
    	ArrayList<GameCell> availableMoves = new ArrayList<GameCell> (movesLeft);
    	availableMoves.remove(tryMove);
    	
    	int result = 0;
    	for (int i = 0; i < availableMoves.size(); i++) {
    		if (player.equals(computerPlayer)) {
    			int score = MinimaxRecurse (humanPlayer, i, availableMoves);
    			if (score < result) {
    				result = score;
    			}
    		} else {
    			int score = MinimaxRecurse (computerPlayer, i, availableMoves);
    			if (score > result) {
    				result = score;
    			}
    		}
    	}
    	tryMove.clearPiece();
    	return result;
    }
    
    public GameCell chooseMoveMinimax () {
    	GameCell bestMove = null;
    	
		//if opening move choose a random move for more fun
		if (filledCells.isEmpty()) {
			bestMove = pickRandomCell(emptyCells);
		} else {
	    	int currentScore = -100;
	    	
	    	for (int i = 0; i < emptyCells.size(); i++) {
	    		int score = MinimaxRecurse (computerPlayer, i, emptyCells);
	    		if (score > currentScore) {
	    			currentScore = score;
	    			bestMove = emptyCells.get(i);
	    		}
	    	}
		}
		
		return bestMove;
    }

    /*********************************************
     * 
     * Game Cell Getter Helpers
     * 
     *********************************************/
    private GameCell getGameCell (IntPair coordinates) {
        return getGameCell(coordinates.getX(), coordinates.getY());
    }

    private GameCell getGameCell (int row, int column) {
    	return gameBoard[row*3 + column];
    }
    
    private GameCell getOppositeCell (GameCell cell) {
    	IntPair coordinates = cell.getOppositeIdx();
    	GameCell result = getGameCell (coordinates);
    	return result;
    }
    
	//Helper function for randomizing moves
	private GameCell pickRandomCell(ArrayList<GameCell> list) {
		GameCell result = null;
    	if (!list.isEmpty()) {
    		Random rand = new Random();
    		int pickIdx = rand.nextInt(list.size());
    		result = list.get(pickIdx);
    	}
    	
    	return result;
	}

    /*********************************************
     * 
     * Newell and Simon AI logic helpers
     * 
     *********************************************/
	//helper for finding a winning move
	private GameCell getWinningMove(Player p) {
		for (GameCell cell : emptyCells) {
			cell.playPiece(p);
        	if (playerHasWon(p)) {
        		cell.clearPiece();
        		return cell;
        	} else {
        		cell.clearPiece();
        	}
		}
		
		return null;
	}
	
	/*
	 * 	Fork: Create an opportunity where the player has two threats to win 
	 *   (two non-blocked lines of 2).
	 *   No extra code for scenarios where forking requires picking center.
	 *   We can just skip to the next best move in Newell and Simon's strategy 
	 *   	x x o		o x o
	 *   	o ! -		x ! -
	 *   	- - -		- - -
	 *   
	 * Case 1: 
	 *   x corner and center; neighbor corners are empty, pick adjacent edge
	 * 	 	x o -	x ! -
	 * 	 	! x -	o x -
	 * 	 	- - o	- - o
	 * 
	 * Case 2: 
	 *   x adjacent edges; 3 neighbor corners are empty; pick the shared corner
	 *   	! x -
	 *   	x o -
	 *   	- o -
	 * 
	 * Case 3: (2 options)
	 *   x edge and center; neighbor corners empty; pick corner with empty opposite corner
	 * 	  ! x !
	 * 	  - x o
	 * 	  - o -
	 * 
	 * Case 4: (2 options)
	 *   x opposite corners; corner free; play corner
	 * 	  x - !
	 * 	  - o -
	 * 	  ! - x
	 */
	private ArrayList<GameCell> getForkMoves(Player player) {
		ArrayList<GameCell> possibleForks = new ArrayList<GameCell>();
		GameCell center = getGameCell (1, 1);
		
		//Case 1
		if (center.isPlayedBy(player)) {
			for (GameCell corner : getFilledCells(player, CellType.CORNER)) {
				for (IntPair idx : corner.getNeighbors()) {
					GameCell e = getGameCell(idx);
					if (e.isEmpty()) {
						GameCell c1 = getGameCell(e.getOtherNeighbor(corner.getIdx()));
						if (c1.isEmpty()) {
							possibleForks.add(e);
						}
					}
				}
			}
		}
		
		//Case 2
		for (GameCell corner : getEmptyCells(CellType.CORNER)) {
			GameCell n1 = getGameCell(corner.getNeighbors()[0]);
			GameCell n2 = getGameCell(corner.getNeighbors()[1]);
			if (n1.isPlayedBy(player) && n2.isPlayedBy(player)) {
				GameCell c1 = getGameCell(n1.getOtherNeighbor(corner.getIdx()));
				GameCell c2 = getGameCell(n2.getOtherNeighbor(corner.getIdx()));
				if (c1.isEmpty() && c2.isEmpty()) {
					possibleForks.add(corner);
				}
			}
		}
		
		//Case 3
		if (center.isPlayedBy(player)) {
			for (GameCell edge : getFilledCells(player, CellType.EDGE)) {
				GameCell n1 = getGameCell(edge.getNeighbors()[0]);
				GameCell n2 = getGameCell(edge.getNeighbors()[1]);
				if (n1.isEmpty() && n2.isEmpty()) {
					if (getOppositeCell(n1).isEmpty()) {
						possibleForks.add(n1);
					}
					if (getOppositeCell(n2).isEmpty()) {
						possibleForks.add(n2);
					}
				}
			}
		}
		
		//Case 4
		for (GameCell corner : getFilledCells(player, CellType.CORNER)) {
			GameCell opposite = getOppositeCell(corner);
			if (opposite.isPlayedBy(player)) {
				for (IntPair idx : corner.getNeighbors()) {
					GameCell e1 = getGameCell(idx);
					if (e1.isEmpty()) {
						GameCell c = getGameCell(e1.getOtherNeighbor(corner.getIdx()));
						if (c.isEmpty()) {
							GameCell e2 = getGameCell(c.getOtherNeighbor(e1.getIdx()));
							if (e2.isEmpty()) {
								possibleForks.add(c);
							}
						}
					}
				}
			}
		}
		
		return possibleForks;
	}
	

	 /*********************************************
	  * If this is the second move of the same, and my first move
	  * 
	  * An edge opening must be answered either with a center mark, 
	  *   a corner mark next to the X, or an edge mark opposite the X. 
	  *   4 options:
	  *     Keep games more interesting by randomly picking one of the 4 options
	  *     instead of always picking center
	  *********************************************/
	private GameCell firstMoveEdge () {
		GameCell result = null;

		if (filledCells.size() == 1) {
        	if (filledCells.get(0).isType(CellType.EDGE)) {
        		GameCell firstMove = filledCells.get(0);
        		ArrayList<GameCell> possibleMoves = new ArrayList<GameCell>();
        		
    			possibleMoves.add(getGameCell (1, 1));
    			possibleMoves.add(getGameCell (firstMove.getNeighbors()[0]));
    			possibleMoves.add(getGameCell (firstMove.getNeighbors()[1]));
    			possibleMoves.add(getOppositeCell(firstMove));
    			
    			result = pickRandomCell (possibleMoves);
        	}
    	}

    	return result;
	}
	
	private ArrayList<GameCell> makeTwoInARow (Player p) {
		ArrayList<GameCell> possibleMoves = new ArrayList<GameCell>();
		
		GameCell center = getGameCell (1, 1);
		
		if (center.isEmpty()) {
			//check opposite corners, opposite edges
			for (GameCell playedCell : filledCells ) {
				if (playedCell.isPlayedBy(p)) {
					if (getOppositeCell(playedCell).isEmpty()) {
						possibleMoves.add(center);
					}
				}
			}
		} else {
			//check empty center cases
			for (GameCell edge : getEmptyCells(CellType.EDGE)) {
				//if 1 neighbor is p and other is empty true;
				for (IntPair idx : edge.getNeighbors()) {
					GameCell c1 = getGameCell(idx);
					if (c1.isEmpty()) {
						GameCell c2 = getGameCell(edge.getOtherNeighbor(c1.getIdx()));
						if (c2.isPlayedBy(p)) {
							possibleMoves.add(edge);
						}
					}
				}
				//we know that center is filled
				if (center.isPlayedBy(p) && getOppositeCell(edge).isEmpty()) {
					possibleMoves.add(edge);
				}
			}

			for (GameCell corner : getEmptyCells(CellType.CORNER)) {
				//if neighbor1 is empty and neighbor1's other neighbor is p true;
				//if neighbor1 is p and neigbor1's other neighbor is empty true;
				for (IntPair idx : corner.getNeighbors()) {
					GameCell e = getGameCell(idx);
					GameCell c = getGameCell(e.getOtherNeighbor(corner.getIdx()));
					if (e.isEmpty()) {
						if (c.isPlayedBy(p)) {
							possibleMoves.add(corner);
						}
					}
					if (e.isPlayedBy(p)) {
						if (c.isEmpty()) {
							possibleMoves.add(corner);
						}
					}
				}
				//we know that center is filled
				if (center.isPlayedBy(p) && getOppositeCell(corner).isEmpty()) {
					possibleMoves.add(corner);
				}
			}
		}
		
		return possibleMoves;
	}
	
    /*********************************************
     * 
     * AI logic for picking the best move
     * Using Newell and Simon's 1972 tic-tac-toe program Strategy
     * 
     * 
     *********************************************/
	public GameCell chooseMoveNS () {
		GameCell cellPicked = null;
		ArrayList<GameCell> possibleMoves = new ArrayList<>();
		
		//if this is not the opening move
		if (! filledCells.isEmpty()) {
			//check if this is my first move going second
			cellPicked = firstMoveEdge();
			
			//check if I win with the next move
			if (cellPicked == null) {
				cellPicked = getWinningMove (computerPlayer);
			}

			//check if we need to block the opponent's winning move
			if (cellPicked == null) {
				cellPicked = getWinningMove (humanPlayer);
			}

			//Fork
			if (cellPicked == null) {
				possibleMoves = getForkMoves(computerPlayer);
		    	cellPicked = pickRandomCell(possibleMoves);
			}

			//Block an opponent's fork
			if (cellPicked == null) {
				possibleMoves = getForkMoves (humanPlayer);
				
				//Case 1: If there is only one possible fork for the opponent, 
				//        the player should block it.
				if (possibleMoves.size() == 1) {
					cellPicked = possibleMoves.get(0);
				}
				
				 /*********************************************
				 * Case 2: Otherwise, the player should block any forks in any way that 
				 *         simultaneously allows them to create two in a row as long as it 
				 *         doesn't result in them creating a fork. 
				 *********************************************/
				if (possibleMoves.size() > 1) {
					possibleMoves = makeTwoInARow (computerPlayer);
					for (GameCell myMove : possibleMoves) {
						myMove.playPiece(computerPlayer);
						GameCell myNextMove = getWinningMove(computerPlayer);
						for (GameCell opponent : getForkMoves (humanPlayer)) {
							if (!opponent.equals(myNextMove)) {
								cellPicked = myMove;
							}
						}
						myMove.clearPiece();
					}
				}
			}

	    	//Center: A player marks the center.
			if (cellPicked == null) {
				cellPicked = getGameCell (1, 1);
				if (!cellPicked.isEmpty()) {
					cellPicked = null;
				}
			}

			//Opposite corner: If the opponent is in the corner, 
			//                 the player plays the opposite corner.
	    	//Pick a random corner if multiple are already played by opponent
			if (cellPicked == null) {
		    	ArrayList<GameCell> emptyCorners = getEmptyCells(CellType.CORNER);
		    	possibleMoves.clear();

		    	for (GameCell cell : emptyCorners) {
		    		GameCell oppositeCell = getOppositeCell(cell);
		    		if (oppositeCell.isPlayedBy(humanPlayer)) {
		    			possibleMoves.add(cell);
		    		}
		    	}
		    	
		    	cellPicked = pickRandomCell(possibleMoves);
		    	
		    	//Empty corner: The player plays in a corner square.
		    	//Pick a random corner if multiple are empty  	
		    	if (cellPicked == null) {
			    	cellPicked = pickRandomCell(emptyCorners);
		    	}
			}
			
			//Empty side: The player plays in a middle square on any of the 4 sides.
	    	//Pick a random edge if multiple are empty
			if (cellPicked == null) {
				possibleMoves = getEmptyCells(CellType.EDGE);
				cellPicked = pickRandomCell(possibleMoves);
			}
		}

		//if opening move choose a random move for more fun
		if (cellPicked == null) {
			cellPicked = pickRandomCell(emptyCells);
		}
		
    	return cellPicked;
	}
	
    /*********************************************
     * 
     * Check for this game's status
     * 
     *********************************************/
    public boolean isAnimatingPiece() {
    	for (GameCell cell : gameBoard) {
    		if (cell.isAnimation()) {
    			return true;
    		}
    	}
    	return false;
    }

    public boolean gameOver() {
    	//Win
    	if (playerHasWon (currentPlayer)) {
    		for (GameCell cell : winCells) {
    			cell.setColor (Color.RED);
    		}
    		gameStatus = GameStatus.WIN;
    		return true;
    	}  	
    	
    	//Draw
		if (emptyCells.isEmpty()) {
			gameStatus = GameStatus.DRAW;
			return true;
		}
    	return false;
    }


    public boolean playerHasWon (Player p) {
    	//check rows
    	for (int i = 0; i < 3; i++) {
    		winCells.clear();
    		for (int j = 0; j < 3; j++) {
    			GameCell cell = getGameCell (i, j);
    			
    			if (cell.isPlayedBy(p)) {
    				winCells.add(cell);
    			} else {
    				break;
    			}
    		}
    		if (winCells.size() == 3) {
    			return true;
    		}
    	}

    	//check columns
    	for (int j = 0; j < 3; j++) {
    		winCells.clear();
    		for (int i = 0; i < 3; i++) {
    			GameCell cell = getGameCell (i, j);
    			if (cell.isPlayedBy(p)) {
    				winCells.add(cell);
    			} else {
    				break;
    			}
    		}
    		if (winCells.size() == 3) {
    			return true;
    		}
    	}

    	//check left-right diagonal: indices (0, 4, 8)
    	winCells.clear();
    	for (int i = 0; i < 9; i=i+4) {
    		if (gameBoard[i].isPlayedBy(p)) {
    			winCells.add(gameBoard[i]);
    		} else {
    			break;
    		}
    	}
		if (winCells.size() == 3) {
			return true;
		}
    	
    	//check right-left diagonal: indices (2, 4, 6)
    	winCells.clear();
    	for (int i = 2; i < 8; i=i+2) {
    		if (gameBoard[i].isPlayedBy(p)) {
    			winCells.add(gameBoard[i]);
    		} else {
    			break;
    		}
    	}
		if (winCells.size() == 3) {
			return true;
		}

        return false;
    }
    
    
    /*********************************************
     * 
     * Private Helper Functions
     * 
     *********************************************/
    private void clearBoard() {
		emptyCells.clear();
		winCells.clear();
		for (GameCell cell : gameBoard) {
			cell.clearPiece();
			emptyCells.add(cell);
		}
	}
    
    private void fillCell (GameCell cell) {
    	emptyCells.remove(cell);
    	filledCells.add(cell);
    	cell.playPiece(currentPlayer);
    }
    
	/*********************************************
	 * 
	 * Game Cell Getters
	 * 
	 *********************************************/
    private ArrayList<GameCell> getFilledCells (Player p, CellType cellType) {
    	ArrayList<GameCell> filteredCells = new ArrayList<>();
    	
        for (GameCell cell : filledCells) {
        	if (cell.isPlayedBy(p) && cell.isType(cellType)) {
        		filteredCells.add(cell);
        	}
        }
		
		return filteredCells;
    }

    private ArrayList<GameCell> getEmptyCells (CellType cellType) {
    	ArrayList<GameCell> filteredCells = new ArrayList<>();
    	
        for (GameCell cell : emptyCells) {
        	if (cell.isType(cellType)) {
        		filteredCells.add(cell);
        	}
        }

		return filteredCells;
    }
}
