package ve.ula.tictactoe.model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private int port;
    private List<Room> rooms;

    public Server(int _port){
        this.port = _port;
        rooms = new ArrayList<Room>();
    }

    private void sendCurrentRoomsInformation(Connection connection) {
        System.out.println("STARTING SENDING ROOMS INFORMATION");
        String RoomsString = "";
        for (int i = 0; i < rooms.size(); ++i) {
            RoomsString += rooms.get(i).getRoomName();
            if (i < rooms.size() - 1) {
                RoomsString += "-";
            }
        }
        connection.sendMessage(RoomsString);
    }
    private void createRoom() {
        Room newRoom = new Room();
        rooms.add(newRoom);
        //new Thread(newRoom).start();
    }

    private void manageIndividualConnection(Connection connection, String message) {

        for (Room room : rooms) {
            if (room.getRoomName().equals(message) && room.getNumPlayersConnected() < 2) {
                if (room.setPlayer(connection)){
                    //connection.resetIn();
                    room.startComunicationWithPlayer();
                    if (room.getNumPlayersConnected() >= 2) {
                        new Thread(room).start();
                    }
                    System.out.println("Player joined to the room " + room.getRoomName() + " successfully!");
                } else {
                    System.out.println("Player try to join to the room " + room.getRoomName() + " FAILED");
                    break;
                }
            }
        }

    }

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
        //connection.disconnect();
    }

    public void listen() {
        System.out.println("SERVER STARTED");
        try {
            createRoom();
            createRoom();
            ServerSocket ss = new ServerSocket(port);
            while(true) {
                Socket soc1 = ss.accept();
                Connection connection = new Connection(soc1);
                new Thread(() -> manageClientRequest(connection)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Server server = new Server(5900);
        server.listen();
    }
}
