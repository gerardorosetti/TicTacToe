package ve.ula.tictactoe.controllers;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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

    private ScheduledService<Void> receiveRoomsList;
    private final int port = 5900;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CONNECTION SUCCESSFULLY");
        try {
            Socket socket = new Socket("localhost", port);
            connection = new Connection(socket);
            //new Thread(this::updateRoomsList).start();
            receiveRoomsList = new ScheduledService<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            String line = connection.receiveRoomsList();
                            List<String> items = Arrays.stream(line.split("-")).toList();

                            javafx.application.Platform.runLater(() -> {
                                List<String> current = roomsListView.getItems().stream().toList();
                                if (!current.equals(items)) {
                                    roomsListView.getItems().clear();
                                    roomsListView.getItems().addAll(items);
                                }
                            });

                            return null;
                        }
                    };
                }
            };
            receiveRoomsList.setPeriod(Duration.seconds(1));
            receiveRoomsList.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        joinRoomButton.setOnAction(e ->
        {
            try {
                String selectedRoomName = roomsListView.getSelectionModel().getSelectedItem();
                connection.sendMessage(selectedRoomName);
                String message = connection.receiveMessage();
                //String message = "JOINED";
                System.out.println(message);
                //while (message.equals())
                if (message.equals("JOINED")) {
                    System.out.println("JOINING ROOM SUCCESS");
                    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeOnlineView.fxml"));
                    Parent fxmlContent = loader.load();
                    container.getChildren().clear();
                    container.getChildren().add(fxmlContent);
                    TicTacToeOnlineController TTTOC = loader.getController();
                    TTTOC.setConnection(connection);
                } else {
                    System.out.println("JOINING ROOM FAILED");
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        /*
        Platform.runLater(() -> {
            Stage stage = (Stage) container.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                connection.disconnect();
            });
        });*/


        /*
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
        });*/
    }
/*
    private void updateRoomsList() {
        System.out.println("STARTING UPDATING LIST OF ROOMS");
        while (true) {
            roomsListView.getItems().clear();
            String message = connection.receiveRoomsList();
            List<String> items = Arrays.stream(message.split("-")).toList();
            roomsListView.getItems().addAll(items);
            if (!keepUpdatingList) {
                connection.sendRoomsList("stop");
                break;
            } else {
                connection.sendRoomsList("keep");
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}