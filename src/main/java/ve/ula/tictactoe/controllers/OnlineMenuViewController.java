package ve.ula.tictactoe.controllers;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
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
    private Connection connectionRooms;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CONNECTION SUCCESSFULLY");
        try {
            Socket socket = new Socket("localhost", port);
            connectionRooms = new Connection(socket);

            receiveRoomsList = new ScheduledService<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            String line = connectionRooms.receiveRoomsList();
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
            try{
                String selectedRoomName = roomsListView.getSelectionModel().getSelectedItem();
                connectionRooms.sendMessage(selectedRoomName);
                int playersCount = Integer.parseInt(selectedRoomName.split("Current Players: ")[1]);
                System.out.println("Número de jugadores actuales: " + playersCount);
                if (playersCount < 2) {
                    System.out.println("JOINING ROOM SUCCESS");
                    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeOnlineView.fxml"));
                    Parent fxmlContent = loader.load();
                    container.getChildren().clear();
                    container.getChildren().add(fxmlContent);
                    TicTacToeOnlineController TTTOC = loader.getController();
                    TTTOC.setConnection(connectionRooms);
                } else {
                    System.out.println("JOINING ROOM FAILED");
                }
            } catch (IOException exp){
                exp.printStackTrace();
            }

        });
    }
}
