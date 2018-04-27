package tsuruko.TicTacToe.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
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
    
    private Timeline timeline;
    
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
    	currentGame = new TicTacToeGame(gameBoard);
    	
    	timeline = new Timeline(
    		    new KeyFrame(Duration.millis(200), e -> {
    		    	whosTurn.setText(currentGame.getCurrentPlayerTurn());
    		    	checkGameStatus();
    		    })
    		);
    	timeline.setCycleCount(Animation.INDEFINITE);
    	
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
	            
	            if (!currentGame.processHumanMove(clickedCell)) {
	            	//if not successful
	                Alert alert = new Alert(AlertType.ERROR);
	                alert.setTitle("TicTacToe");
	                alert.setHeaderText("Invalid Move");
	                alert.setContentText(cell.getPlayerName() + " already filled that box!");

	                alert.showAndWait();
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
    	currentGame.setSize(gameBoard.getWidth(), gameBoard.getHeight());
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
    	
    	timeline.play();
    }
    
    public void newGame(boolean useAi) {   
    	if (useAi) {
    		toggleAi.setText("AI ON");
    	}
    	
    	currentGame.newGame(useAi);
    	gameMessage.setTitle("TicTacToe");
    	
    	whosTurn.setText(currentGame.getCurrentPlayerTurn());
    	
    	currentGame.setAiType(computerMode);
    	
    	timeline.play();
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
    		timeline.stop();
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