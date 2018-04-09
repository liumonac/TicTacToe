package tsuruko.TicTacToe.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameCell extends StackPane {
	public static final int CENTER = 0;
	public static final int EDGE = 1;
	public static final int CORNER = 2;
	
	private GameShape myShape;

	private Player myPlayer;
	
	private boolean isEmpty = true;
	private IntPair index;
	private int cellType = -1;
	
    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/

	public GameCell() {
		index = new IntPair (-1, -1);
		this.getChildren().clear();
		isEmpty = true;
		setType();
	}

	public GameCell(IntPair idx) {
		this.index = idx;
		this.getChildren().clear();
		isEmpty = true;
		setType();
	}
	
	public GameCell(IntPair idx, Player player) {
		this.index = idx;
		this.getChildren().clear();
		playPiece (player);
		setType();
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setPlayer (Player p) {
		myPlayer = p;
	}
	
	public void setColor (Color color) {
		myShape.setStrokeColor(color);
	}
	
	private void setType() {
		if (index.equals(0,0) || index.equals(0,2) || index.equals(2,0) || index.equals(2,2)) {
			this.cellType = CORNER;
		}
		
		if (index.equals(0,1) || index.equals(1,0) || index.equals(1,2) || index.equals(2,1)) {
			this.cellType = EDGE;
		}
		
		if (index.equals(1,1)) {
			this.cellType = CENTER;
		}
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public Player getPlayer () {
		return myPlayer;
	}
	
	public IntPair getIdx () {
		return index;
	}
	
	public int getCellType () {
		return cellType;
	}
	
    /*********************************************
     * 
     * Main Functions
     * 
     *********************************************/
	public void playPiece (Player p) {
		if (isEmpty) {
			myPlayer = p;
			if (myPlayer.getShapeUsed() == Player.CIRCLE) {
		        myShape = new Oshape();
			} else {
				myShape = new Xshape(100);
			}
			isEmpty = false;
			this.getChildren().add(myShape);
		}
	}
	
	public void clearPiece() {
		this.getChildren().clear();
		this.isEmpty = true;
		this.myPlayer = null;
	}
	
    /*********************************************
     * 
     * Check State
     * 
     *********************************************/
	public boolean isPlayedBy (Player p) {
		if (myPlayer == null || isEmpty) {
			return false;
		}
		return myPlayer.equals(p);
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
}
