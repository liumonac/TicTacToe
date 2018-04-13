package tsuruko.TicTacToe.util;

public final class CellType extends Type {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final CellType CENTER = new CellType(0, "Center");
    public static final CellType EDGE   = new CellType(1, "Edge");
    public static final CellType CORNER = new CellType(2, "Corner");

	
	private CellType( int value, String desc ) {
		super( value, desc );
	}
}
