package tsuruko.TicTacToe.model;

import java.util.ArrayList;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import tsuruko.TicTacToe.util.*;

/*********************************************
 * 
 * Represents one of the squares on the game board
 * 
 *********************************************/
public class GameCell extends StackPane {
	private GameShape myShape;
	private Player myPlayer;
	
	private boolean isEmpty = true;
	private IntPair index;
	private CellType cellType;
	
	ArrayList<IntPair> neighbors = new ArrayList<IntPair>();

	//cell size control
	private double cellWidth = 100;
	private double cellHeight = 100;
	
    /*********************************************
     * 
     * Constructors
     * 
     *********************************************/
	public GameCell() {
		this.setMinSize(0, 0);
		index = new IntPair (-1, -1);
		this.getChildren().clear();
		isEmpty = true;
		setType();
		setNeighbors();
	}

	public GameCell(IntPair idx) {
		this.setMinSize(0, 0);
		this.index = idx;
		this.getChildren().clear();
		isEmpty = true;
		setType();
		setNeighbors();
		setStyle();
	}
	
    /*********************************************
     * 
     * Setters
     * 
     *********************************************/
	public void setSize (double width, double height) {
		cellWidth = width;
		cellHeight = height;
		
		if (myShape != null) {
			myShape.setSize(width, height);
		}
	}
	
	public void setPlayer (Player p) {
		myPlayer = p;
	}
	
	public void setColor (Color color) {
		myShape.setStrokeColor(color);
	}
	
    /*********************************************
     * 
     * Getters
     * 
     *********************************************/
	public String getPlayerName () {
		if (myPlayer != null) {
			return myPlayer.getPlayerName();
		}
		return null;
	}
	
	public IntPair getIdx () {
		return index;
	}
	
	public CellType getCellType () {
		return cellType;
	}
	
	public GameShape getMyShape () {
		return myShape;
	}
	
	public ArrayList<IntPair> getNeighbors () {
		return neighbors;
	}
	
	//get the neighbor opposite of the one specified
	public IntPair getOtherNeighbor (IntPair p) {
		if (neighbors.get(0).equals(p)) {
			return neighbors.get(1);
		}
		return neighbors.get(0);
	}
	
	public boolean isAnimation () {
		if (myShape != null) {
			return myShape.isAnimation();
		}
		return false;
	}
	
	
	//get idx across the board from this one
    public IntPair getOppositeIdx () {
    	int x = getOppositeIdx (index.getX());
    	int y = getOppositeIdx (index.getY());
    	
    	return new IntPair (x, y);
    }
	
    /*********************************************
     * 
     * Main Functions
     * 
     *********************************************/
	public void playPiece (Player p) {
		if (isEmpty) {
			myPlayer = p;
			if (myPlayer.getPlayerType() == PlayerType.PLAYER2) {
		        myShape = new Oshape(cellWidth, cellHeight);
			} else {
				myShape = new Xshape(cellWidth, cellHeight);
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
	
    /*********************************************
     * 
     * Private Helper Functions
     * 
     *********************************************/
	private void setType() {
		if (index.equals(0,0) || index.equals(0,2) || index.equals(2,0) || index.equals(2,2)) {
			cellType = CellType.CORNER;
		}
		
		if (index.equals(0,1) || index.equals(1,0) || index.equals(1,2) || index.equals(2,1)) {
			cellType = CellType.EDGE;
		}
		
		if (index.equals(1,1)) {
			cellType = CellType.CENTER;
		}
	}
	
	private void setNeighbors() {
		if (cellType == CellType.EDGE) {
			if (index.getX() == 1) {
				neighbors.add(new IntPair (0, index.getY()));
				neighbors.add(new IntPair (2, index.getY()));
			} else {
				neighbors.add(new IntPair (index.getX(), 0));
				neighbors.add(new IntPair (index.getX(), 2));
			}
		}
		
		if (cellType == CellType.CORNER) {
			if (index.getX() == 0) {
				neighbors.add(new IntPair (0, 1));
			} else {
				neighbors.add(new IntPair (2, 1));
			}
			
			if (index.getY() == 0) {
				neighbors.add(new IntPair (1, 0));
			} else {
				neighbors.add(new IntPair (1, 2));
			}
		}
	}
	
    
    private int getOppositeIdx (int idx) {
    	int result = -1;
    	
    	switch (idx) {
    		case 0:
    			result = 2;
    			break;
    		case 1:
    			result = 1;
    			break;
    		case 2:
    			result = 0;
    			break;
    	}
    	
    	return result;
    }
    
    private void setStyle() {
    	int rowIndex = index.getX();
    	int colIndex = index.getY();
    	
        this.getStyleClass().add("game-grid-cell");

        //sets which edges to put borders on
        if (rowIndex == 0 && colIndex == 0) {
        	this.getStyleClass().add("left-top-corner");
        }
        
        if (rowIndex == 1 && colIndex == 0) {
        	this.getStyleClass().add("left-edge");
        }
        
        if (rowIndex == 2 && colIndex == 0) {
        	this.getStyleClass().add("left-bottom-corner");
        }
        
        if (rowIndex == 0 && colIndex == 1) {
        	this.getStyleClass().add("top-edge");
        }
        
        if (rowIndex == 1 && colIndex == 1) {
        	this.getStyleClass().add("center");
        }
        
        if (rowIndex == 2 && colIndex == 1) {
        	this.getStyleClass().add("bottom-edge");
        }
        
        if (rowIndex == 0 && colIndex == 2) {
        	this.getStyleClass().add("right-top-corner");
        }
        
        if (rowIndex == 1 && colIndex == 2) {
        	this.getStyleClass().add("right-edge");
        }
        
        if (rowIndex == 2 && colIndex == 2) {
        	this.getStyleClass().add("right-bottom-corner");
        }
    }
    

}
