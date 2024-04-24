package ve.ula.tictactoe.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    private Socket socketClient;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket soc) {
        socketClient = soc;
        try {
            out = new PrintWriter(socketClient.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() {
        try {
            String message = in.readLine();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}