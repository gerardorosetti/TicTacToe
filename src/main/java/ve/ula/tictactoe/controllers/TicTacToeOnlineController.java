package ve.ula.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Connection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TicTacToeOnlineController implements Initializable {

    @FXML
    private Text playerText;
    @FXML
    private Button leaveButton;
    @FXML
    private VBox container;

    private char playerChar;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leaveButton.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("OnlineMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
                connection.disconnect();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });
    }

    private void setTexts() {
        String player = connection.receiveMessage();
        if (player.equals("player1")) {
            playerText.setText("X");
            playerChar = 'X';
        } else {
            playerText.setText("O");
            playerChar = 'O';
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        setTexts();
    }
}