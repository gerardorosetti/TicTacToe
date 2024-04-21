package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ve.ula.tictactoe.MainApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineMenuViewController implements Initializable {

    @FXML
    private Button createRoomButton;

    @FXML
    private Button joinRoomButton;

    @FXML
    private Button returnMenuButton;

    @FXML
    private VBox container;

    @FXML
    ListView<String> roomsListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        createRoomButton.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeLocalView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        returnMenuButton.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
                //container.getScene().getStylesheets().add(MainApplication.class.getResource("MainMenuView.css").toExternalForm());
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        joinRoomButton.setOnAction(e ->
        {
            System.out.println("Join");
        });
        /*
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
        });*/
    }
}