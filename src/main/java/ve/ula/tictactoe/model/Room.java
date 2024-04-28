package ve.ula.tictactoe.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Room implements Runnable{

    private static int roomNumber = 0;
    //private Socket[] players_socket = new Socket[2];
    private boolean finished;
    private TicTacToe game;
    private  String roomName = "";
    private final Connection[] playersConnections;
    private int numPlayersConnected;
    private boolean endThread;

    public Room() {
        roomName = "Room " + ++roomNumber + " | Current Players: " + numPlayersConnected;
        playersConnections = new Connection[2];
        playersConnections[0] = null;
        playersConnections[1] = null;
        numPlayersConnected = 0;
        game = new TicTacToe();
        finished = false;
        endThread = false;
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
            //connection.sendMessage("JOINED");
            //playersConnections[0].sendMessage("player1");
        } else {
            playersConnections[1] = connection;
            //playersConnections[1].sendMessage("player2");
        }
        //connection.sendMessage("JOINED");
        ++numPlayersConnected;
        return true;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    @Override
    public void run() {
        while (!endThread) {
            if (numPlayersConnected >= 2) {
                try {
                    while (!finished) {
                        playersConnections[0].sendMessage("PLAY");
                        String message1 = playersConnections[0].receiveMessage();
                        String[] parts = message1.split(" ");

                        char player = 'X';
                        int row = Integer.parseInt(parts[0]);
                        int col = Integer.parseInt(parts[1]);

                        game.makeMove(row, col, player);
                        game.show_board();
                        finished = game.isGameOver();
                        if (finished) {
                            for (short i = 0; i < 2; ++i) {
                                playersConnections[i].sendMessage("GAMEOVER");
                            }
                            continue;
                        }

                        playersConnections[1].sendMessage("PLAY");
                        String message2 = playersConnections[1].receiveMessage();
                        parts = message2.split(" ");

                        char player2 = 'O';
                        row = Integer.parseInt(parts[0]);
                        col = Integer.parseInt(parts[1]);

                        game.makeMove(row, col, player2);
                        game.show_board();
                        finished = game.isGameOver();
                        if (finished) {
                            for (short i = 0; i < 2; ++i) {
                                playersConnections[i].sendMessage("GAMEOVER");
                            }
                            continue;
                        }
                        for (short i = 0; i < 2; ++i) {
                            playersConnections[i].sendMessage("KEEP PLAYING");
                        }
                    }
                    game.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void endThreadExecution() {
        endThread = true;
    }
}
