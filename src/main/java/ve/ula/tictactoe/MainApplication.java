/**
 * The MainApplication class serves as the entry point for the Tic Tac Toe application.
 */
package ve.ula.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The MainApplication class extends the Application class and represents the main application that launches the Tic Tac Toe game.
 */
public class MainApplication extends Application {

    /**
     * The start method initializes the application's primary stage and sets up the main menu view for the Tic Tac Toe game.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an input or output exception occurs during the initialization process.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainMenuView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("MainMenuView.css").toExternalForm());
        stage.setTitle("TicTacToe!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method, the entry point of the application, launches the JavaFX application.
     *
     * @param args The arguments passed to the application, if any.
     */
    public static void main(String[] args) {
        launch();
    }
}