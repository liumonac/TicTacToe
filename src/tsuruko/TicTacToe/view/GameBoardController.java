package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.model.player;
import tsuruko.TicTacToe.model.playerShape;

public class GameBoardController {
    // Reference to the main application
    private MainApp mainApp;
    
    private player player1;
    private player player2;
    private player currentPlayer;

    public void createPlayers() {
    	player1 = new player();
    	player2 = new player("o", "Player 2");
    	currentPlayer = player2;
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

	public playerShape makeMove (int colIndex, int rowIndex) {
    	if (currentPlayer == player1) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    	
		return currentPlayer.makeMove(colIndex, rowIndex);
	}
    
    @FXML
    private void gridClicked(MouseEvent event) {
        StackPane source = (StackPane) event.getSource() ;
        
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        if (source.getChildren().isEmpty()) {
	        source.getChildren().add(makeMove(colIndex, rowIndex));
        } else {
        	playerShape pShape = (playerShape) source.getChildren().get(0);
        	
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Move");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(pShape.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
        }
    }
}