package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ve.ula.tictactoe.MainApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuViewController implements Initializable {

    @FXML
    private Button buttonLocal;

    @FXML
    private Button buttonOnline;

    @FXML
    private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));

        buttonLocal.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeGame.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        buttonOnline.setOnAction(e ->
        {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("OnlineMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });
        /*
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
        });*/
    }
}