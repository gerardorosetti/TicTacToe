/**
 * The MainMenuViewController class controls the main menu view of the Tic Tac Toe game.
 */
package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ve.ula.tictactoe.MainApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The MainMenuViewController class implements the Initializable interface and controls the main menu view.
 */
public class MainMenuViewController implements Initializable {

    @FXML
    private Button buttonLocal;

    @FXML
    private Button buttonOnline;

    @FXML
    private VBox container;

    /**
     * Initializes the main menu view with the specified URL and resource bundle.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The ResourceBundle for the root object, or null if there is no ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }
}