package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import tsuruko.TicTacToe.model.TicTacToeGame;
import tsuruko.TicTacToe.util.GameOptions;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.model.GameCell;
import tsuruko.TicTacToe.model.IntPair;

public class GameBoardController {

	private TicTacToeGame currentGame;
    
    private Alert gameMessage = new Alert(AlertType.INFORMATION);
    
    GameOptions computerMode = GameOptions.NewellSimon;
    
    @FXML
    private GridPane gameBoard;
    
    @FXML
    private Label whosTurn;
    
    @FXML
    private Button toggleAi;
    
    /*********************************************
     * 
     * Initialize Game
     * 
     *********************************************/
    public void initalize(MainApp mainApp) {
    	gameMessage.setTitle("TicTacToe");
    	gameMessage.setHeaderText("Game Over!");

    	gameMessage.setOnHidden(event -> {        	
        		mainApp.showGameOptionsDialog();
        	});
    	
        for (int i = 0 ; i < 3 ; i++) {
            for (int j = 0; j < 3; j++) {
                addCell(i, j);
            }
        }
    	currentGame = new TicTacToeGame(gameBoard, this);
    	newGame();
    }

    
    private void addCell (int rowIndex, int colIndex) {
        GameCell cell = new GameCell(new IntPair (rowIndex, colIndex));
        
        cell.setOnMouseClicked(mouseEvent -> {
        	if (currentGame.isAnimatingPiece()) {
        		return;  //make players wait for their turn
        	}
        	
        	Node source = (Node) mouseEvent.getSource();
        	if (source.getClass() == GameCell.class) { 
	            GameCell clickedCell = (GameCell) mouseEvent.getSource();
	            
	        	if (currentGame.processHumanMove(clickedCell)) {
	        		checkGameStatus();
	        	}
	        	
        	}
        });
        
        gameBoard.add(cell, colIndex, rowIndex);
    }
    
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
    public void setSize() {
    	currentGame.setSize();
    }
    
    public void setTurn(String turn) {
    	whosTurn.setText(turn);
    	checkGameStatus();
    }
    
    /*********************************************
     * 
     * Start New Game
     * 
     *********************************************/
    public void newGame() {   
    	currentGame.newGame();
    	gameMessage.setTitle("TicTacToe");
    	
    	whosTurn.setText(currentGame.getCurrentPlayerTurn());
    	
    	currentGame.setAiType(computerMode);
    }
    
    public void newGame(boolean useAi ) {   
    	if (useAi) {
    		toggleAi.setText("AI ON");
    	}
    	
    	currentGame.newGame(useAi);
    	gameMessage.setTitle("TicTacToe");
    	
    	whosTurn.setText(currentGame.getCurrentPlayerTurn());
    	
    	currentGame.setAiType(computerMode);
    }

    /*********************************************
     * 
     * Toggle AI Mode
     * 
     *********************************************/
    public void setAiMode(GameOptions aiMode ) {
    	computerMode = aiMode;
    }
    
    /*********************************************
     * 
     * Private Helper Functions
     * 
     *********************************************/
    private void checkGameStatus() {
    	if (currentGame.gameOver()) {
        	gameMessage.setContentText(currentGame.getGameStatus());
        	gameMessage.show();
        }
    }
    
    /*********************************************
     * 
     * Debugging Only
     * 
     *********************************************/
    @FXML
    private void toggleAI() {
    	toggleAi.setText(currentGame.toggleComputerPlayer());
    	checkGameStatus();
    }
    
    public void setDebug(boolean debug ) {
    	if (debug) {
    		toggleAi.setVisible(true);
    		toggleAi.setDisable(false);
    	} else {
    		toggleAi.setVisible(false);
    		toggleAi.setDisable(true);
    	}
    }

}