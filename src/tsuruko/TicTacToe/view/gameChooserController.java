package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import tsuruko.TicTacToe.MainApp;

public class gameChooserController {

    private MainApp mainApp;
    private Stage dialogStage;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handlePlayer1() {
    	mainApp.getGameBoardController().newGame(true);
    	dialogStage.close();
    }
    
    @FXML
    public void handlePlayer2() {
    	mainApp.getGameBoardController().newGame(false);
    	dialogStage.close();
    }
    
    @FXML
    private void handleExit() {
    	dialogStage.close();
    }
    
}
