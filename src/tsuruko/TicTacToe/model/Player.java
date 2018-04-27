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
	public Player (PlayerType type) {
		playerType = type;
		
		if (playerType == PlayerType.PLAYER1) {
			this.playerName = "Player 1";
		} else {
			this.playerName = "Player 2";
		}
	}
	
	public Player () {
		this(PlayerType.PLAYER1);
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public String getName () {
		return playerName;
	}
	
	public String getShape () {
		return playerType.toString();
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
		if (playerType == p.getPlayerType()) {
			return true;
		}
		
		return false;
	}
}
