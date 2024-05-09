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
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Board;
import ve.ula.tictactoe.model.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
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

    int winningPlayer = 0;
    int gameOverResult;
    private static final double IMAGE_SCALE = 0.75;
    private char playerChar;
    private Connection connection;
    private boolean canPlay;
    private Board board;
    private GraphicsContext graphicsContext;
    private Thread gameThread;
    private boolean firstMove;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        myCanvas.setOnMouseClicked(mouseEvent -> {
            if (gameOverResult != 0 || !canPlay) {
                return;
            }
            int row = (int) (mouseEvent.getY() / cellHeight);
            int col = (int) (mouseEvent.getX() / cellWidth);
            play(gc, row, col);
        });
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
    private void setTexts() {
        String player = connection.receiveMessage();
        System.out.println("JUGADOR TESTING STRING: " + player);
        if (player.equals("player1")) {
            playerText.setText("X");
            playerChar = 'X';
            firstMove = true;
        } else {
            playerText.setText("O");
            playerChar = 'O';
            firstMove = false;
        }
        canPlay = false;
    }
    private void game() {
        GraphicsContext f = graphicsContext;
        System.out.println("GAME STARTED LOCALLY");
        while (gameOverResult != 1 && gameOverResult != -1) {
            String message = connection.receiveMessage();
            if (firstMove) {
                connection.sendMessage("PLAYING");
                firstMove = false;
            }
            if (message.equals("DEFAULT") || message.equals("DISCONNECTED")) {
                winnerText.setText("YOU WIN BECAUSE OTHER PLAYER DISCONNECTION");
                System.out.println("WIN FOR DEFAULT");
                break;
            }
            String boardStr = message.substring(1);
            System.out.println(message);
            System.out.println(boardStr);
            if (message.charAt(0) == '1' && playerChar == 'X'
                    || message.charAt(0) == '2' && playerChar == 'O') {
                //String oldBoard = getBoardStr();
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
            System.out.println(canPlay);
        }
        connection.sendMessage("GAMEOVER");
        connection.disconnect();
    }
    private void updateBoard(String boardStr) {
        for (int i = 0, k = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j, ++k) {
                board.set(i, j, boardStr.charAt(k));
            }
        }
    }
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
                winnerText.setText("Player X won!!");
            } else {
                winnerText.setText("Player O won!!");
            }
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!!");
        }
    }
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
                winnerText.setText("Player X won!!");
            } else {
                winnerText.setText("Player O won!!");
            }
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!!");
        }
        canPlay = false;
    }
    private String getBoardStr() {
        String result = playerChar == 'X' ? "2" : "1";
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                result += board.getCharAt(i, j);//charBoard[i][j];
            }
        }
        return result;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
        setTexts();
        gameThread = new Thread(this::game);
        gameThread.start();
    }
}