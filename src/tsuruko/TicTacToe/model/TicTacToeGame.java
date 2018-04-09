package tsuruko.TicTacToe.model;

import java.util.ArrayList;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class TicTacToeGame {
	private GridPane gameBoard;
	
    private player player1;
    private player player2;
    private player currentPlayer;
    
    private boolean useComputerPlayer;
    
	//Grid Indexes
	// 0,0 | 0,1 | 0,2        0 | 1 | 2
	// 1,0 | 1,1 | 1,2		  3 | 4 | 5
	// 2,0 | 2,1 | 2,2	      6 | 7 | 8
    private IntPair[] corners = new IntPair[] { new IntPair(0, 0), 
    											new IntPair(0, 2),
    											new IntPair(2, 0),
    											new IntPair(2, 2)};
    
    private IntPair[] edges = new IntPair[] { new IntPair(0, 1), 
										  	  new IntPair(1, 0),
											  new IntPair(1, 2),
											  new IntPair(2, 1)};

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public TicTacToeGame() {
		gameBoard = new GridPane();

    	player1 = new player();
    	player2 = new player("o", "Player 2");
	}
	
	public TicTacToeGame(GridPane g) {
		gameBoard = g;
		
    	player1 = new player();
    	player2 = new player("o", "Player 2");
	}
	
	public TicTacToeGame(GridPane g, boolean useComputer) {
		gameBoard = g;
		useComputerPlayer = useComputer;

    	player1 = new player();
    	player2 = new player("o", "Player 2");
    	
    	if (useComputerPlayer) {
    		player2 = new computerPlayer();
    	}
	}
	
    /*********************************************
     * 
     * General Game Functionality
     * 
     *********************************************/
    public void setComputerPlayer (boolean ai) {
    	useComputerPlayer = ai;
    	if (useComputerPlayer) {
    		player2 = new computerPlayer();
    	} else {
    		player2 = new player("o", "Player 2");
    	}
    }
	
	public player getCurrentPlayer() {
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
        	GameCell cell = ((computerPlayer) player2).chooseMove(this, player1);
        	cell.playPiece(currentPlayer);
        	currentPlayer = player1;
        }
	}
	
	public void clearBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GameCell cellPicked = getGameCell(i, j);
    			if (!cellPicked.isEmpty()) {
    				cellPicked.clearPiece();
    			}
			}
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
    		GameCell cell = ((computerPlayer) currentPlayer).chooseMove(this, player1);
    		cell.playPiece(currentPlayer);
    		return true;
    	}
    	return false;
    }
    
    /*********************************************
     * 
     * Game Cell Getters
     * 
     *********************************************/
    public GameCell getGameCell (IntPair coordinates) {
    	return getGameCell(coordinates.getX(), coordinates.getY());
    }

    public GameCell getGameCell (int row, int column) {
    	GameCell result = null;
    	
        ObservableList<Node> childrens = gameBoard.getChildren();

        for (Node node : childrens) {
            if (	   node.getClass() == GameCell.class
            		&& GridPane.getRowIndex(node) == row 
            		&& GridPane.getColumnIndex(node) == column) {
                result = (GameCell) node;
                break;
            }
        }

        return result;
    }
    
    public IntPair getOppositeCell (IntPair coordinates) {
    	IntPair result = null;
    	
    	//edges
    	if (coordinates.equals(0, 1)) {
    		result = new IntPair (2, 1);
    	}
    	
    	if (coordinates.equals(2, 1)) {
    		result = new IntPair (0, 1);
    	}
    	
    	if (coordinates.equals(1, 0)) {
    		result = new IntPair (1, 2);
    	}
    	
    	if (coordinates.equals(1, 2)) {
    		result = new IntPair (1, 0);
    	}

    	//corners
    	if (coordinates.equals(0, 0)) {
    		result = new IntPair (2, 2);
    	}
    	
    	if (coordinates.equals(2, 0)) {
    		result = new IntPair (0, 2);
    	}
    	
    	if (coordinates.equals(0, 2)) {
    		result = new IntPair (2, 0);
    	}
    	
    	if (coordinates.equals(2, 2)) {
    		result = new IntPair (0, 0);
    	}
    	
    	return result;
    }
    
/*********************************************
 * 
 * Check for Empty Cells in Game Board grid
 * 
 *********************************************/
    public boolean boardIsEmpty() {
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

    public ArrayList<IntPair> getEmptyCells () {
    	ArrayList<IntPair> emptyCells = new ArrayList<>();
    	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GameCell cell = getGameCell(i, j);
    			if (cell.isEmpty()) {
    				emptyCells.add(new IntPair (i, j));
    			}
			}
		}
		
		return emptyCells;
    }
    
    public ArrayList<IntPair> getEmptyCells (ArrayList<IntPair> cellList) {
    	ArrayList<IntPair> filterList = new ArrayList<>();
    	
    	for (IntPair coordinates : cellList) {
    		GameCell cell = getGameCell (0,0);
    		if (cell.isEmpty()) {
    			filterList.add(coordinates);
    		}
    	}
    	return filterList;
    }
    
    public ArrayList<IntPair> getEmptyCorners () {
    	ArrayList<IntPair> emptyCorners = new ArrayList<>();
    	
		for (int i = 0; i < corners.length; i++) {
			GameCell cell = getGameCell(corners[i]);
			if (cell.isEmpty()) {
				emptyCorners.add(corners[i]);
			}
		}
		
		return emptyCorners;
    }
    
    public ArrayList<IntPair> getEmptyEdges () {
    	ArrayList<IntPair> emptyEdges = new ArrayList<>();
    	
		for (int i = 0; i < edges.length; i++) {
			GameCell cell = getGameCell(edges[i]);
			if (cell.isEmpty()) {
				emptyEdges.add(edges[i]);
			}
		}
		
		return emptyEdges;
    }
    
    /*********************************************
     * 
     * Check for this game's winner status
     * 
     *********************************************/
    public boolean isDraw () {
    	for (Node n : gameBoard.getChildren()) {
    		if (n.getClass() == GameCell.class) {
    			GameCell cell = (GameCell) n;
    			if (cell.isEmpty()) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public boolean hasWinner () {
    	//make independent of player
    	if (playerHasWon(player1)) {
    		currentPlayer = player1;
    		return true;
    	}
    	
    	if (playerHasWon(player2)) {
    		currentPlayer = player2;
    		return true;
    	}

    	return false;
    }
    
    public boolean playerHasWon (player p) {
    	//check rows
    	int cellsInARow = 0;
    	for (int i = 0; i < 3; i++) {
    		cellsInARow = 0;
    		for (int j = 0; j < 3; j++) {
    			GameCell cell = getGameCell(i, j);
    			
    			if (!cell.isEmpty() && cell.isPlayedBy(p)) {
    				cellsInARow += 1;
    			} else {
    				cellsInARow = 0;
    			}
    		}
    		if (cellsInARow == 3) {
    			return true;
    		}
    	}

    	//check columns
    	for (int j = 0; j < 3; j++) {
    		cellsInARow = 0;
    		for (int i = 0; i < 3; i++) {
    			GameCell cell = getGameCell(i, j);
    			
    			if (!cell.isEmpty() && cell.isPlayedBy(p)) {
    				cellsInARow += 1;
    			} else {
    				cellsInARow = 0;
    			}
    		}
    		if (cellsInARow == 3) {
    			return true;
    		}
    	}

    	//check left-right diagonal
    	//(0,0), (1,1), (2,2)
		cellsInARow = 0;
    	for (int i = 0; i < 3; i++) {
    		GameCell cell = getGameCell(i, i);
    		
    		if (!cell.isEmpty() && cell.isPlayedBy(p)) {
    			cellsInARow += 1;
    		} else {
    			cellsInARow = 0;
    		}

    		if (cellsInARow == 3) {
    			return true;
    		}
    	}
    	
    	//check right-left diagonal
		cellsInARow = 0;
		GameCell cell = getGameCell(0, 2);
		if (!cell.isEmpty() && cell.isPlayedBy(p)) {
			cellsInARow += 1;
		} else {
			cellsInARow = 0;
		}
		cell = getGameCell(1, 1);
		if (!cell.isEmpty() && cell.isPlayedBy(p)) {
			cellsInARow += 1;
		} else {
			cellsInARow = 0;
		}
		cell = getGameCell(2, 0);
		if (!cell.isEmpty() && cell.getPlayer().equals(p)) {
			cellsInARow += 1;
		} else {
			cellsInARow = 0;
		}
		if (cellsInARow == 3) {
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
    
}
