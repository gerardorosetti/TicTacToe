package testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Client(){
        try{
            System.out.println("Client Started");
            Socket soc = new Socket("localhost", 6000);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Waiting for another player");

            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

            out.println("READY");

            String message = in.readLine();
            char player = message.charAt(0);

            String boardStr = message.substring(1);

            char[][] board = getBoardByString(boardStr);

            while(true) {
                int row, col;
                do {
                    printBoardByString(boardStr);
                    System.out.println("Enter the row (0-2):");
                    row = Integer.parseInt(userInput.readLine());
                    System.out.println("Enter the column (0-2):");
                    col = Integer.parseInt(userInput.readLine());
                }while (!(board[row][col] == '_'));
                String ans = "";
                if (player == '1')
                {
                    board[row][col] = 'X';
                    ans += '2';
                }
                else {
                    board[row][col] = 'O';
                    ans += '1';
                }
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        ans += board[i][j];
                    }
                }
                out.println(ans);
                message = in.readLine();
                if (message.equals("GAMEOVER")) {
                    break;
                }
                player = message.charAt(0);
                boardStr = message.substring(1);
                board = getBoardByString(boardStr);
            }
            System.out.println("GAME FINISHED");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printBoardByString(String inf) {
        for (int i = 0; i < 9; ++i) {
            if (i%3 == 0 && i > 0) {
                System.out.println();
            }
            System.out.print(inf.charAt(i));
        }
    }

    private char[][] getBoardByString(String inf) {
        char[][] board = new char[3][3];
        for (int i = 0, j = 0, k = 0; k < 9; ++j, ++k) {
            board[i][j] = inf.charAt(k);
            if (j == 2) {
                j = -1;
                ++i;
            }
        }
        return board;
    }

    public static void main(String[] args){
        Client client = new Client();
    }
}