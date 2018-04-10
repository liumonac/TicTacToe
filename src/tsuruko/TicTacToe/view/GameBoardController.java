package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import tsuruko.TicTacToe.model.TicTacToeGame;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.model.GameCell;
import tsuruko.TicTacToe.model.IntPair;

public class GameBoardController {
    
    // Reference to the main application
    private MainApp mainApp;
    
    TicTacToeGame currentGame;
    
    Alert gameMessage = new Alert(AlertType.INFORMATION);
    
    @FXML
    GridPane gameBoard;
    
    @FXML
    private Label whosTurn;
    
    /*********************************************
     * 
     * Initialize Game
     * 
     *********************************************/
    public void initalize(MainApp mainApp) {
    	this.mainApp = mainApp;
        for (int i = 0 ; i < 3 ; i++) {
            for (int j = 0; j < 3; j++) {
                addCell(i, j);
            }
        }
    	currentGame = new TicTacToeGame(gameBoard);
    	newGame();
    }

    
    private void addCell (int rowIndex, int colIndex) {
        GameCell cell = new GameCell(new IntPair (rowIndex, colIndex));
        
        cell.setOnMouseClicked(event -> {
        	if (currentGame.isAnimatingPiece()) {
        		return;  //make players wait for their turn
        	}
        	
        	Node source = (Node) event.getSource();
        	if (source.getClass() == GameCell.class) { 
	            GameCell clickedCell = (GameCell) event.getSource();
	            
	        	if (currentGame.processHumanMove(clickedCell)) {
	        		processWinner();
		        	if (currentGame.processComputerMove()) {
		        		processWinner();
		        	}
	        	}
	        	
        	}
        });
        
        cell.setStyle();
        
        gameBoard.add(cell, colIndex, rowIndex);
    }
    
    public void setSize() {
    	currentGame.setSize();
    }
    
    /*********************************************
     * 
     * Start New Game
     * 
     *********************************************/
    public void newGame() {   
    	currentGame.clearBoard();
    	currentGame.newGame();
    	
    	whosTurn.setText(  currentGame.getCurrentPlayer().getPlayerName() + "'s Turn "
    					 + "(" + currentGame.getCurrentPlayer().getShapeUsed() + ")");
    	
    	gameMessage.setTitle("TicTacToe");
    }
    
    public void newGame(boolean useComputer) {   
    	currentGame.clearBoard();
    	currentGame.setComputerPlayer(useComputer);
    	currentGame.newGame();
    	
    	whosTurn.setText(  currentGame.getCurrentPlayer().getPlayerName() + "'s Turn "
    					 + "(" + currentGame.getCurrentPlayer().getShapeUsed() + ")");
    	
    	gameMessage.setTitle("TicTacToe");
    }

    /*********************************************
     * 
     * Private Helper Functions
     * 
     *********************************************/
    private void processWinner() {
    	if (currentGame.hasWinner()) {
        	gameMessage.setHeaderText("Game Over!");
        	gameMessage.setContentText(currentGame.getWinMesage());
        	gameMessage.showAndWait();
        	
        	mainApp.showGameChooserDialog();
        } else {
        	currentGame.toggleCurrentPlayer();
        	whosTurn.setText(  currentGame.getCurrentPlayer().getPlayerName() + "'s Turn "
					 + "(" + currentGame.getCurrentPlayer().getShapeUsed() + ")");
        }
        
        if (currentGame.isDraw()) {
            gameMessage.setHeaderText("No Winner!");
            gameMessage.setContentText("It's a draw!");
            gameMessage.showAndWait();
            
            mainApp.showGameChooserDialog();
        }
    }

}