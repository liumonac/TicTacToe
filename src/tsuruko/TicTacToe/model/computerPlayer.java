package tsuruko.TicTacToe.model;

public class computerPlayer extends player {

	public computerPlayer () {
		this.shapeUsed = CIRCLE;
	}
	
	public computerPlayer (String shapeChosen) {
		super(shapeChosen);
	}

	public computerPlayer (String shapeChosen, String playerName) {
		super(shapeChosen, playerName);
	}

	public void determinNextMove () {
		
	}
}
