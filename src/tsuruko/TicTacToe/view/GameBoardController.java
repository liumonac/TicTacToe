package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
    
    public void newGame() {
    	player1 = new player();
    	player2 = new player("o", "Player 2");
    	currentPlayer = player2;
    }
    
    public void newGame(GridPane board) {
    	player1 = new player();
    	player2 = new player("o", "Player 2");
    	currentPlayer = player2;
    	
    	for (Node n : board.getChildren()) {
    		Integer colIndex = GridPane.getColumnIndex(n);
    		
    		if (colIndex != null) {
    			StackPane child = (StackPane) n;
    			child.getChildren().clear();
    		}
    	}
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
    private void gridCellClicked(MouseEvent event) {
        StackPane source = (StackPane) event.getSource() ;
        
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        GridPane gameBoard = (GridPane) source.getParent();
        
        if (source.getChildren().isEmpty()) {
	        source.getChildren().add(makeMove(colIndex, rowIndex));
	        
            if (hasWon (gameBoard, currentPlayer)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("We have a winner!");
                alert.setHeaderText("Congratulations!");
                alert.setContentText(currentPlayer.getPlayerName() + " has won!");

                alert.showAndWait();
                
                newGame(gameBoard);
            }
        } else {
        	playerShape pShape = (playerShape) source.getChildren().get(0);
        	
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Move");
            alert.setHeaderText("Invalid Move");
            alert.setContentText(pShape.getPlayer().getPlayerName() + " already filled that box!");

            alert.showAndWait();
        }
        
        if (isDraw(gameBoard)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("It's a draw!");
            alert.setHeaderText("No Winner!");
            alert.setContentText("It's a draw!");

            alert.showAndWait();
            
            newGame(gameBoard);
        }
    }
    
    public boolean isDraw (GridPane gameBoard) {
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
    
    public boolean hasWon (GridPane gameBoard, player p) {
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