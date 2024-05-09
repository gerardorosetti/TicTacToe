package ve.ula.tictactoe.model;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
/**
 *The Server class represents the server side of the Tic Tac Toe game,
 *handling client connections and game rooms.
*/
public class Server {

    private int port; // The port on which the server is running.
    private List<Room> rooms; // A list of active game rooms.
    private boolean serverRunning; // A flag to indicate if the server is running.

    /**
     *Constructor for the Server class, initializes the server with the specified port.
     *@param _port The port on which the server should run.
    */
    public Server(int _port){
        this.port = _port;
        this.serverRunning = true;
        rooms = new ArrayList<Room>();
    }

     /**
      *Sends information about the current active game rooms to the specified connection.
      *param connection The connection to send the room information to.
    */
    private void sendCurrentRoomsInformation(Connection connection) {
        String RoomsString = "";
        for (int i = 0; i < rooms.size(); ++i) {
            RoomsString += rooms.get(i).getRoomName();
            if (i < rooms.size() - 1) {
                RoomsString += "-";
            }
        }
        connection.sendMessage(RoomsString);
    }
    /**
     *Creates a new game room and adds it to the list of active rooms.
    */
    public void createRoom() {
        Room newRoom = new Room();
        rooms.add(newRoom);
    }
    /**
     *Manages an individual client connection and the corresponding message received from the client.
     *@param connection The client connection.
     *@param message    The message received from the client.
    */
    private void manageIndividualConnection(Connection connection, String message) {
        for (Room room : rooms) {
            if (room.getRoomName().equals(message) && room.getNumPlayersConnected() < 2) {
                if (room.setPlayer(connection)){
                    room.startComunicationWithPlayer();
                    if (room.getNumPlayersConnected() >= 2) {
                        new Thread(room).start();
                    } else if (room.getNumPlayersConnected() == 1) {
                        new Thread(room::waitingForPlayer).start();
                    }
                } else {
                    break;
                }
            }
        }
    }
    /**
     *Manages requests from the client connection.
     *@param connection The client connection.
    */
    private void manageClientRequest(Connection connection){
        String message = connection.receiveMessage();
        if(message.equals("SEND ROOMS")){
            sendCurrentRoomsInformation(connection);
            connection.disconnect();
        } else if (message.equals("CREATE")) {
            createRoom();
            connection.disconnect();
        }else {
            manageIndividualConnection(connection, message);
        }
    }
    /**
     * Stops the server from running.
    */
    public void stopServer(){
        this.serverRunning = false;
    }
    /**
     * Listens for incoming client connections and manages them on separate threads.
     */
    public void listen() {
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.setSoTimeout(1000);
            serverRunning = true;
            while(serverRunning) {
                try{
                    Socket soc1 = ss.accept();
                    Connection connection = new Connection(soc1);
                    new Thread(() -> manageClientRequest(connection)).start();
                }catch (SocketTimeoutException e){
                }
            }
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}