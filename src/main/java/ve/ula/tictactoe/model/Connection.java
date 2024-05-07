package ve.ula.tictactoe.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connection {

    private final Socket socketClient;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket soc) {
        socketClient = soc;
        try {
            socketClient.setSoTimeout(2500);
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
        while (true) {
            try {
                return in.readLine();
            } catch (SocketTimeoutException timeout) {
                try {
                    PrintWriter writer = new PrintWriter(socketClient.getOutputStream(), true);
                    writer.println("ping"); // Enviar un mensaje de prueba al cliente
                    System.out.println("La conexión con el cliente: " + socketClient +"aún está activa.");
                } catch (IOException ex) {
                    System.out.println("Se ha perdido la conexión con el cliente.");
                    break;
                }
            } catch (Exception e) {
                break;
                //e.printStackTrace();
            }
        }
        return "DISCONNECTED";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socketClient.isConnected();
    }
}