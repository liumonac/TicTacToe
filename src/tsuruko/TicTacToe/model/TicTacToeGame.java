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
    private static final IntPair[] corners = new IntPair[] { new IntPair(0, 0), 
			    								 			 new IntPair(0, 2),
			    											 new IntPair(2, 0),
			    											 new IntPair(2, 2)};
    
    private static final IntPair[] edges = new IntPair[] { new IntPair(0, 1), 
													  	   new IntPair(1, 0),
														   new IntPair(1, 2),
														   new IntPair(2, 1)};
    
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
     * General Game Board Functionality
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
     * Game Cell Getters (General)
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
	 * Game Cell Getters (filled)
	 * 
	 *********************************************/
    public ArrayList<IntPair> getFilledCells () {
    	ArrayList<IntPair> fiiledCells = new ArrayList<>();
    	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				GameCell cell = getGameCell(i, j);
    			if (!cell.isEmpty()) {
    				fiiledCells.add(new IntPair (i, j));
    			}
			}
		}
		
		return fiiledCells;
    }
    
	/*********************************************
	 * 
	 * Game Cell Getters (empty)
	 * 
	 *********************************************/
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
     * Check Game Cell state
     * 
     *********************************************/
    public boolean isEdgeCell (IntPair coordinates) {
		for (int i = 0; i < edges.length; i++) {
			if (edges[i].equals(coordinates)) {
				return true;
			}
		}
		return false;
    }
    
    public boolean isCornerCell (IntPair coordinates) {
		for (int i = 0; i < corners.length; i++) {
			if (corners[i].equals(coordinates)) {
				return true;
			}
		}
		return false;
    }
    
    
    /*********************************************
     * 
     * Check for this game's status
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
    
    public boolean hasWinner() {
    	if (checkWinner()) {
    		currentPlayer = winCells.get(0).getPlayer();
    		return true;
    	}
    	return false;
    }

    private boolean checkWinner () {
    	winCells.clear();
    	
    	for (int i = 0; i < 3; i++) {
    		winCells.clear();
    		for (int j = 0; j < 3; j++) {
    			GameCell cell = getGameCell(i, j);
    			
    			if (cell.isEmpty()) {
    				break;
    			}
				if (winCells.isEmpty() ||
					cell.getPlayer().equals(winCells.get(0).getPlayer())
				) {
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
    			
    			if (cell.isEmpty()) {
    				break;
    			}
				if (winCells.isEmpty() ||
					cell.getPlayer().equals(winCells.get(0).getPlayer())
				) {
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
			
			if (cell.isEmpty()) {
				break;
			}
			if (winCells.isEmpty() ||
				cell.getPlayer().equals(winCells.get(0).getPlayer())
			) {
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
			
			if (cell.isEmpty()) {
				break;
			}
			if (winCells.isEmpty() ||
				cell.getPlayer().equals(winCells.get(0).getPlayer())
			) {
				winCells.add(cell);
			} else {
				break;
			}
			if (winCells.size() == 3) {
				return true;
			}
    	}
    	
		if (winCells.size() == 3) {
			return true;
		}

        return false;	
    }
    
    public boolean playerHasWon (player p) {
    	winCells.clear();
    	//check rows
    	for (int i = 0; i < 3; i++) {
    		winCells.clear();
    		for (int j = 0; j < 3; j++) {
    			GameCell cell = getGameCell(i, j);
    			
    			if (!cell.isEmpty() && cell.isPlayedBy(p)) {
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
    			
    			if (!cell.isEmpty() && cell.isPlayedBy(p)) {
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
    		
    		if (!cell.isEmpty() && cell.isPlayedBy(p)) {
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
    		if (!cell.isEmpty() && cell.isPlayedBy(p)) {
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
    
}
