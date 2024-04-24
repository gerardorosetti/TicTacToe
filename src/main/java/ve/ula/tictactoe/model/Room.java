package ve.ula.tictactoe.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Room/* implements Runnable*/{

    private static int roomNumber = 0;
    //private Socket[] players_socket = new Socket[2];
    private boolean finished;
    private TicTacToe game;
    private  String roomName = "";
    private final Connection[] playersConnections;
    private int numPlayersConnected;

    public Room() {
        roomName = "Room-" + ++roomNumber;
        playersConnections = new Connection[2];
        playersConnections[0] = null;
        playersConnections[1] = null;
        numPlayersConnected = 0;
        game = new TicTacToe();
        finished = false;
    }
    /*public Room(Socket[] _players_socket){
        this.players_socket = _players_socket;
    }*/
    public String getRoomName() {
        return roomName;
    }

    public void startComunicationWithPlayer() {
        if (numPlayersConnected == 1) {
            playersConnections[0].sendMessage("player1");
        } else {
            playersConnections[1].sendMessage("player2");
        }
    }

    public boolean setPlayer(Connection connection) {
        if (numPlayersConnected >= 2) {
            return false;
        }
        if (playersConnections[0] == null) {
            playersConnections[0] = connection;
            //playersConnections[0].sendMessage("player1");
        } else {
            playersConnections[1] = connection;
            //playersConnections[1].sendMessage("player2");
        }
        ++numPlayersConnected;
        return true;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    /*@Override
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
    }*/
}
