package tsuruko.TicTacToe.model;

public class IntPair {
	private int x;
	private int y;
	
    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public IntPair (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
    /*********************************************
     * 
     * Check State
     * 
     *********************************************/
	public boolean equals (int x, int y) {
		if (this.x == x && this.y == y) {
			return true;
		}
		return false;
	}
	
	public boolean equals (IntPair p) {
		if (this.x == p.getX() && this.y == p.getY()) {
			return true;
		}
		return false;
	}
}
