package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import tsuruko.TicTacToe.model.TicTacToeGame;

public class GameBoardController {
    
    TicTacToeGame currentGame;
    
    Alert gameMessage = new Alert(AlertType.INFORMATION);
    
    @FXML
    GridPane gameBoard;
    
    @FXML
    private Label whosTurn;
    
    public void initalize() {

    	currentGame = new TicTacToeGame(gameBoard);
    	newGame();
    }


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
    
    @FXML
    private void gridCellClicked(MouseEvent event) {
        StackPane clickedCell = (StackPane) event.getSource() ;
    
    	currentGame.processHumanMove(clickedCell);
    	processWinner();
    	
    	currentGame.processComputerMove();
    	processWinner();
    }
    
    @FXML
    private void testClick( MouseEvent event) {
        Node source = (Node)event.getSource() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }
    
    private void processWinner() {
    	if (currentGame.hasWinner()) {
        	gameMessage.setHeaderText("Game Over!");
        	
        	gameMessage.setContentText(currentGame.getWinText());

        	gameMessage.showAndWait();
            newGame();
        } else {
        	currentGame.toggleCurrentPlayer();
        }
        
        if (currentGame.isDraw()) {
            gameMessage.setHeaderText("No Winner!");
            gameMessage.setContentText("It's a draw!");

            gameMessage.showAndWait();
            
            newGame();
        }
    }

}