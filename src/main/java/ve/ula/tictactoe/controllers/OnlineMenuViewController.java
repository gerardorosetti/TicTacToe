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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private final String host = "localhost";
    private Socket socket;
    private Lock lock;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lock = new ReentrantLock();
        try {
            receiveRoomsList = new ScheduledService<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            if (lock.tryLock()) {
                                try {
                                    socket = new Socket(host, port);
                                    Connection clientConnection = new Connection(socket);
                                    clientConnection.sendMessage("SEND ROOMS");
                                    String line = clientConnection.receiveMessage();
                                    List<String> items = Arrays.stream(line.split("-")).toList();
                                    javafx.application.Platform.runLater(() -> {
                                        List<String> current = roomsListView.getItems().stream().toList();
                                        if (!current.equals(items)) {
                                            roomsListView.getItems().clear();
                                            roomsListView.getItems().addAll(items);
                                        }
                                    });
                                    clientConnection.disconnect();
                                } finally {
                                    lock.unlock();
                                }
                            }
                            return null;
                        }
                    };

                }
            };
            receiveRoomsList.setPeriod(Duration.millis(1000));
            receiveRoomsList.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        createRoomButton.setOnAction(e -> {
            if (lock.tryLock()) {
                try {
                    socket = new Socket(host, port);
                    Connection clientConnection = new Connection(socket);
                    clientConnection.sendMessage("CREATE");
                    clientConnection.disconnect();
                } catch (Exception exp) {
                    exp.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        returnMenuButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
                receiveRoomsList.cancel();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        joinRoomButton.setOnAction(e -> {
            if (lock.tryLock()) {
                try{
                    socket = new Socket(host, port);
                    Connection clientConnection = new Connection(socket);
                    String selectedRoomName = roomsListView.getSelectionModel().getSelectedItem();
                    clientConnection.sendMessage(selectedRoomName);
                    int playersCount = Integer.parseInt(selectedRoomName.split("Current Players: ")[1]);
                    System.out.println("Número de jugadores actuales: " + playersCount);
                    if (playersCount < 2) {
                        System.out.println("JOINING ROOM SUCCESS");
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeOnlineView.fxml"));
                        Parent fxmlContent = loader.load();
                        container.getChildren().clear();
                        container.getChildren().add(fxmlContent);
                        TicTacToeOnlineController TTTOC = loader.getController();
                        TTTOC.setConnection(clientConnection);
                        receiveRoomsList.cancel();
                    } else {
                        System.out.println("JOINING ROOM FAILED");
                    }
                    //clientConnection.disconnect();
                } catch (IOException exp){
                    exp.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });
    }
}
