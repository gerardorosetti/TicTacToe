package ve.ula.tictactoe.model;

public class Board {
    public final static int NUM_ROWS = 3;
    public final static int NUM_COLS = 3;
    private final char blank = '_';

    private char[][] board;

    public Board() {
        board = new char[NUM_ROWS][NUM_COLS];
        reset();
    }

    public char getCharAt(int i, int j) {
        return board[i][j];
    }

    public void reset() {
        for (int i = 0; i < NUM_ROWS; ++i) {
            for (int j = 0; j < NUM_COLS; ++j) {
                board[i][j] = blank;
            }
        }
    }

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
                    return 0;
                }
            }
        }

        return -1;
    }

}
