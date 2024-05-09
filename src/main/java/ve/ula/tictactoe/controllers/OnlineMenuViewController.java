/**
 * OnlineMenuViewController class controls the online menu view of the Tic Tac Toe game.
 */
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The OnlineMenuViewController class controls the online menu view of the Tic Tac Toe game.
 */
public class OnlineMenuViewController implements Initializable {

    @FXML
    private Button createRoomButton; // Button to create a new game room.

    @FXML
    private Button joinRoomButton; // Button to join a selected game room.

    @FXML
    private Button returnMenuButton; // Button to return to the main menu.

    @FXML
    private VBox container; // Container for holding various UI elements.

    @FXML
    ListView<String> roomsListView; // ListView to display available game rooms.

    private ScheduledService<Void> receiveRoomsList; // Scheduled service to periodically update the list of game rooms.
    private final int port = 5900; // Port for the server connection.
    private final String host = "localhost"; // Host address for the server.
    private Socket socket; // Socket for server communication.
    private Lock lock; // Lock for thread synchronization.

    /**
     * Initializes the OnlineMenuViewController with the specified URL and resource bundle.
     *
     * @param url The URL to initialize.
     * @param resourceBundle The Resource Bundle to use for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lock = new ReentrantLock(); // Initialize the lock for thread safety.

        // Setting up the scheduled service to update the list of game rooms periodically.
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
                                    List<String> items = Arrays.asList(line.split("-"));
                                    javafx.application.Platform.runLater(() -> {
                                        List<String> current = roomsListView.getItems();
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

        // Event handler for creating a game room.
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

        // Event handler for returning to the main menu.
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

        // Event handler for joining a selected game room.
        joinRoomButton.setOnAction(e -> {
            if (lock.tryLock()) {
                try{
                    socket = new Socket(host, port);
                    Connection clientConnection = new Connection(socket);
                    String selectedRoomName = roomsListView.getSelectionModel().getSelectedItem();
                    clientConnection.sendMessage(selectedRoomName);
                    int playersCount = Integer.parseInt(selectedRoomName.split("Current Players: ")[1]);
                    if (playersCount < 2) {
                        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("TicTacToeOnlineView.fxml"));
                        Parent fxmlContent = loader.load();
                        container.getChildren().clear();
                        container.getChildren().add(fxmlContent);
                        TicTacToeOnlineController TTTOC = loader.getController();
                        TTTOC.setConnection(clientConnection);
                        receiveRoomsList.cancel();
                    }
                } catch (IOException exp){
                    exp.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });
    }
}