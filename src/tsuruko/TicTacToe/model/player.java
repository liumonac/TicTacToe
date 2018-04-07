package tsuruko.TicTacToe.model;

public class player {
	public static final String XSHAPE = "X";
	public static final String CIRCLE = "O";

    private String shapeUsed = "X";
    private String playerName = "Player 1";

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
	
}
