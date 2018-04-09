package tsuruko.TicTacToe.model;

public class Player {
	public static final String XSHAPE = "X";
	public static final String CIRCLE = "O";

    protected String shapeUsed = "X";
    protected String playerName = "Player 1";

	public Player () {
		shapeUsed = XSHAPE;
	}
	
	public Player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			shapeUsed = CIRCLE;
		} else {
			shapeUsed = XSHAPE;
		}
	}

	public Player (String shapeChosen, String playerName) {
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

	public GameCell playPiece (int cellNum) {
		GameCell newShape = new GameCell(shapeUsed, cellNum);
		newShape.setPlayer(this);

		return newShape;
	}
	
	public boolean equals (Player p) {
		if ( this.playerName.equals(p.getPlayerName())
			 && this.shapeUsed.equals(p.getShapeUsed())
			) {
			return true;
		}
		
		return false;
	}
	
	//helper for finding a winning move
	//returns null if no winning move exists
	protected GameCell getWinningMove(TicTacToeGame myGame) {
		GameCell result = null;
		GameCell cellIterator = null;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cellIterator = myGame.getGameCell(i, j);
    			if (cellIterator.isEmpty()) {
    				cellIterator.playPiece(this);
    	        	if (myGame.playerHasWon(this)) {
    	        		cellIterator.clearPiece();
    	        		result = cellIterator;
    	        		break;
    	        	} else {
    	        		cellIterator.clearPiece();
    	        	}
    			}
			}
		}
		
		return result;

	}
}
