package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

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
    
    private boolean useComputerPlayer;
    
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
    	player2 = new Player("o", "Player 2");
    	
    	initiate();
	}
	
	public TicTacToeGame(GridPane g) {
		gameBoard = g;
		
    	player1 = new Player();
    	player2 = new Player("o", "Player 2");
    	
    	initiate();
	}
	
	public TicTacToeGame(GridPane g, boolean useComputer) {
		gameBoard = g;
		useComputerPlayer = useComputer;

    	player1 = new Player();
    	player2 = new Player("o", "Player 2");
    	
    	if (useComputerPlayer) {
    		player2 = new ComputerPlayer();
    	}
    	
    	initiate();
	}
	
	
	
    /*********************************************
     * 
     * General Game Board Functionality
     * 
     *********************************************/
    public void setComputerPlayer (boolean ai) {
    	useComputerPlayer = ai;
    	if (useComputerPlayer) {
    		player2 = new ComputerPlayer();
    	} else {
    		player2 = new Player("o", "Player 2");
    	}
    }
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
    public void toggleCurrentPlayer() {
    	if (currentPlayer == player1) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
    
	public GridPane getGameBoard() {
		return gameBoard;
	}
	
	public String getWinMesage() {
    	if (useComputerPlayer) {
    		if (!currentPlayer.equals(player1)) {
    			return "You lost!";
    		} else {
    			return "You win!";
    		}
    	} else {
    		return (currentPlayer.getPlayerName() + " wins!");
    	}
	}
	
	public void newGame() {
		setWhoFirst();

        if (useComputerPlayer && currentPlayer!= player1) {
        	GameCell cell = ((ComputerPlayer) player2).chooseMove(this, player1);
        	cell.playPiece(currentPlayer);
        	emptyCells.remove(cell);
        	currentPlayer = player1;
        }
	}
	
	public void clearBoard() {
		emptyCells.clear();
		winCells.clear();
		for (GameCell cell : allCells) {
			emptyCells.add(cell);
			cell.clearPiece();
		}
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
    
    public boolean processComputerMove () {
    	if (useComputerPlayer && currentPlayer != player1) {
    		GameCell cell = ((ComputerPlayer) currentPlayer).chooseMove(this, player1);
    		if (cell != null) {
	    		cell.playPiece(currentPlayer);
	    		emptyCells.remove(cell);
    		} else {
    			System.out.println("Error: No computer move selected");
    		}
    		return true;
    	}
    	return false;
    }
    
    /*********************************************
     * 
     * Game Cell Getters (General)
     * 
     *********************************************/
    public GameCell getGameCell (IntPair coordinates) {
    	GameCell result = null;

        for (GameCell cell : allCells) {
        	if (cell.getIdx().equals (coordinates)) {
        		result = cell;
        	}
            break;
        }

        return result;
    }

    public GameCell getGameCell (int row, int column) {
        return getGameCell(new IntPair (row, column));
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
            break;
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
                break;
            }
            
    	} else {
    		System.out.println("Error: Invalid Game Cell Type");
    	}

		return filteredCells;
    }
    
	//helper for finding a winning move
	//returns null if no winning move exists
	public GameCell getWinningMove(Player p) {
		GameCell result = null;
		for (GameCell cellIterator : emptyCells) {
			if (cellIterator.isEmpty()) {
				cellIterator.playPiece(p);
	        	if (playerHasWon(p)) {
	        		cellIterator.clearPiece();
	        		result = cellIterator;
	        		break;
	        	} else {
	        		cellIterator.clearPiece();
	        	}
			}
		}
		
		return result;

	}
    
    /*********************************************
     * 
     * Check Game Cell state
     * 
     *********************************************/

    public ArrayList<IntPair> matchingNeighbors (IntPair checkIdx) {
    	ArrayList<IntPair> neighbors = new ArrayList<IntPair>();
    	GameCell checkCell = getGameCell (checkIdx);
    	
    	for (int i = checkIdx.getX() - 1; i <= checkIdx.getX() + 1; i++) {
    		for (int j = checkIdx.getY() - 1; j <= checkIdx.getY() + 1; j++) {
    			if (i > 0 && j > 0 && i < 3 && j < 3) {
        			GameCell cell = getGameCell(i, j);
            		if (cell.isPlayedBy(checkCell.getPlayer())) {
            			neighbors.add(new IntPair (i, j));
            		}
    			}
    		}
    	}
    	
    	return neighbors;
    }
    
    
    /*********************************************
     * 
     * Check for this game's status
     * 
     *********************************************/
    public boolean boardIsEmpty() {
    	if (emptyCells.size() == 9) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isDraw () {
    	if (emptyCells.isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasWinner() {
    	if (checkWinner()) {
    		currentPlayer = winCells.get(0).getPlayer();
    		
    		for (GameCell cell : winCells) {
    			cell.setColor (Color.RED);
    		}
    		return true;
    	}
    	return false;
    }

    private boolean checkWinner () {
    	if (playerHasWon (player1)) {
    		return true;
    	}
    	
    	if (playerHasWon (player2)) {
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
    private void setWhoFirst () {
    	Random rand = new Random();
    	int whoFirst = rand.nextInt(2);
    	
    	if (whoFirst == 0) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
    
    private void initiate() {
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
    }
    
}
