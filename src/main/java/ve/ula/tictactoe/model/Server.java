package ve.ula.tictactoe.model;
import javafx.application.Platform;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class Server {

    //private Socket[] sockets = new Socket[2];
    private int port;
    private List<Room> rooms;
    //private List<Thread> threads;

    public Server(int _port){
        this.port = _port;
        rooms = new ArrayList<Room>();
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
                new Thread(() -> manageIndividualConnection(connection)).start();
                new Thread(() -> sendCurrentRoomsInformation(connection)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRoom() {
        Room newRoom = new Room();
        rooms.add(newRoom);
    }

    private void manageIndividualConnection(Connection connection) {
        System.out.println("CONNECTION SUCCESSFULLY");
        String selectedRoom = connection.receiveMessage();

        for (Room room : rooms) {
            if (room.getRoomName().equals(selectedRoom) && room.getNumPlayersConnected() < 2) {
                if (room.setPlayer(connection)) {
                    connection.sendMessage("JOINED");
                    room.startComunicationWithPlayer();
                    new Thread(room).start();
                    System.out.println("Player joined to the room " + room.getRoomName() + " successfully!");
                } else {
                    System.out.println("Player try to join to the room " + room.getRoomName() + " FAILED");
                    manageIndividualConnection(connection);
                }
            }
        }
    }

    private void sendCurrentRoomsInformation(Connection connection) {
        System.out.println("STARTING SENDING ROOMS INFORMATION");
        while (true) {
            String roomsListString = "";
            for (int i = 0; i < rooms.size(); ++i) {
                roomsListString += rooms.get(i).getRoomName();
                if (i < rooms.size() - 1) {
                    roomsListString += "-";
                }
            }
            connection.sendRoomsList(roomsListString);
        }
    }

    public static void main(String[] args){
        Server server = new Server(5900);
        server.listen();
    }
}

