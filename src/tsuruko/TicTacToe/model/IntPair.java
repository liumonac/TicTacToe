package tsuruko.TicTacToe.model;

public class IntPair {
	private int x;
	private int y;
	
	public IntPair (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean equals (int x, int y) {
		if (this.x == x && this.y == y) {
			return true;
		}
		return false;
	}
}
