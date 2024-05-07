package ve.ula.tictactoe.model;

public class Room implements Runnable{

    private static int roomNumber = 0;
    private boolean finished;
    private  String roomName = "";
    private final Connection[] playersConnections;
    private int numPlayersConnected;
    private final int id;

    public Room() {
        id = ++roomNumber;
        roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
        playersConnections = new Connection[2];
        playersConnections[0] = null;
        playersConnections[1] = null;
        numPlayersConnected = 0;
        finished = false;
    }
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
        } else {
            playersConnections[1] = connection;
        }roomName = "Room " + id + " | Current Players: " + ++numPlayersConnected;
        return true;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    @Override
    public void run() {
        try {
            System.out.println("GAME STARTED");
            String board = "1_________";
            System.out.println(board);
            while (!finished) {
                sendMessageToAll(board);
                board = getPlayFromConnections();
                if (board.equals("GAMEOVER")) {
                    finished = true;
                }
                System.out.println(board);
            }
            finished = false;
            removePlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAll(String str) {
        for (int i = 0; i < 2; ++i) {
            if (playersConnections[i] != null) {
                if (playersConnections[i].isConnected()) {
                    playersConnections[i].sendMessage(str);
                }
            }
        }
    }

    private String getPlayFromConnections() {
        String player1 = "ping";
        while (player1.equals("ping")) {
            player1 = playersConnections[0].receiveMessage();
            if (player1 == null) {
                break;
            }
        }
        String player2 = "ping";
        while (player2.equals("ping")) {
            player2 = playersConnections[1].receiveMessage();
            if (player2 == null) {
                break;
            }
        }
        if (player1 == null || player2 == null) {
            if (player1 != null) {
                if (player1.equals("GAMEOVER")) {
                    return "GAMEOVER";
                }
            } else if (player2 != null) {
                if (player2.equals("GAMEOVER")) {
                    return "GAMEOVER";
                }
            }
            return "DEFAULT";
        } else if (player1.equals("GAMEOVER") || player2.equals("GAMEOVER")) {
            return "GAMEOVER";
        } else if (player1.equals("DISCONNECTED") || player2.equals("DISCONNECTED")) {
            return "DEFAULT";
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
}
