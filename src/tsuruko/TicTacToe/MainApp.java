package tsuruko.TicTacToe;

import java.io.IOException;

import tsuruko.TicTacToe.view.GameBoardController;
import tsuruko.TicTacToe.view.RootLayoutController;
import tsuruko.TicTacToe.view.GameOptionsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {
	
    private Stage primaryStage;
    private BorderPane rootLayout;
    private GameBoardController gameController;
    private GameOptionsController optionsController;  
    
	/*********************************************
	 * 
	 * Loaders
	 * 
	 *********************************************/
    public void loadGameBoard() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameBoard.fxml"));
            AnchorPane gameBoard = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(gameBoard);
            
            // Give the controller access to the main app.
            gameController = loader.getController();
            gameController.initalize(this);
            
            primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            	gameController.setSize();
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadRootLayout() {
        try {
            // Load root layout from FXML file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);


            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TicTacToe");

        loadRootLayout();
        
        loadGameBoard();
        
        primaryStage.show();
        
        loadGameOptionsDialog();
    }

	public static void main(String[] args) {
		launch(args);
	}
	
	/*********************************************
	 * 
	 * Show Dialogs
	 * 
	 *********************************************/
	public void loadGameOptionsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/gameOptionsDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Game Mode");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            optionsController = loader.getController();
            optionsController.setDialogStage(dialogStage);
            optionsController.setMainApp(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    public void showGameOptionsDialog() {
            // Show the dialog and wait until the user closes it
    	optionsController.getDialogStage().showAndWait();
    }
	
	/*********************************************
	 * 
	 * Getters
	 * 
	 *********************************************/
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public GameBoardController getGameBoardController() {
    	return gameController;
    }
}
