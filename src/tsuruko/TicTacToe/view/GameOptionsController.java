package tsuruko.TicTacToe.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import tsuruko.TicTacToe.MainApp;
import tsuruko.TicTacToe.util.GameOptions;

public class GameOptionsController {

    private MainApp mainApp;
    private Stage dialogStage;
    
    private boolean useAi = true;
    
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
    public void handle1Player() {
    	useAi = true;
    }
    
    @FXML
    public void handle2Players() {
    	useAi = false;
    }
    
    @FXML
    public void handleDebugOn() {
    	mainApp.getGameBoardController().setDebug(true);
    }
    
    @FXML
    public void handleDebugOff() {
    	mainApp.getGameBoardController().setDebug(false);
    }
    
    @FXML
    public void handleAIModeNS() {
    	mainApp.getGameBoardController().setAiMode(GameOptions.NewellSimon);
    }
    
    @FXML
    public void handleAIModeMinMax() {
    	mainApp.getGameBoardController().setAiMode(GameOptions.MinMax);
    }
    
	
	/*********************************************
	 * 
	 * Submit
	 * 
	 *********************************************/
    @FXML
    private void handleOk() {
    	mainApp.getGameBoardController().newGame(useAi);
    	dialogStage.close();
    }
    
	/*********************************************
	 * 
	 * Getters
	 * 
	 *********************************************/
    public Stage getDialogStage() {
    	return dialogStage;
    }
    
}
