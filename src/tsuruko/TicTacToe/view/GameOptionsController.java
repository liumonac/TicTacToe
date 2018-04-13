package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import tsuruko.TicTacToe.MainApp;

public class GameOptionsController {

    private MainApp mainApp;
    private Stage dialogStage;
    
    private boolean useComputerPlayer = true;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handlePlayer1() {
    	useComputerPlayer = true;
    }
    
    @FXML
    public void handlePlayer2() {
    	useComputerPlayer = false;
    }
    
    @FXML
    private void handleExit() {
    	mainApp.getGameBoardController().newGame(useComputerPlayer);
    	dialogStage.close();
    }
    
}
