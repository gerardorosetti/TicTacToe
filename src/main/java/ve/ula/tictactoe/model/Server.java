package ve.ula.tictactoe.model;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

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
    }

    public static void main(String[] args){
        Server server = new Server(5900);
        server.listen();
    }
}