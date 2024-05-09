/**
 * ServerViewController class controls the server view of the Tic Tac Toe game.
 */
package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ve.ula.tictactoe.model.Server;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The ServerViewController class implements the Initializable interface and controls the server view.
 */
public class ServerViewController implements Initializable {

    @FXML
    private Button toggleServerButton; // Button to toggle the server on and off.

    @FXML
    private Text infoText; // Text field to display server status information.

    @FXML
    private VBox container; // Container for holding various UI elements.

    private final int port = 5900; // Port for the server connection.
    private final Server server = new Server(port); // Server instance.
    private Thread listen; // Thread for server listening.
    private boolean isServerRunning = false; // Flag to indicate if the server is running.

    /**
     * Initializes the server view with the specified URL and resource bundle.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The ResourceBundle for the root object, or null if there is no ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleServerButton.setOnAction(e -> {
            if (isServerRunning) {
                infoText.setText("Server Stopped");
                server.stopServer();
                listen.interrupt();
                Stage stage = (Stage) toggleServerButton.getScene().getWindow();
                stage.close();
            } else {
                infoText.setText("Server Running...");
                server.createRoom();
                server.createRoom();
                listen = new Thread(server::listen);
                listen.start();

                Stage stage = (Stage) container.getScene().getWindow();
                stage.setOnCloseRequest(event -> {
                    if (listen != null) {
                        server.stopServer();
                        listen.interrupt();
                    }
                });

                toggleServerButton.setText("End - Close Server");
                isServerRunning = true;
            }
        });
    }
}