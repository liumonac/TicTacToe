package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class TicTacToeGame {
	private GridPane gameBoard;
	
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player computerPlayer;
    private Player humanPlayer;

    private boolean useComputerPlayer = false;
    
    private double boardCellWidth = 100;
    private double boardCellHeight = 100;
    
    //avoids having to cast node using getChildren()
    private ArrayList<GameCell> allCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> emptyCells = new ArrayList<GameCell>();
    private ArrayList<GameCell> winCells = new ArrayList<GameCell>();


    /*********************************************
     * 
     * Constants for labeling cell types
     * 
     * 	Grid Indexes:
	 *	 0,0 | 0,1 | 0,2	0 | 1 | 2
	 *	 1,0 | 1,1 | 1,2	3 | 4 | 5
	 *	 2,0 | 2,1 | 2,2	6 | 7 | 8
     * 
     *********************************************/
    private static final IntPair[] rightDiagonal = new IntPair[] { new IntPair(0, 2), 
															  	   new IntPair(1, 1),
																   new IntPair(2, 0)};

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public TicTacToeGame() {
		gameBoard = new GridPane();

    	player1 = new Player();
    	player2 = new Player("o");
    	
    	initiate();
	}
	
	public TicTacToeGame(GridPane g) {
		gameBoard = g;
		
    	player1 = new Player();
    	player2 = new Player("o");
    	
    	initiate();
	}
	
    /*********************************************
     * 
     * General Game Board Getters
     * 
     *********************************************/
	public String getCurrentPlayerTurn() {
		return currentPlayer.getPlayerName() + "'s Turn " + 
					"(" + currentPlayer.getPlayerType() + ")";
	}

    public void toggleCurrentPlayer() {
    	if (currentPlayer.equals(player1)) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
	
	public String getWinMesage() {
    	if (useComputerPlayer) {
    		if (currentPlayer.equals(computerPlayer)) {
    			return "You lost!";
    		} else {
    			return "You win!";
    		}
    	} else {
    		return (currentPlayer.getPlayerName() + " wins!");
    	}
	}
	
    /*********************************************
     * 
     * General Game Board Functionality
     * 
     *********************************************/
	public void newGame(boolean useComputer) {
		useComputerPlayer = useComputer;
		newGame();
	}
	
	public void newGame() {
		clearBoard();
		
		currentPlayer = player1;

    	Random rand = new Random();
    	int whoFirst = rand.nextInt(2);
    	
    	if (whoFirst == 0  
    			|| 1 == 1  //debugging
    	   ) {
    		computerPlayer = player2;
    		humanPlayer    = player1;
    	} else {
    		computerPlayer = player1;
    		humanPlayer    = player2;
    	}
    	
		computerPlayer.setPlayerName (useComputerPlayer);
    	
        if (useComputerPlayer && computerPlayer.equals(player1)) {
        	GameCell cell = processComputerMove();
        	
        	if (cell != null) {
        		cell.playPiece(currentPlayer);
        		emptyCells.remove(cell);

        		Timeline compT = cell.getMyShape().startAnimation();
        		compT.setOnFinished(computerEvent -> {
        			cell.getMyShape().stopAnimation();
        			toggleCurrentPlayer();
        		});
        		compT.play();
        	}
        	
        }
	}
	
	public String toggleComputerPlayer() {
		String status = "";
		if (useComputerPlayer) {
			useComputerPlayer = false;
			status = "AI OFF";
		} else {
			useComputerPlayer = true;
			status = "AI ON";
		}
		computerPlayer.setPlayerName (useComputerPlayer);
		return status;
	}
    	
    /*********************************************
     * 
     * Process Player Moves
     * 
     *********************************************/
    public boolean processHumanMove (GameCell cell) {
    	if (cell.isEmpty()) {
    		cell.playPiece(currentPlayer);
    		emptyCells.remove(cell);

    		return true;
    	} else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(cell.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
    	}
    	
    	return false;
    }
    
    public GameCell processComputerMove() {
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
    	
    	GameCell cell = chooseMove();
    	if (cell != null) {
	   		cell.playPiece(currentPlayer);
	   		emptyCells.remove(cell);
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
	
    /*********************************************
     * 
     * Game Cell Getters (General)
     * 
     *********************************************/
    public GameCell getGameCell (IntPair coordinates) {
        for (GameCell cell : allCells) {
        	if (cell.getIdx().equals (coordinates)) {
        		return cell;
        	}
        }

        return null;
    }

    public GameCell getGameCell (int row, int column) {
        return getGameCell(new IntPair (row, column));
    }
    
    public GameCell getOppositeCell (GameCell cell) {
    	GameCell result = null;
    	IntPair coordinates = cell.getIdx();
    	int x = getOppositeIdx (coordinates.getX());
    	int y = getOppositeIdx (coordinates.getY());
    	
    	if (x != -1 && y != -1 ) {
    		result = getGameCell (x, y);
    	}

    	return result;
    }
    
	/*********************************************
	 * 
	 * Game Cell Getters (filled)
	 * 
	 *********************************************/
    public ArrayList<GameCell> getFilledCells () {
    	ArrayList<GameCell> filledCells = new ArrayList<>();
    	
        for (GameCell cell : allCells) {
        	if (! cell.isEmpty()) {
        		filledCells.add(cell);
        	}
        }
		
		return filledCells;
    }

    public ArrayList<GameCell> getFilledCells (Player p, int cellType) {
    	ArrayList<GameCell> filledCells = new ArrayList<>();
    	
    	if (cellType == GameCell.CORNER 
    			|| cellType == GameCell.EDGE 
    			|| cellType == GameCell.CENTER
    		) {
	        for (GameCell cell : allCells) {
	        	if (cell.isPlayedBy(p) && cell.getCellType() == cellType) {
	        		filledCells.add(cell);
	        	}
	        }
    	}
		
		return filledCells;
    }
    
	/*********************************************
	 * 
	 * Game Cell Getters (empty)
	 * 
	 *********************************************/
    public ArrayList<GameCell> getEmptyCells () {
		return emptyCells;
    }
    
    public ArrayList<GameCell> getEmptyCells (int cellType) {
    	ArrayList<GameCell> filteredCells = new ArrayList<>();
    	
    	if (cellType == GameCell.CORNER 
    			|| cellType == GameCell.EDGE 
    			|| cellType == GameCell.CENTER
    		) {
    		
            for (GameCell cell : emptyCells) {
            	if (cell.getCellType() == cellType) {
            		filteredCells.add(cell);
            	}
            }
            
    	} else {
    		System.out.println("Error: Invalid Game Cell Type");
    	}

		return filteredCells;
    }
    
    public GameCell getCenter(boolean isEmpty) {
    	GameCell center = getGameCell (1, 1);
    	
    	if (isEmpty) {
    		if (center.isEmpty()) {
    			return center;
    		}
    	} else {
    		return center;
    	}

        return null;
    }
    
    /*********************************************
     * 
     * AI logic helpers
     * 
     *********************************************/
	//helper for finding a winning move
	//returns null if no winning move exists
	public GameCell getWinningMove(Player p) {
		for (GameCell cellIterator : emptyCells) {
			if (cellIterator.isEmpty()) {
				cellIterator.playPiece(p);
	        	if (playerHasWon(p)) {
	        		cellIterator.clearPiece();
	        		return cellIterator;
	        	} else {
	        		cellIterator.clearPiece();
	        	}
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
	public ArrayList<GameCell> getForkMoves(Player player) {
		ArrayList<GameCell> possibleForks = new ArrayList<GameCell>();
		GameCell center = getGameCell(1, 1);
		
		//Case 1
		if (center.isPlayedBy(player)) {
			for (GameCell corner : getFilledCells(player, GameCell.CORNER)) {
				GameCell n1 = getGameCell(corner.getNeighbor(0));
				if (n1.isEmpty()) {
					GameCell c1 = getGameCell(n1.getOtherNeighbor(corner.getIdx()));
					if (c1.isEmpty()) {
						possibleForks.add(n1);
					}
				}
				
				GameCell n2 = getGameCell(corner.getNeighbor(1));
				if (n2.isEmpty()) {
					GameCell c2 = getGameCell(n1.getOtherNeighbor(corner.getIdx()));
					if (c2.isEmpty()) {
						possibleForks.add(n2);
					}
				}
			}

		}
		
		//Case 2
		for (GameCell corner : getEmptyCells(GameCell.CORNER)) {
			GameCell n1 = getGameCell(corner.getNeighbor(0));
			GameCell n2 = getGameCell(corner.getNeighbor(1));
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
			for (GameCell edge : getFilledCells(player, GameCell.EDGE)) {
				GameCell n1 = getGameCell(edge.getNeighbor(0));
				GameCell n2 = getGameCell(edge.getNeighbor(1));
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
		for (GameCell corner : getFilledCells(player, GameCell.CORNER)) {
			GameCell opposite = getOppositeCell(corner);
			if (opposite.isPlayedBy(player)) {
				for (int i = 0; i < 2; i++) {
					GameCell e1 = getGameCell(corner.getNeighbor(i));
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
		ArrayList<GameCell> filledCells = getFilledCells();
		
		if (filledCells.size() == 1) {
        	if (filledCells.get(0).getCellType() == GameCell.EDGE) {
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
		
		GameCell center = getCenter(true);
		
		if (center != null) {
			//check opposite corners, opposite edges
			for (GameCell playedCell : getFilledCells() ) {
				if (playedCell.isPlayedBy(p)) {
					if (getOppositeCell(playedCell).isEmpty()) {
						possibleMoves.add(center);
					}
				}
			}
		} else {
			center = getCenter (false);
			//check empty center cases
			for (GameCell edge : getEmptyCells(GameCell.EDGE)) {
				//if 1 neighbor is p and other is empty true;
				for (int i = 0; i < 2; i++) {
					GameCell c1 = getGameCell(edge.getNeighbor(i));
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
				
				
			for (GameCell corner : getEmptyCells(GameCell.CORNER)) {
				//if neighbor1 is empty and neighbor1's other neighbor is p true;
				//if neighbor1 is p and neigbor1's other neighbor is empty true;
				for (int i = 0; i < 2; i++) {
					GameCell e = getGameCell(corner.getNeighbor(i));
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
	public GameCell chooseMove () {
		GameCell cellPicked = null;
		
		//if opening choose a random move for more fun, to avoid always picking center
		// (If it is the first move of the game, playing on a corner gives the second player more opportunities to make a mistake and may therefore be the better choice; however, it makes no difference between perfect players.)
		if (! boardIsEmpty()) {
	
			//if opening move going second
			//An edge opening must be answered either with a center mark, 
			//  a corner mark next to the X, or an edge mark opposite the X. 
			//4 options:
			//  Keep games more interesting by randomly picking one of the 4 options
			//  instead of always picking center
			GameCell firstMove = firstMoveEdge();
			if (firstMove != null) {
				Random rand = new Random();
				int chooseOption = rand.nextInt(4);
		    	
		    	cellPicked = getGameCell (1, 1);
	
		    	switch (chooseOption) {
		        	case 0:
		        		cellPicked = getGameCell (firstMove.getNeighbor(0));
		                break;
		            case 1:
		            	cellPicked = getGameCell (firstMove.getNeighbor(1));
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
				
				/*
				 * Case 2: Otherwise, the player should block any forks in any way that 
				 *         simultaneously allows them to create two in a row as long as it 
				 *         doesn't result in them creating a fork. 
				 *         
				 * For example, if "X" has two opposite corners and "O" has the center, 
				 *    "O" must not play a corner in order to win. 
				 *    (Playing a corner in this scenario creates a fork for "X" to win.)
				 *      
				 * Block case 3: (block perspective, playing as o) 
				 *   x corner and opposite; o center; o cannot pick corner
				 * 	  x - -
				 * 	  - o -
				 * 	  - - x
				 */
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
				cellPicked = getCenter(true);
			}

			//Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
	    	//Add functionality to pick a random corner if multiple are already played by opponent
			if (cellPicked == null) {
		    	ArrayList<GameCell> emptyCorners = getEmptyCells(GameCell.CORNER);
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
				cellPicked = pickRandomCell(getEmptyCells(GameCell.EDGE));
			}
		}

		//Catch All: choose a random empty cell
		if (cellPicked == null) {
			cellPicked = pickRandomCell(getEmptyCells());
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
    
    public boolean isDraw () {
    	if (emptyCells.isEmpty() && !hasWinner() ) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasWinner() {
    	if ( playerHasWon (player1) || playerHasWon (player2) ) {
    		currentPlayer = winCells.get(0).getPlayer();
    		
    		for (GameCell cell : winCells) {
    			cell.setColor (Color.RED);
    		}
    		return true;
    	}
    	return false;
    }

    public boolean playerHasWon (Player p) {
    	//only 2 moves have been played, we don't need to bother checking
    	if (emptyCells.size() > 6) {
    		return false;
    	}
    	
    	//check rows
    	for (int i = 0; i < 3; i++) {
    		winCells.clear();
    		for (int j = 0; j < 3; j++) {
    			GameCell cell = getGameCell(i, j);
    			
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
    			GameCell cell = getGameCell(i, j);
    			
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
    	//(0,0), (1,1), (2,2)
    	winCells.clear();
    	for (int i = 0; i < 3; i++) {
    		GameCell cell = getGameCell(i, i);
    		
    		if (cell.isPlayedBy(p)) {
    			winCells.add(cell);
    		} else {
    			break;
    		}

    		if (winCells.size() == 3) {
    			return true;
    		}
    	}
    	
    	//check right-left diagonal
    	winCells.clear();
    	for (int i = 0; i < rightDiagonal.length; i++) {
    		GameCell cell = getGameCell(rightDiagonal[i]);
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
			emptyCells.add(cell);
			cell.clearPiece();
		}
	}
	
    private void initiate() {
    	allCells = new ArrayList<>();
    	emptyCells = new ArrayList<>();
    	
    	for (Node node : gameBoard.getChildren()) {
    		if (node.getClass() == GameCell.class) {
    			GameCell cell = (GameCell) node;

    			allCells.add(cell);
    			if (cell.isEmpty()) {
    				emptyCells.add(cell);
    			}
    		}
    	}
    	
    	setSize();
    }
    
    private int getOppositeIdx (int idx) {
    	int result = -1;
    	
    	switch (idx) {
    		case 0:
    			result = 2;
    			break;
    		case 1:
    			result = 1;
    			break;
    		case 2:
    			result = 0;
    			break;
    	}
    	
    	return result;
    }

}
