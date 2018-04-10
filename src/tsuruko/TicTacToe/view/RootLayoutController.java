package tsuruko.TicTacToe.view;

import tsuruko.TicTacToe.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    
    public void handleNewGame() {
    	mainApp.showGameChooserDialog();
    }
    
    public void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("TicTacToe");
        alert.setHeaderText("About");
        alert.setContentText("Author: Mona Liu" + "\n" + "Source: https://github.com/liumonac/TicTacToe");

        alert.showAndWait();
    }
}
