package tsuruko.TicTacToe.util;

public final class GameStatus extends Type {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final GameStatus NONE = new GameStatus (0, "NewellSimon");
	public static final GameStatus WIN  = new GameStatus (1, "WIN");
	public static final GameStatus DRAW = new GameStatus (2, "DRAW");
	
	private GameStatus( int value, String desc ) {
		super( value, desc );
	}
}
