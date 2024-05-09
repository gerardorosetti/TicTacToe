package ve.ula.tictactoe.model;
/**
 * The Room class represents a room in a multiplayer game where players can communicate with each other and play a game.
 */
public class Room implements Runnable{

    private static int roomNumber = 0; // Keeps track of the room number
    private final int id; // The unique identifier for the room
    private boolean finished; // Indicates whether the game in the room has finished
    private String roomName; // The name of the room
    private final Connection[] playersConnections; // An array to store player connections
    private int numPlayersConnected; // The number of players connected to the room

    /**
     * Constructor for the Room class.
     */
    public Room() {
        id = ++roomNumber;
        roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
        playersConnections = new Connection[2];
        playersConnections[0] = null;
        playersConnections[1] = null;
        numPlayersConnected = 0;
        finished = false;
    }
    /**
    * Retrieves the name of the room.
    * @return The name of the room.
    */
    public String getRoomName() {
        return roomName;
    }
    /**
     * Initiates communication with the player who is waiting for an opponent.
     */
    public void startComunicationWithPlayer() {
        if (numPlayersConnected == 1) {
            playersConnections[0].sendMessage("player1");
        } else {
            playersConnections[1].sendMessage("player2");
        }
    }

    /**
     * Waits for the second player to join the room and sets up the game.
     */
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
    /**
     * Adds a player to the room.
     * @param connection The player's connection to be added to the room.
     * @return True if the player is successfully added, false if the room is full.
     */
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

    /**
     * Retrieves the number of players connected to the room.
     * @return The number of players connected to the room.
     */
    public int getNumPlayersConnected() {
        return numPlayersConnected;
    }

    /**
     * Executes the game logic within the room.
     */
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

    /**
     * Sends a message to all players connected to the room.
     * @param str The message to be sent.
     */
    private void sendMessageToAll(String str) {
        for (int i = 0; i < 2; ++i) {
            if (playersConnections[i] != null) {
                if (playersConnections[i].isConnected()) {
                    playersConnections[i].sendMessage(str);
                }
            }
        }
    }

    /**
     * Retrieves the game play from the connected players.
     * @return The game play received from the players.
     */
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

    /**
     * Remove players of the room
     */
    private void removePlayers() {
        for (int i = 0; i < 2; ++i) {
            playersConnections[i].disconnect();
            playersConnections[i] = null;
            --numPlayersConnected;
        }
        roomName = "Room " + id + " | Current Players: " + numPlayersConnected;
    }
}
