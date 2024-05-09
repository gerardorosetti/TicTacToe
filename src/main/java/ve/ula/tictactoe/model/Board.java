package ve.ula.tictactoe.model;
/**
 * The Board class represents a Tic-Tac-Toe board with a specific size
 * and functionality to set marks, reset the board, and determine the game status.
 */
public class Board {
    public final static int NUM_ROWS = 3;
    public final static int NUM_COLS = 3;
    private final char blank = '_';

    private char[][] board; // The 2D array representing the game board

    /**
     * Constructor for the Board class. Initializes the board and resets it.
     */
    public Board() {
        board = new char[NUM_ROWS][NUM_COLS];
        reset();
    }

    /**
     * Retrieves the character at the specified position on the board.
     * @param i The row index.
     * @param j The column index.
     * @return The character at the specified position.
     */
    public char getCharAt(int i, int j) {
        return board[i][j];
    }

    /**
     * Resets the board by filling it with blank characters.
     */
    public void reset() {
        for (int i = 0; i < NUM_ROWS; ++i) {
            for (int j = 0; j < NUM_COLS; ++j) {
                board[i][j] = blank;
            }
        }
    }

    /**
     * Attempts to set a character at the specified position on the board.
     * @param r The row index.
     * @param c The column index.
     * @param t The character to be set.
     * @return true if set successfully, false otherwise.
     */
    public boolean set(int r, int c, char t) {
        if (r < 0 || r >= NUM_ROWS || c < 0 || c >= NUM_COLS){
            return false;
        }

        if (board[r][c] != blank) {
            return false;
        }

        board[r][c] = t;
        return true;
    }

    // Private Helper Methods
    private boolean checkRow(int i) {
        char c = board[i][0];

        if (c == blank) {
            return false;
        }

        for (int j = 1; j < NUM_COLS; ++j) {
            if (board[i][j] != c) {
                return false;
            }
        }

        return true;
    }

    private boolean checkCol(int j) {
        char c = board[0][j];

        if (c == blank) {
            return false;
        }

        for (int i = 1; i < NUM_ROWS; ++i) {
            if (board[i][j] != c) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines the game status by checking rows, columns, and diagonal lines.
     * @return 1 if a win is detected, 0 if the game continues, -1 if it's a tie.
     */
    public int isGameOver() {
        for (int i = 0; i < NUM_ROWS; ++i){
            if (checkRow(i)) {
                return 1;
            }
        }

        for (int j = 0; j < NUM_COLS; ++j) {
            if (checkCol(j)) {
                return 1;
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != blank) {
            return 1;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != blank) {
            return 1;
        }

        for (int i = 0; i < NUM_ROWS; ++i) {
            for (int j = 0; j < NUM_COLS; ++j) {
                if (board[i][j] == blank) {
                    return 0; // Game continues
                }
            }
        }
        return -1; //Tie
    }
}
