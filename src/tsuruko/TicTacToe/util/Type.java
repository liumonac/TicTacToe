package tsuruko.TicTacToe.util;

public class Type implements java.io.Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int value;
    private transient String name;
    
    protected Type( int value, String name )
    {
        this.value = value;
        this.name = name;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String toString()
    {
        return name;
    }
}