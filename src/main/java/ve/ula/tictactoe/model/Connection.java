package ve.ula.tictactoe.model;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    private final Socket socketClient;
    private PrintWriter out;
    private BufferedReader in;
    private PrintWriter outList;
    private BufferedReader inList;

    public Connection(Socket soc) {
        socketClient = soc;
        try {
            out = new PrintWriter(socketClient.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            outList = new PrintWriter(socketClient.getOutputStream(), true);
            inList = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public synchronized void sendRoomsList(String message) {
        outList.println(message);
    }

    public synchronized String receiveRoomsList() {
        try {
            return inList.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void disconnect() {

        try {
            if (socketClient != null && !socketClient.isClosed()) {
                socketClient.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (outList != null) {
                outList.close();
            }
            if (inList != null) {
                inList.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkConnection() {
        return socketClient.isConnected();
    }
}