package ve.ula.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Board;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TicTacToeGameController implements Initializable {

    @FXML
    public Text winnerText;

    @FXML
    public Button reset;
    @FXML
    public Button returnMenuButton;
    @FXML
    private VBox container;
    @FXML
    private Canvas myCanvas;

    private double cellWidth;
    private double cellHeight;

    private double imageWidth;
    private double imageHeight;

    private double imageXOffset;
    private double imageYOffset;

    private int playerTurn = 1;

    private Image xImage;
    private Image oImage;

    private Board board;

    int winningPlayer = 0;
    int gameOverResult;



    private static final double IMAGE_SCALE = 0.75;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board = new Board();

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
            if (gameOverResult != 0) {
                return;
            }
            int row = (int) (mouseEvent.getY() / cellHeight);
            int col = (int) (mouseEvent.getX() / cellWidth);

           player(gc, row, col);
        });

        reset.setOnAction(e -> {
            board.reset();
            winningPlayer = 0;
            gameOverResult = 0;
            gc.drawImage(myImage, 0, 0, myCanvas.getWidth(), myCanvas.getHeight());
            winnerText.setText("Tic-Tac-Toe");
            //restart.setDisable(true);
            reset.setDisable(true);
        });

        returnMenuButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
                //receiveRoomsList.cancel();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });

        //restart.setDisable(true);
        reset.setDisable(true);
    }

    public void player(GraphicsContext f, int r, int c) {
        int x = (int) (cellWidth * c + imageXOffset);
        int y = (int) (cellHeight * r + imageYOffset);

        winningPlayer = 0;

        if (playerTurn == 1) {

            if (board.set(r, c, 'X')) {
                f.drawImage(xImage, x, y, imageWidth, imageHeight);
                gameOverResult = board.isGameOver();

                if (gameOverResult == 1) {
                    winningPlayer = 1;
                } else {
                    playerTurn = 2;
                }
            }
        }
        else
        {
            if (board.set(r, c, 'O')) {
                f.drawImage(oImage, x, y, imageWidth, imageHeight);
                gameOverResult = board.isGameOver();

                if (gameOverResult == 1) {
                    winningPlayer = 2;
                } else {
                    playerTurn = 1;
                }
            }
        }

        if (gameOverResult == 1) {
            winnerText.setText("Player " + winningPlayer + " won!!");
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!!");
        }

        reset.setDisable(gameOverResult == 0);
    }

}
