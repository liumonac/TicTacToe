package tsuruko.TicTacToe.util;

public final class GameOptions extends Type {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final GameOptions NewellSimon = new GameOptions(0, "NewellSimon");
    public static final GameOptions MinMax      = new GameOptions(1, "MinMax");
	
	private GameOptions( int value, String desc ) {
		super( value, desc );
	}
}
