package ve.ula.tictactoe.model;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Server {

    private Socket[] sockets = new Socket[2];
    private int port;
    private List<Connection> connections;
    private  List<Room> rooms;

    public Server(int _port){
        this.port = _port;
    }

    public void listen() {
        try {
            connections = new ArrayList<Connection>();
            rooms = new ArrayList<Room>();
            createRoom();
            //System.out.println("Waiting for players...");
            ServerSocket ss = new ServerSocket(port);
            while(true) {
                Socket soc1 = ss.accept();

                Connection connection = new Connection(soc1);
                connections.add(connection);
                manageIndividualConnection(connection);
/*
                sockets[0] = soc1;
                PrintWriter out1 = new PrintWriter(soc1.getOutputStream(), true);

                System.out.println("Connection Established 1");
                out1.println("WAITING");

                Socket soc2 = ss.accept();
                sockets[1] = soc2;

                PrintWriter out2 = new PrintWriter(soc2.getOutputStream(), true);

                System.out.println("Connection Established 2");
                out1.println("START");
                out2.println("START");

                Room room = new Room(sockets);
                Thread roomThread = new Thread(room);
                roomThread.start();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRoom() {
        rooms.add(new Room());
    }

    private void manageIndividualConnection(Connection connection) {
        String roomsListString = "";
        for (int i = 0; i < rooms.size(); ++i) {
            roomsListString += rooms.get(i).getRoomName();
            if (i < rooms.size() - 1) {
                roomsListString += " ";
            }
        }
        connection.sendMessage(roomsListString);

        String selectedRoom = connection.receiveMessage();

        for (Room room : rooms) {
            if (room.getRoomName().equals(selectedRoom)) {
                if (room.getNumPlayersConnected() < 2) {
                    if (room.setPlayer(connection)) {
                        System.out.println("Player joined to the room " + room.getRoomName() + " successfully!");
                        connection.sendMessage("JOINED");
                        room.startComunicationWithPlayer();
                        break;
                    }
                }
                System.out.println("Player try to join to the room " + room.getRoomName() + " FAILED");
                connection.sendMessage("FAILED");
                manageIndividualConnection(connection);
                break;
            }
        }
    }

    public static void main(String[] args){
        Server server = new Server(5900);
        server.listen();
    }
    /*
    public Iterator<Room> roomIterator() {
        return new RoomIterator();
    }

    private class RoomIterator implements Iterator<Room> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < rooms.size();
        }

        @Override
        public Room next() {
            return rooms.get(currentIndex++);
        }
    }
    */
}

