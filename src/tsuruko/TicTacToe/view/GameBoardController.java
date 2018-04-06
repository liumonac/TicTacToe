package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameBoardController {
    @FXML
    private void gridClicked(MouseEvent event) {
        Node source = (Node)event.getSource() ;
        
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        
        if (colIndex == null && rowIndex == null) {
        	 System.out.printf("ALL INDEX NULL%n");
        } else if (colIndex == null) {
        	System.out.printf("Mouse entered cell [???, %d]%n", rowIndex.intValue());
        } else if (rowIndex == null) {
        	System.out.printf("Mouse entered cell [%d, ???]%n", colIndex.intValue());
        }
        else {
            System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
        }
    }
}