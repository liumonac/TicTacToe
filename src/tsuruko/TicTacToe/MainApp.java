package tsuruko.TicTacToe;

import java.io.IOException;

import tsuruko.TicTacToe.model.player;
import tsuruko.TicTacToe.view.GameBoardController;
import tsuruko.TicTacToe.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {
	
    private Stage primaryStage;
    private BorderPane rootLayout;
    private player player1;
    private player player2;
    private int currentPlayer = 1;
    
    public player getCurrentPlayer() {
    	if (currentPlayer == 1) {
    		currentPlayer = 2;
    		return player1;
    	} else {
    		currentPlayer = 1;
    		return player2;
    	}
    }
    
    public void createPlayers() {
    	player1 = new player();
    	player2 = new player("o");
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
            GameBoardController controller = loader.getController();
            controller.setMainApp(this);
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
        
        createPlayers();
    }

	public static void main(String[] args) {
		launch(args);
	}
	
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
