package tsuruko.TicTacToe;

import java.io.IOException;

import tsuruko.TicTacToe.view.GameBoardController;
import tsuruko.TicTacToe.view.RootLayoutController;
import tsuruko.TicTacToe.view.gameChooserController;
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
    
    public GameBoardController getGameBoardController() {
    	return gameController;
    }
    
    public void showGameBoard() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameBoard.fxml"));
            AnchorPane gameBoard = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(gameBoard);
            
            // Give the controller access to the main app.
            gameController = loader.getController();
            gameController.setMainApp(this);
            gameController.setGameBoard(gameBoard.getChildren().get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showgameChooserDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/gameChooserDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose Game Mode");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            gameChooserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void initRootLayout() {
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

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TicTacToe");

        initRootLayout();
        
        showGameBoard();
        
        showgameChooserDialog();
    }

	public static void main(String[] args) {
		launch(args);
	}
	
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
