package tsuruko.TicTacToe.model;

public class intPair {
	private final int x;
	private final int y;
	
	intPair () {
		this.x = 0;
		this.y = 0;
	}
	
	intPair (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
