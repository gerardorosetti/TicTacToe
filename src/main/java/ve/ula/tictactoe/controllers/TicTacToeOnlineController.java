package ve.ula.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import ve.ula.tictactoe.model.Connection;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TicTacToeOnlineController implements Initializable {

    @FXML
    private Text player1;
    @FXML
    private Text player2;

    private char playerChar;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void setTexts() {
        String player = connection.receiveMessage();
        if (player.equals("player1")) {
            player1.setText("YOU");
            playerChar = 'X';
        } else {
            player2.setText("RIVAL");
            playerChar = 'O';
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        setTexts();
    }
}