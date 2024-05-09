/**
 * TicTacToeOnlineController class manages the online multiplayer gameplay functionality of the Tic Tac Toe game.
 */
package ve.ula.tictactoe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Board;
import ve.ula.tictactoe.model.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The TicTacToeOnlineController class implements the Initializable interface and controls online multiplayer gameplay.
 */
public class TicTacToeOnlineController implements Initializable {
    @FXML
    private Text playerText;
    @FXML
    private Button leaveButton;
    @FXML
    private VBox container;
    @FXML
    public Text winnerText;
    @FXML
    private Canvas myCanvas;

    private double cellWidth;
    private double cellHeight;
    private double imageWidth;
    private double imageHeight;
    private double imageXOffset;
    private double imageYOffset;
    private Image xImage;
    private Image oImage;
    private static final double IMAGE_SCALE = 0.75;
    private GraphicsContext graphicsContext;

    private int winningPlayer = 0;
    private int gameOverResult;
    private char playerChar;
    private boolean canPlay;
    private boolean firstMove;
    private Board board;
    private Connection connection;

    /**
     * Initializes the TicTacToeOnlineController with the specified URL and resource bundle.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The ResourceBundle for the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        container.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(event -> {
                    connection.sendMessage("DISCONNECTED");
                    connection.disconnect();
                });
            }
        });

        // Initialization of game board and canvas.
        board = new Board();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board.set(i, j, '_');
            }
        }
        cellWidth = myCanvas.getWidth() / 3;
        cellHeight = myCanvas.getHeight() / 3;
        imageWidth = cellWidth * IMAGE_SCALE;
        imageHeight = cellHeight * IMAGE_SCALE;
        imageXOffset = (cellWidth - imageWidth) / 2;
        imageYOffset = (cellHeight - imageHeight) / 2;
        Image myImage = new Image(Objects.requireNonNull(MainApplication.class.getResource("img/board.png")).toString());
        xImage = new Image(Objects.requireNonNull(MainApplication.class.getResource("img/x.png")).toString());
        oImage = new Image(Objects.requireNonNull(MainApplication.class.getResource("img/o.png")).toString());
        GraphicsContext gc = myCanvas.getGraphicsContext2D();
        gc.drawImage(myImage, 0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        // Handling player moves on mouse click.
        myCanvas.setOnMouseClicked(mouseEvent -> {
            if (gameOverResult != 0 || !canPlay) {
                return;
            }
            int row = (int) (mouseEvent.getY() / cellHeight);
            int col = (int) (mouseEvent.getX() / cellWidth);
            play(gc, row, col);
        });

        // Leaving the game button handler.
        leaveButton.setOnAction(e ->
        {
            try {
                connection.sendMessage("DISCONNECTED");
                connection.disconnect();
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("OnlineMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });
        graphicsContext = gc;
    }

    /**
     * Sets up the player connections, initializes some parameters of the game.
     */
    private void setTexts() {
        String player = connection.receiveMessage();
        if (player.equals("player1")) {
            playerText.setText("X");
            playerChar = 'X';
            winnerText.setText("Waiting for Another Player");
            firstMove = true;
        } else {
            playerText.setText("O");
            playerChar = 'O';
            firstMove = false;
        }
        canPlay = false;
    }

    /**
     * Manages the game loop for online gameplay, updates the board, and handles game messages.
     */
    private void game() {
        GraphicsContext f = graphicsContext;
        while (gameOverResult != 1 && gameOverResult != -1) {
            String message = connection.receiveMessage();
            if (firstMove) {
                winnerText.setText("Tic-Tac-Toe");
                connection.sendMessage("PLAYING");
                firstMove = false;
            }
            if (message.equals("DEFAULT") || message.equals("DISCONNECTED")) {
                winnerText.setText("YOU WIN BECAUSE OTHER PLAYER DISCONNECTION");
                break;
            }
            String boardStr = message.substring(1);
            if (message.charAt(0) == '1' && playerChar == 'X'
                    || message.charAt(0) == '2' && playerChar == 'O') {
                updateBoard(boardStr);
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        if (board.getCharAt(i, j) == 'X') {
                            fakePlay(f, i, j, 'X');
                        } else if (board.getCharAt(i, j) == 'O') {
                            fakePlay(f, i, j, 'O');
                        }
                    }
                }
                canPlay = true;
            } else {
                connection.sendMessage("NOTHING");
            }
        }
        connection.sendMessage("GAMEOVER");
        connection.disconnect();
    }

    /**
     * Updates the game board based on received data.
     *
     * @param boardStr The string representation of the game board.
     */
    private void updateBoard(String boardStr) {
        for (int i = 0, k = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j, ++k) {
                board.set(i, j, boardStr.charAt(k));
            }
        }
    }

    /**
     * Simulates a fake player move on the board for visualization and logic purposes.
     *
     * @param f      The GraphicsContext for drawing.
     * @param r      The row of the move.
     * @param c      The column of the move.
     * @param player The player symbol to be placed on the board.
     */
    private void fakePlay(GraphicsContext f, int r, int c, char player) {
        int x = (int) (cellWidth * c + imageXOffset);
        int y = (int) (cellHeight * r + imageYOffset);
        if (player == 'X') {
            f.drawImage(xImage, x, y, imageWidth, imageHeight);
            gameOverResult = board.isGameOver();
            if (gameOverResult == 1 && (winningPlayer != 1 && winningPlayer != 2)) {
                winningPlayer = 1;
            }
        } else {
            f.drawImage(oImage, x, y, imageWidth, imageHeight);
            gameOverResult = board.isGameOver();
            if (gameOverResult == 1  && (winningPlayer != 1 && winningPlayer != 2)) {
                winningPlayer = 2;
            }
        }
        if (gameOverResult == 1) {
            if (winningPlayer == 1) {
                winnerText.setText("Player X won!");
            } else {
                winnerText.setText("Player O won!");
            }
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!");
        }
    }

    /**
     * Handles the player move on the game board and communicates with the other player.
     *
     * @param f The GraphicsContext for drawing.
     * @param r The row of the move.
     * @param c The column of the move.
     */
    private void play(GraphicsContext f, int r, int c) {
        if (board.getCharAt(r, c) != '_') {
            return;
        }
        int x = (int) (cellWidth * c + imageXOffset);
        int y = (int) (cellHeight * r + imageYOffset);
        if (playerChar == 'X') {
            board.set(r, c, 'X');
            f.drawImage(xImage, x, y, imageWidth, imageHeight);
            connection.sendMessage(getBoardStr());
            gameOverResult = board.isGameOver();
            if (gameOverResult == 1) {
                winningPlayer = 1;
            }
        } else {
            board.set(r, c, 'O');
            f.drawImage(oImage, x, y, imageWidth, imageHeight);
            connection.sendMessage(getBoardStr());
            gameOverResult = board.isGameOver();
            if (gameOverResult == 1) {
                winningPlayer = 2;
            }
        }

        if (gameOverResult == 1) {
            if (winningPlayer == 1) {
                winnerText.setText("Player X won!");
            } else {
                winnerText.setText("Player O won!");
            }
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!");
        }
        canPlay = false;
    }

    /**
     * Forms and returns the string representation of the game board.
     *
     * @return The string representation of the game board.
     */
    private String getBoardStr() {
        String result = playerChar == 'X' ? "2" : "1";
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                result += board.getCharAt(i, j);
            }
        }
        return result;
    }

    /**
     * Sets the connection for the online game and starts the game loop.
     *
     * @param connection The Connection object for handling network communication.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        setTexts();
        Thread gameThread = new Thread(this::game);
        gameThread.start();
    }
}