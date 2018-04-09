package tsuruko.TicTacToe.view;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.model.computerPlayer;
import tsuruko.TicTacToe.model.player;
import tsuruko.TicTacToe.model.playerShape;

public class GameBoardController {
    private MainApp mainApp;
    
    private player player1;
    private player player2;
    private player currentPlayer;
    
    Alert gameMessage = new Alert(AlertType.INFORMATION);
    
    @FXML
    GridPane gameBoard;
    
    private boolean useComputerPlayer;
    
    @FXML
    private Label whosTurn;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setComputerPlayer (boolean ai) {
    	useComputerPlayer = ai;
    }
    
    public void newGame() {
    	player1 = new player();
    	player2 = new player("o", "Player 2");

    	if (useComputerPlayer) {
    		player2 = new computerPlayer();
    	}
    	
    	Random rand = new Random();
    	int whoFirst = rand.nextInt(100) + 1;
    	
    	if (whoFirst <= 50) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
        
    	whosTurn.setText(currentPlayer.getPlayerName() + "'s Turn (" + currentPlayer.getShapeUsed() + ")");

    	for (Node n : gameBoard.getChildren()) {
    		Integer colIndex = GridPane.getColumnIndex(n);
    		
    		if (colIndex != null) {
    			StackPane child = (StackPane) n;
    			child.getChildren().clear();
    		}
    	}

    	currentPlayer = player1;
    	whosTurn.setText(currentPlayer.getPlayerName() + "'s Turn (" + currentPlayer.getShapeUsed() + ")");

    	
        if (useComputerPlayer && currentPlayer!= player1) {
        	((computerPlayer) player2).makeMove(gameBoard, player1);
        	currentPlayer = player1;
        }
        
    	gameMessage.setTitle("TicTacToe");
    }
    
    private void toggleCurrentPlayer() {
    	if (currentPlayer == player1) {
    		currentPlayer = player2;
    	} else {
    		currentPlayer = player1;
    	}
    	whosTurn.setText(currentPlayer.getPlayerName() + "'s Turn (" + currentPlayer.getShapeUsed() + ")");
    }

    private Boolean isValidMove (StackPane cell) {
    	if (cell.getChildren().isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    @FXML
    private void gridCellClicked(MouseEvent event) {
        StackPane clickedCell = (StackPane) event.getSource() ;

        gameBoard = (GridPane) clickedCell.getParent();
        
        if (isValidMove(clickedCell)) {
        	if (useComputerPlayer) {
        		if (currentPlayer == player1) {
                	clickedCell.getChildren().add(player1.takeTurn());

                    if (player1.hasWon(gameBoard)) {
                    	showWinMessage();
                        newGame();
                    } else {
                    	toggleCurrentPlayer();
                    	
                    	((computerPlayer) currentPlayer).makeMove(gameBoard, player1);
                        if (currentPlayer.hasWon(gameBoard)) {
                        	showWinMessage();
                            newGame();
                        } else {
                        	toggleCurrentPlayer();
                        }
                    }
        		}
        	} else {
            	clickedCell.getChildren().add(currentPlayer.takeTurn());

                if (currentPlayer.hasWon(gameBoard)) {
                	showWinMessage();
                    newGame();
                } else {
                	toggleCurrentPlayer();
                }
        	}
        } else {
        	playerShape pShape = (playerShape) clickedCell.getChildren().get(0);
        	
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(pShape.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
        }
        
        if (isDraw()) {
            gameMessage.setHeaderText("No Winner!");
            gameMessage.setContentText("It's a draw!");

            gameMessage.showAndWait();
            
            newGame();
        }
    }
    
    private void showWinMessage() {
    	gameMessage.setHeaderText("Congratulations!");
    	if (useComputerPlayer) {
    		if (!currentPlayer.equals(player1)) {
    			gameMessage.setHeaderText("Too Bad!");
    			gameMessage.setContentText("You lost!");
    		} else {
    			gameMessage.setContentText("You win!");
    		}
    	} else {
    		gameMessage.setContentText(currentPlayer.getPlayerName() + " wins!");
    	}

    	gameMessage.showAndWait();
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

}