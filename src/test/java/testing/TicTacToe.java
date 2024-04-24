package testing;

public class TicTacToe {
    char[][] board = new char[3][3];

    public TicTacToe() {
        for(int i = 0 ; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = '_';
            }
        }
    }

    public boolean makeMove(int row, int col, char player) {
        if ( board [row][col] == '_') {
            board[row][col] = player;
            return true;
        }
        return false;
    }

    public void setBoardByString(String b) {
        for (int i = 0, j = 0, k = 0; k < 9; ++j, ++k) {
            board[i][j] = b.charAt(k);
            if (j == 2) {
                j = -1;
                ++i;
            }
        }
    }

    public void show_board(){
        for (int i = 0 ; i< 3 ; ++i){
            for (int j = 0 ; j < 3; ++j){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public boolean isGameOver() {
        boolean gameover = true;
        if (board[0][0] == 'X' && board[0][1] == 'X' && board[0][2] == 'X') {
            System.out.println("X won");
        } else if (board[1][0] == 'X' && board[1][1] == 'X' && board[1][2] == 'X') {
            System.out.println("X won");
        } else if (board[2][0] == 'X' && board[2][1] == 'X' && board[2][2] == 'X') {
            System.out.println("X won");
        } else if (board[0][0] == 'X' && board[1][0] == 'X' && board[2][0] == 'X') {
            System.out.println("X won");
        } else if (board[1][0] == 'X' && board[2][1] == 'X' && board[2][2] == 'X') {
            System.out.println("X won");
        } else if (board[0][0] == 'X' && board[1][1] == 'X' && board[2][2] == 'X') {
            System.out.println("X won");
        } else if (board[0][2] == 'X' && board[1][1] == 'X' && board[2][0] == 'X') {
            System.out.println("X won");
        } else if (board[0][2] == 'X' && board[1][2] == 'X' && board[2][2] == 'X') {
            System.out.println("X won");
        }
        else if (board[0][0] == 'O' && board[0][1] == 'O' && board[0][2] == 'O') {
            System.out.println("O won");
        } else if (board[1][0] == 'O' && board[1][1] == 'O' && board[1][2] == 'O') {
            System.out.println("O won");
        } else if (board[2][0] == 'O' && board[2][1] == 'O' && board[2][2] == 'O') {
            System.out.println("O won");
        } else if (board[0][0] == 'O' && board[1][0] == 'O' && board[2][0] == 'O') {
            System.out.println("O won");
        } else if (board[1][0] == 'O' && board[2][1] == 'O' && board[2][2] == 'O') {
            System.out.println("O won");
        } else if (board[0][0] == 'O' && board[1][1] == 'O' && board[2][2] == 'O') {
            System.out.println("O won");
        } else if (board[0][2] == 'O' && board[1][1] == 'O' && board[2][0] == 'O') {
            System.out.println("O won");
        } else if (board[0][2] == 'O' && board[1][2] == 'O' && board[2][2] == 'O') {
            System.out.println("O won");
        }
        else gameover = false;
        return gameover;
    }
}