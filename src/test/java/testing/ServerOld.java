package testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOld implements Runnable {
    static private TicTacToe game = new TicTacToe();
    static private boolean finished = false;
    private int port;

    public ServerOld(int _port){
        this.port = _port;
    }
    @Override
    public void run() {
        try {
            System.out.println("Waiting for players...");
            ServerSocket ss = new ServerSocket(port);

            Socket soc1 = ss.accept();
            PrintWriter out1 = new PrintWriter(soc1.getOutputStream(), true);

            System.out.println("Connection Established 1");
            out1.println("WAITING");

            Socket soc2 = ss.accept();
            PrintWriter out2 = new PrintWriter(soc2.getOutputStream(), true);

            System.out.println("Connection Established 2");
            out1.println("START");
            out2.println("START");
            BufferedReader in1 = new BufferedReader(new InputStreamReader(soc1.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(soc2.getInputStream()));
            while(!finished) {

                out1.println("PLAY");
                String message1 = in1.readLine();
                String[] parts = message1.split(" ");

                char player = 'X';//parts[0].charAt(0);
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                //PrintWriter out = new PrintWriter(soc1.getOutputStream(), true);
                process_move(row, col, player);
                game.show_board();
                finished = game.isGameOver();
                if (finished) {
                    out1.println("GAMEOVER");
                    out2.println("GAMEOVER");
                    continue;
                }

                out2.println("PLAY");
                String message2 = in2.readLine();
                parts = message2.split(" ");

                char player2 = 'O';//parts[0].charAt(0);
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);

                //PrintWriter out = new PrintWriter(soc1.getOutputStream(), true);
                process_move(row, col, player2);
                game.show_board();
                finished = game.isGameOver();
                if (finished) {
                    out1.println("GAMEOVER");
                    out2.println("GAMEOVER");
                    continue;
                }

                out1.println("KEEP PLAYING");
                out2.println("KEEP PLAYING");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        ServerOld server = new ServerOld(6000);
        server.run();
    }
    static void process_move(int row, int col, char player)
    {
        game.makeMove(row,col,player);
    }
}