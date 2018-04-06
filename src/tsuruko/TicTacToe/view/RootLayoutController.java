package tsuruko.TicTacToe.view;

import tsuruko.TicTacToe.MainApp;
import javafx.fxml.FXML;

public class RootLayoutController {
    // Reference to the main application
    private MainApp mainApp;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
