package tsuruko.TicTacToe.util;

public class PlayerType extends Type {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final PlayerType PLAYER1 = new PlayerType( 0, "X");
    public static final PlayerType PLAYER2 = new PlayerType( 1, "O");

	private PlayerType( int value, String desc ) {
		super( value, desc );
	}
}
