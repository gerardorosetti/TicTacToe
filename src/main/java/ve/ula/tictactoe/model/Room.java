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
    //private final Connection[] playersConnections;
    private Connection[] playersConnections;
    private int numPlayersConnected;
    private boolean endThread;
    private int id;

    public Room() {
        id = ++roomNumber;
        roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
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
        roomName = "Room " + id + " | Current Players: " + ++numPlayersConnected;
        return true;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    @Override
    public void run() {
        //while (!endThread) {
            //if (numPlayersConnected >= 2) {
                try {
                    System.out.println("GAME STARTED");
                    String board = "1_________";
                    System.out.println(board);
                    while (!finished) {
                        sendMessageToAll(board);
                        //System.out.println("MESSAGE SEND");
                        board = getPlayFromConnections();
                        if (board.equals("GAMEOVER")) {
                            finished = true;
                        }
                        System.out.println(board);
                    }
                    game.reset();
                    finished = false;
                    removePlayers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        //}
    }

    private void sendMessageToAll(String str) {
        for (int i = 0; i < 2; ++i) {
            playersConnections[i].sendMessage(str);
        }
    }

    private String getPlayFromConnections() {
        String player1 = playersConnections[0].receiveMessage();
        String player2 = playersConnections[1].receiveMessage();
        if (player1.equals("GAMEOVER") || player2.equals("GAMEOVER")) {
            return "GAMEOVER";
        } else if (player1.equals("NOTHING")) {
            return player2;
        } else {
            return player1;
        }
    }

    private void removePlayers() {
        for (int i = 0; i < 2; ++i) {
            playersConnections[i].disconnect();
            playersConnections[i] = null;
            --numPlayersConnected;
        }
        roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
    }

    public void endThreadExecution() {
        endThread = true;
    }
}
