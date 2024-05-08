package ve.ula.tictactoe.model;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class Server {

    private int port;
    private List<Room> rooms;
    private boolean serverRunning;

    public Server(int _port){
        this.port = _port;
        this.serverRunning = true;
        rooms = new ArrayList<Room>();
    }

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
    public void createRoom() {
        Room newRoom = new Room();
        rooms.add(newRoom);
    }

    private void manageIndividualConnection(Connection connection, String message) {
        for (Room room : rooms) {
            if (room.getRoomName().equals(message) && room.getNumPlayersConnected() < 2) {
                if (room.setPlayer(connection)){
                    room.startComunicationWithPlayer();
                    if (room.getNumPlayersConnected() >= 2) {
                        new Thread(room).start();
                    }
                    System.out.println("Player joined to the room " + room.getRoomName() + " successfully!");
                } else {
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
    }
    public void stopServer(){
        this.serverRunning = false;
    }

    public void listen() {
        System.out.println("SERVER STARTED");
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.setSoTimeout(1000);
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
