package tsuruko.TicTacToe.model;

public class Player {
	public static final String PLAYER1 = "X";
	public static final String PLAYER2 = "O";

    private String playerType = "X";
    private String playerName = "Player 1";

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public Player () {
		playerType = PLAYER1;
	}
	
	public Player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			playerType = PLAYER2;
			this.playerName = "Player 2";
		} else {
			playerType = PLAYER1;
			this.playerName = "Player 1";
		}
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public String getPlayerName () {
		return playerName;
	}
	
	public String getPlayerType () {
		return playerType;
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setPlayerName (boolean isComputer) {
		if (isComputer) {
			playerName = "Computer";
		} else {
			if (playerType == PLAYER1) {
				playerName = "Player 1";
			} else {
				playerName = "Player 2";
			}
		}

	}
	
    /*********************************************
     * 
     * Check State
     * 
     *********************************************/
	public boolean equals (Player p) {
		if (playerType.equals(p.getPlayerType())) {
			return true;
		}
		
		return false;
	}
}
