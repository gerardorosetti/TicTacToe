package ve.ula.tictactoe.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Room implements Runnable{
    private static int roomNumber = 0;
    private Socket[] players_socket = new Socket[2];
    private boolean finished = false;
    private TicTacToe game = new TicTacToe();
    String roomName = "";

    public Room() {
        roomName = "Room-" + ++roomNumber;
    }
    public Room(Socket[] _players_socket){
        this.players_socket = _players_socket;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public void run() {
        try {
            Socket soc1 = players_socket[0];
            Socket soc2 = players_socket[1];

            PrintWriter out1 = new PrintWriter(soc1.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(soc2.getOutputStream(), true);

            BufferedReader in1 = new BufferedReader(new InputStreamReader(soc1.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(soc2.getInputStream()));
            while (!finished) {

                out1.println("PLAY");
                String message1 = in1.readLine();
                String[] parts = message1.split(" ");

                char player = 'X';//parts[0].charAt(0);
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                //PrintWriter out = new PrintWriter(soc1.getOutputStream(), true);
                game.makeMove(row, col, player);
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
                game.makeMove(row, col, player2);
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
}
