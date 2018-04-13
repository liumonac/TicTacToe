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

	/*********************************************
	 * 
	 * Toggle Options
	 * 
	 *********************************************/
    @FXML
    public void handlePlayer1() {
    	useComputerPlayer = true;
    }
    
    @FXML
    public void handlePlayer2() {
    	useComputerPlayer = false;
    }
    
    @FXML
    public void handleDebugOn() {
    	mainApp.getGameBoardController().setDebug(true);
    }
    
    @FXML
    public void handleDebugOff() {
    	mainApp.getGameBoardController().setDebug(false);
    }
    
	
	/*********************************************
	 * 
	 * Submit
	 * 
	 *********************************************/
    @FXML
    private void handleOk() {
    	mainApp.getGameBoardController().newGame(useComputerPlayer);
    	dialogStage.close();
    }
    
}
