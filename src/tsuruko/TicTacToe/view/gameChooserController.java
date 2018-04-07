package tsuruko.TicTacToe.view;

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

    public void handlePlayer1() {
    	mainApp.getGameBoardController().setComputerPlayer(true);
    	dialogStage.close();
    }
    
    public void handlePlayer2() {
    	mainApp.getGameBoardController().setComputerPlayer(false);
    	dialogStage.close();
    }
}
