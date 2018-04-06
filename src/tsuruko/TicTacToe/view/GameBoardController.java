package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.model.xShape;

public class GameBoardController {
    // Reference to the main application
    private MainApp mainApp;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void gridClicked(MouseEvent event) {
        StackPane source = (StackPane) event.getSource() ;
        
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());

        if (source.getChildren().isEmpty()) {
	        Circle circle = new Circle();
	        circle.setRadius(50);
	        circle.setFill(Color.WHITESMOKE);
	        circle.setStroke(Color.BLACK);
	        circle.setStrokeWidth(5);

	        xShape x = new xShape (100);

	        if (colIndex.intValue() % 2 == 0) {
		        source.getChildren().add(circle);
	        } else {
		        source.getChildren().add(x);
	        }
	        
        } else {
            System.out.printf("Box is alread filled!%n");

        }
    }
}