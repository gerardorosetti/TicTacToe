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

    public void waitingForPlayer() {
        if (numPlayersConnected == 1) {
            String message = playersConnections[0].receiveMessage();

            if (message != null) {
                if (message.equals("DISCONNECTED")) {
                    --numPlayersConnected;
                    playersConnections[0] = null;
                    roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
                }
            }
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
        }
        if (numPlayersConnected == 0) {
            roomName = "Room " + id + " | Current Players: " + ++numPlayersConnected;
        } else {
            roomName = "Room " + id + " | Current Players: " + ++numPlayersConnected + " (Playing)";
        }
        return true;
    }

    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    @Override
    public void run() {
        try {
            String board = "1_________";
            while (!finished) {
                sendMessageToAll(board);
                board = getPlayFromConnections();
                if (board.equals("GAMEOVER")) {
                    finished = true;
                }
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
        String player1 = playersConnections[0].receiveMessage();
        String player2 = playersConnections[1].receiveMessage();
        if (player1 == null || player2 == null) {
            if (player1 == null && player2 == null) {
                return "GAMEOVER";
            }
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
