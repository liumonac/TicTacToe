package tsuruko.TicTacToe.model;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class TicTacToeGame {
	private GridPane gameBoard;
	
    private player player1;
    private player player2;
    private player currentPlayer;
    
    private boolean useComputerPlayer;
    
    
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
	
	public void newGame() {
		setWhoFirst();
		
        if (useComputerPlayer && currentPlayer!= player1) {
        	((computerPlayer) player2).makeMove(gameBoard, player1);
        	currentPlayer = player1;
        }
	}
	
	public String getWinText() {
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
	
	public void clearBoard() {
    	for (Node n : gameBoard.getChildren()) {
    		if (n.getClass() == StackPane.class ) {
    			StackPane child = (StackPane) n;
    			child.getChildren().clear();
    		}
    	}
	}
	
    public void toggleCurrentPlayer() {
    	if (currentPlayer == player1) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
    
    public void processHumanMove (StackPane cell) {
    	if (isValidMove(cell) && currentPlayer == player1) {
    		cell.getChildren().add(currentPlayer.playPiece());
    	} else {
        	gamePiece piece = (gamePiece) cell.getChildren().get(0);
        	
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(piece.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
    	}
    }
    
    public void processComputerMove () {
    	if (useComputerPlayer && currentPlayer != player1) {
    		((computerPlayer) currentPlayer).makeMove(gameBoard, player1);
    	}
    	
    }
    
    public boolean hasWinner () {
    	//make independent of player
    	if (player1.hasWon(gameBoard)) {
    		currentPlayer = player1;
    		return true;
    	}
    	
    	if (player2.hasWon(gameBoard)) {
    		currentPlayer = player2;
    		return true;
    	}

    	return false;
    }
    
    public boolean isDraw () {
    	for (Node cell : gameBoard.getChildren()) {
    		if (cell.getClass() == StackPane.class) {
    			StackPane child = (StackPane) cell;
    			if (child.getChildren().isEmpty()) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    //private helper functions
    private void setWhoFirst () {
    	Random rand = new Random();
    	int whoFirst = rand.nextInt(2);
    	
    	if (whoFirst == 0) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    }
    
    private Boolean isValidMove (StackPane cell) {
    	if (cell.getChildren().isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
}
