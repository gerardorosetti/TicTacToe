package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerViewController implements Initializable {

    @FXML
    private Button toggleServerButton;

    @FXML
    private Text infoText;

    @FXML
    private VBox container;

    private int port = 5900;
    private Server server = new Server(port);
    private Thread listen;
    private boolean isServerRunning = false;

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
                listen = new Thread(() -> server.listen());
                listen.start();
                toggleServerButton.setText("End - Close Server");
                isServerRunning = true;
            }
        });
    }
}