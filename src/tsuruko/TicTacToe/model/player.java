package tsuruko.TicTacToe.model;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class player {
	public static final String XSHAPE = "X";
	public static final String CIRCLE = "O";

    protected String shapeUsed = "X";
    protected String playerName = "Player 1";

	public player () {
		shapeUsed = XSHAPE;
	}
	
	public player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;
		} else {
			shapeUsed = XSHAPE;
		}
	}

	public player (String shapeChosen, String playerName) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;
		} else {
			shapeUsed = XSHAPE;
		}
		this.playerName = playerName;
	}
	
	public String getPlayerName () {
		return playerName;
	}
	
	public String getShapeUsed () {
		return shapeUsed;
	}

	public playerShape takeTurn () {
		playerShape newShape = new playerShape(shapeUsed);
		newShape.setPlayer(this);

		return newShape;
	}
	
	public boolean equals (player p) {
		if ( this.playerName.equals(p.getPlayerName())
			 && this.shapeUsed.equals(p.getShapeUsed())
			) {
			return true;
		}
		
		return false;
	}

    public boolean hasWon (GridPane gameBoard) {
    	//check rows
    	int cellsInARow = 0;
    	for (int i = 0; i < 3; i++) {
    		cellsInARow = 0;
    		for (int j = 0; j < 3; j++) {
    			StackPane cell = (StackPane) gameBoard.getChildren().get(i*3+j);
    			
    			if (!cell.getChildren().isEmpty()) {
    				playerShape filledShape = (playerShape) cell.getChildren().get(0);
    				
    				if (filledShape.getPlayer().equals(this)) {
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
    				
    				if (filledShape.getPlayer().equals(this)) {
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
				
				if (filledShape.getPlayer().equals(this)) {
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
				
				if (filledShape.getPlayer().equals(this)) {
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
