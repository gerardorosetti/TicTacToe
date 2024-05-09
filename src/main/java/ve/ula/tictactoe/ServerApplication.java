package ve.ula.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ServerApplication class represents the main entry point for the server UI application.
 * It extends the JavaFX Application class and overrides the start method to initialize the UI.
 */
public class ServerApplication extends Application {
    /**
     * The main entry point of the application where the primary stage is initialized and displayed.
     * @param stage The primary stage to be initialized.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ServerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("MainMenuView.css").toExternalForm());
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * The main method to launch the application Server.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
