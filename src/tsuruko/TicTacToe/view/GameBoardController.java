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
import tsuruko.TicTacToe.model.player;
import tsuruko.TicTacToe.model.playerShape;

public class GameBoardController {
    private MainApp mainApp;
    
    private player player1;
    private player player2;
    private player currentPlayer;
    
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
    }

    @FXML
    private void gridCellClicked(MouseEvent event) {
        StackPane cell = (StackPane) event.getSource() ;

        gameBoard = (GridPane) cell.getParent();
        
        if (cell.getChildren().isEmpty()) {
        	cell.getChildren().add(currentPlayer.takeTurn());

            if (hasWon (currentPlayer)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("TicTacToe");
                alert.setHeaderText("Congratulations!");
            	if (useComputerPlayer) {
            		if (currentPlayer.equals(player2)) {
                        alert.setHeaderText("Too Bad!");
                        alert.setContentText("You lost!");
            		} else {
            			alert.setContentText("You win!");
            		}
            	} else {
                    alert.setContentText(currentPlayer.getPlayerName() + " wins!");
            	}

                alert.showAndWait();
                
                newGame();
            }
            
	        	if (currentPlayer == player1) {
	        		currentPlayer = player2;
	        	} else {
	        		currentPlayer = player1;
	        	}
        	
        	whosTurn.setText(currentPlayer.getPlayerName() + "'s Turn (" + currentPlayer.getShapeUsed() + ")");

            if (useComputerPlayer) {
            	//TODO: computer AI
            }
        } else {
        	playerShape pShape = (playerShape) cell.getChildren().get(0);
        	
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(pShape.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
        }
        
        if (isDraw()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("No Winner!");
            alert.setContentText("It's a draw!");

            alert.showAndWait();
            
            newGame();
        }
        
        
    }
    
    public boolean isDraw () {
    	for (Node n : gameBoard.getChildren()) {
    		Integer colIndex = GridPane.getColumnIndex(n);
    		
    		if (colIndex != null) {
    			StackPane child = (StackPane) n;
    			if (child.getChildren().isEmpty()) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public boolean hasWon (player p) {
    	//check rows
    	int cellsInARow = 0;
    	for (int i = 0; i < 3; i++) {
    		cellsInARow = 0;
    		for (int j = 0; j < 3; j++) {
    			StackPane cell = (StackPane) gameBoard.getChildren().get(i*3+j);
    			
    			if (!cell.getChildren().isEmpty()) {
    				playerShape filledShape = (playerShape) cell.getChildren().get(0);
    				
    				if (filledShape.getPlayer().equals(p)) {
    					cellsInARow += 1;
    				} else {
    					cellsInARow = 0;
    				}
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
    			StackPane cell = (StackPane) gameBoard.getChildren().get(i*3+j);
    			
    			if (!cell.getChildren().isEmpty()) {
    				playerShape filledShape = (playerShape) cell.getChildren().get(0);
    				
    				if (filledShape.getPlayer().equals(p)) {
    					cellsInARow += 1;
    				} else {
    					cellsInARow = 0;
    				}
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
			StackPane cell = (StackPane) gameBoard.getChildren().get(i*3+i);
			
			if (!cell.getChildren().isEmpty()) {
				playerShape filledShape = (playerShape) cell.getChildren().get(0);
				
				if (filledShape.getPlayer().equals(p)) {
					cellsInARow += 1;
				} else {
					cellsInARow = 0;
				}
			}

    		if (cellsInARow == 3) {
    			return true;
    		}
    	}
    	
    	//check right-left diagonal
    	//(0,2)=2, (1,1)=4, (2, 0)=6
		cellsInARow = 0;
    	for (int i = 2; i < 7; i+=2) {
			StackPane cell = (StackPane) gameBoard.getChildren().get(i);
			
			if (!cell.getChildren().isEmpty()) {
				playerShape filledShape = (playerShape) cell.getChildren().get(0);
				
				if (filledShape.getPlayer().equals(p)) {
					cellsInARow += 1;
				} else {
					cellsInARow = 0;
				}
			}

    		if (cellsInARow == 3) {
    			return true;
    		}
    	}
    
        return false;
        	
    }
}