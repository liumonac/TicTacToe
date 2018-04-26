package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tsuruko.TicTacToe.util.*;
import tsuruko.TicTacToe.view.GameBoardController;

public class TicTacToeGame {
	//Reference back to Controller
	private GameBoardController controller;
	
	//Player Variables
    private Player player1;
    private Player player2;
    
    private Player currentPlayer;
    private Player humanPlayer;
    private Player computerPlayer;

    private boolean useComputerPlayer = false;
    private GameOptions AiType = GameOptions.NewellSimon;
    
    //Board Variables
	private GridPane gameBoard;
	private GameStatus gameStatus = GameStatus.NONE;
	
    private double boardCellWidth = 100;
    private double boardCellHeight = 100;
    
    //avoids having to cast node using getChildren()
    private GameCell[] allCells = new GameCell[9];

    private ArrayList<GameCell> filledCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> emptyCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> winCells = new ArrayList<GameCell>();


    /*********************************************
     * 
     * Constants for labeling cell types
     * 
     *********************************************/
    private static final int centerID = 4;

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public TicTacToeGame() {
		gameBoard = new GridPane();
    	initiate();
	}
	
	public TicTacToeGame(GridPane g, GameBoardController c) {
		controller = c;
		gameBoard = g;
    	initiate();
	}
	
    /*********************************************
     * 
     * General Game Board Getters
     * 
     *********************************************/
	public String getCurrentPlayerTurn() {
		return currentPlayer.getPlayerName() + "'s Turn " + 
					"(" + currentPlayer.getPlayerType().toString() + ")";
	}
	
	public String getGameStatus() {
		String message = currentPlayer.getPlayerName() + " wins!";
		
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
    	
    	if (whoFirst == 0 && 1==0) {
    		computerPlayer = player2;
    		humanPlayer    = player1;
    	} else {
    		computerPlayer = player1;
    		humanPlayer    = player2;
    	}
    	
		computerPlayer.setPlayerName (useComputerPlayer);
    	controller.setTurn(getCurrentPlayerTurn());
    	
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
    	controller.setTurn(getCurrentPlayerTurn());
    	if (currentPlayer.equals(player1)) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    	controller.setTurn(getCurrentPlayerTurn());
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(cell.getPlayerName() + " already filled that box!");

            alert.showAndWait();
    	}
    	
    	return false;
    }
    
    private GameCell processComputerMove() {
    	//don't play if there are no more moves
    	if (emptyCells.size() == 0) {
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
	public void setSize() {
		boardCellWidth = gameBoard.getWidth() / 3.0;
		boardCellHeight = gameBoard.getHeight() / 3.0;
		
		for (GameCell cell : allCells) {
			cell.setSize (boardCellWidth, boardCellHeight);
		}
	}
	
	public void setAiType(GameOptions useAi) {
		AiType = useAi;
	}
    /*********************************************
     * 
     * Game Cell Getters (General)
     * 
     *********************************************/
    public GameCell getGameCell (IntPair coordinates) {
        return getGameCell(coordinates.getX(), coordinates.getY());
    }

    public GameCell getGameCell (int row, int column) {
    	return allCells[row*3 + column];
    }
    
    public GameCell getOppositeCell (GameCell cell) {
    	IntPair coordinates = cell.getOppositeIdx();
    	GameCell result = getGameCell (coordinates);
    	return result;
    }
    
    /*********************************************
     * 
     * Minimax Algorithm Recursion Helper
     * 
     *********************************************/
    private int MinimaxRecurse (int level, Player player, GameCell tryMove, ArrayList<GameCell> movesLeft) {
    	tryMove.playPiece(player);
    	
    	level = level + 1;

    	if (playerHasWon(humanPlayer)) {
    		System.out.println("level: " + level + " coord: " + tryMove.getIdx().getX() + " " + tryMove.getIdx().getY() + ": " + -10);
    		
    		tryMove.clearPiece();
    		return -10;
    	} else if (playerHasWon(computerPlayer)) {
    		System.out.println("level: " + level + " coord: " + tryMove.getIdx().getX() + " " + tryMove.getIdx().getY() + ": " + 10);
    		
    		tryMove.clearPiece();
    		return 10;
    		//Draw
    	} else if (emptyCells.isEmpty()) {
    		System.out.println("level: " + level + " coord: " + tryMove.getIdx().getX() + " " + tryMove.getIdx().getY() + ": " + 0);
    		
    		tryMove.clearPiece();
    		return 0;
    	}
    	
    	ArrayList<GameCell> availableMoves = new ArrayList<GameCell> (movesLeft);
    	availableMoves.remove(tryMove);
    	
    	//pick best move of the loop
    	//if computer player pick 10
    	//if human pick -10
    	int result = 0;
    	for (GameCell cell : availableMoves) {
    		if (player.equals(computerPlayer)) {
    			int score = MinimaxRecurse (level, humanPlayer, cell, availableMoves);
    			if (score > result) {
    				result = score;
    			}
    		} else {
    			int score = MinimaxRecurse (level, computerPlayer, cell, availableMoves);
    			if (score < result) {
    				result = score;
    			}
    		}
    	}
		System.out.println("continue level: " + level + " coord: " + tryMove.getIdx().getX() + " " + tryMove.getIdx().getY() + ": " + result);
		
    	tryMove.clearPiece();
    	return result;

    }

    /*********************************************
     * 
     * AI using Minimax Algorithm
     * 
     *********************************************/
    /*
     * 
     * 1. return a value if a terminal state is found (+10, 0, -10)
     * 2. go through available spots on the board
     * 3. call the minimax function on each available spot (recursion)
     * 4. evaluate returning values from function calls
     * 5. and return the best value
     */
    
    public GameCell chooseMoveMinimax () {
    	GameCell bestMove = null;
    	ArrayList<GameCell> checkMoves = new ArrayList<GameCell>(emptyCells);
    	
    	int currentScore = -100;
    	
    	for (GameCell cell : emptyCells) {
    		int score = MinimaxRecurse (0, computerPlayer, cell, checkMoves);
    		if (score > currentScore) {
    			currentScore = score;
    			bestMove = cell;
    		}
    	}
    	
		return bestMove;
    }
    

    /*********************************************
     * 
     * Newell and Simon AI logic helpers
     * 
     *********************************************/
	//helper for finding a winning move
	//returns null if no winning move exists
	private GameCell getWinningMove(Player p) {
		for (GameCell cellIterator : emptyCells) {
			cellIterator.playPiece(p);
        	if (playerHasWon(p)) {
        		cellIterator.clearPiece();
        		return cellIterator;
        	} else {
        		cellIterator.clearPiece();
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
		GameCell center = allCells[centerID];
		
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
			GameCell n1 = getGameCell(corner.getNeighbors().get(0));
			GameCell n2 = getGameCell(corner.getNeighbors().get(1));
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
				GameCell n1 = getGameCell(edge.getNeighbors().get(0));
				GameCell n2 = getGameCell(edge.getNeighbors().get(1));
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
	
	//if this is the second move of the game,
	//return index of first move played if it was an edge
	private GameCell firstMoveEdge () {
		GameCell result = null;

		if (filledCells.size() == 1) {
        	if (filledCells.get(0).getCellType() == CellType.EDGE) {
        		result = filledCells.get(0);
        	}
    	}

    	return result;
	}
	
	//Helper function for randomizing moves
	private GameCell pickRandomCell(ArrayList<GameCell> list) {
		GameCell result = null;
    	if (list.size() > 0) {
    		Random rand = new Random();
    		int pickIdx = rand.nextInt(list.size());
    		result =list.get(pickIdx);
    	}
    	
    	return result;
	}
	
	private ArrayList<GameCell> makeTwoInARow (Player p) {
		ArrayList<GameCell> possibleMoves = new ArrayList<GameCell>();
		
		GameCell center = allCells[centerID];
		
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
     * 	Grid Indexes
     * 0,0 | 0,1 | 0,2        0 | 1 | 2
     * 1,0 | 1,1 | 1,2		  3 | 4 | 5
     * 2,0 | 2,1 | 2,2	      6 | 7 | 8
     * 
     *********************************************/
	public GameCell chooseMoveNS () {
		GameCell cellPicked = null;
		
		//if opening choose a random move for more fun, to avoid always picking center
		// (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)
		if (! boardIsEmpty()) {
	
			 /*********************************************
			  * An edge opening must be answered either with a center mark, 
			  *   a corner mark next to the X, or an edge mark opposite the X. 
			  *   4 options:
			  *     Keep games more interesting by randomly picking one of the 4 options
			  *     instead of always picking center
			  *********************************************/
			GameCell firstMove = firstMoveEdge();
			if (firstMove != null) {
				Random rand = new Random();
				int chooseOption = rand.nextInt(4);
		    	
		    	cellPicked = getGameCell (1, 1);
	
		    	switch (chooseOption) {
		        	case 0:
		        		cellPicked = getGameCell (firstMove.getNeighbors().get(0));
		                break;
		            case 1:
		            	cellPicked = getGameCell (firstMove.getNeighbors().get(1));
		            	break;
		            case 2:
		            	cellPicked = getOppositeCell(firstMove);
		            	break;
		        }
			}
			
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
		    	ArrayList<GameCell> possibleMoves = getForkMoves(computerPlayer);
		    	cellPicked = pickRandomCell(possibleMoves);
			}

			//Block an opponent's fork
			if (cellPicked == null) {
				ArrayList<GameCell> possibleMoves = getForkMoves (humanPlayer);
				
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
				cellPicked = allCells[centerID];
				if (!cellPicked.isEmpty()) {
					cellPicked = null;
				}
			}

			//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
	    	//Add functionality to pick a random corner if multiple are already played by opponent
			if (cellPicked == null) {
		    	ArrayList<GameCell> emptyCorners = getEmptyCells(CellType.CORNER);
		    	ArrayList<GameCell> possibleMoves = new ArrayList<>();

		    	for (GameCell cell : emptyCorners) {
		    		GameCell oppositeCell = getOppositeCell(cell);
		    		if (oppositeCell.isPlayedBy(player1)) {
		    			possibleMoves.add(cell);
		    		}
		    	}
		    	
		    	cellPicked = pickRandomCell(possibleMoves);
		    	
		    	//Empty corner: The player plays in a corner square.
		    	//Add functionality to pick a random corner if multiple are empty  	
		    	if (cellPicked == null) {
			    	cellPicked = pickRandomCell(emptyCorners);
		    	}
			}
			
			//Empty side: The player plays in a middle square on any of the 4 sides.
	    	//Add functionality to pick a random edge if multiple are empty
			if (cellPicked == null) {
				cellPicked = pickRandomCell(getEmptyCells(CellType.EDGE));
			}
		}

		//Catch All: choose a random empty cell
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
    	for (GameCell cell : allCells) {
    		if (cell.isAnimation()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean boardIsEmpty() {
    	if (emptyCells.size() == 9) {
    		return true;
    	}
    	return false;
    }
    
    public boolean gameOver() {
    	if (hasWinner()) {
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
    
    public boolean hasWinner() {
    	if (playerHasWon (currentPlayer)) {
    		for (GameCell cell : winCells) {
    			cell.setColor (Color.RED);
    		}
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

    	//check left-right diagonal
    	//0, 4, 8
    	winCells.clear();
    	for (int i = 0; i < 9; i=i+4) {
    		GameCell cell = allCells[i];
    		
    		if (cell.isPlayedBy(p)) {
    			winCells.add(cell);
    		} else {
    			break;
    		}
    	}
		if (winCells.size() == 3) {
			return true;
		}
    	
    	//check right-left diagonal
		//2, 4, 6
    	winCells.clear();
    	for (int i = 0; i < 8; i=i+2) {
    		GameCell cell = allCells[i];
    		if (cell.isPlayedBy(p)) {
    			winCells.add(cell);
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
		for (GameCell cell : allCells) {
			cell.clearPiece();
			emptyCells.add(cell);
		}
	}
	
    private void initiate() {
    	player1 = new Player();
    	player2 = new Player("o");
    	
    	emptyCells = new ArrayList<>();
    	
    	//save the gridPane's cell children to avoid having to case Node 
    	//and avoid dealing with the scene group child for future logic
    	int i = 0;
    	for (Node node : gameBoard.getChildren()) {
    		if (node.getClass() == GameCell.class) {
    			GameCell cell = (GameCell) node;

    			allCells[i] = cell;
    			i++;
    			if (cell.isEmpty()) {
    				emptyCells.add(cell);
    			}
    		}
    	}
    	
    	setSize();
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
        	if (cell.isPlayedBy(p) && cell.getCellType() == cellType) {
        		filteredCells.add(cell);
        	}
        }
		
		return filteredCells;
    }

    private ArrayList<GameCell> getEmptyCells (CellType cellType) {
    	ArrayList<GameCell> filteredCells = new ArrayList<>();
    	
        for (GameCell cell : emptyCells) {
        	if (cell.getCellType() == cellType) {
        		filteredCells.add(cell);
        	}
        }

		return filteredCells;
    }
}
