/**
 * TicTacToeGameController class controls the gameplay of the Tic Tac Toe game in local/offline mode.
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
import ve.ula.tictactoe.MainApplication;
import ve.ula.tictactoe.model.Board;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The TicTacToeGameController class implements the Initializable interface and controls the gameplay of Tic Tac Toe.
 */
public class TicTacToeGameController implements Initializable {

    @FXML
    private Text winnerText; // Text field to display the winner or tie message.
    @FXML
    private Button reset; // Button to reset the game.
    @FXML
    private Button returnMenuButton; // Button to return to the main menu.
    @FXML
    private VBox container; // Container for holding various UI elements.
    @FXML
    private Canvas myCanvas; // Canvas for drawing the game board.

    private double cellWidth;
    private double cellHeight;
    private double imageWidth;
    private double imageHeight;
    private double imageXOffset;
    private double imageYOffset;
    private int playerTurn = 1;
    private Image xImage;
    private Image oImage;
    private static final double IMAGE_SCALE = 0.75;

    private Board board;
    private int winningPlayer = 0;
    private int gameOverResult;

    /**
     * Initializes the TicTacToeGameController with the specified URL and resource bundle.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The ResourceBundle for the root object, or null if there is no ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board = new Board(); // Initialize the game board.

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
            reset.setDisable(true);
        });

        returnMenuButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainMenuView.fxml"));
                Parent fxmlContent = loader.load();
                container.getChildren().clear();
                container.getChildren().add(fxmlContent);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        });
        reset.setDisable(true);
    }

    /**
     * Handles the player's move on the game board.
     *
     * @param f The GraphicsContext for drawing.
     * @param r The row where the move is made.
     * @param c The column where the move is made.
     */
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
            winnerText.setText("Player " + winningPlayer + " won!");
        } else if (gameOverResult == -1) {
            winnerText.setText("Game tied!");
        }
        reset.setDisable(gameOverResult == 0);
    }
}