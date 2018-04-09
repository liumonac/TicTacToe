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
	
	public boolean equals (Player p) {
		if ( this.playerName.equals(p.getPlayerName())
			 && this.shapeUsed.equals(p.getShapeUsed())
			) {
			return true;
		}
		
		return false;
	}
}
