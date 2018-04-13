package tsuruko.TicTacToe.model;

import tsuruko.TicTacToe.util.PlayerType;

public class Player {
    private PlayerType playerType;
    private String playerName = "Player 1";

    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public Player () {
		playerType = PlayerType.PLAYER1;
	}
	
	public Player (String shapeChosen) {
		if (shapeChosen.equals("O") || shapeChosen.equals("o")) {
			playerType = PlayerType.PLAYER2;
			this.playerName = "Player 2";
		} else {
			playerType = PlayerType.PLAYER1;
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
	
	public PlayerType getPlayerType () {
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
			if (playerType == PlayerType.PLAYER1) {
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
