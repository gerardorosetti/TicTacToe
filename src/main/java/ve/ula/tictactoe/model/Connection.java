package ve.ula.tictactoe.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The Connection class represents a connection over a socket
 * facilitating the sending and receiving of messages.
 */
public class Connection {

    private final Socket socketClient;  // Represents the socket used for the connection
    private PrintWriter out;  // Writes data to the output stream of the socket
    private BufferedReader in;  // Reads data from the input stream of the socket

    /**
     * Constructor to initialize the Connection object with the provided socket.
     * @param soc The socket to establish the connection.
     */
    public Connection(Socket soc) {
        socketClient = soc;
        try {
            out = new PrintWriter(socketClient.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message over the connection.
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Receives a message from the connection.
     * @return The received message or "DISCONNECTED" if an error occurs during communication.
     */
    public String receiveMessage() {
            try {
                return in.readLine();
            } catch (Exception e) {
            }
        return "DISCONNECTED";
    }

    /**
     * Closes the socket connection, output stream, and input stream.
     */
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

    /**
     * Checks if the socket connection is active.
     * @return true if the socket is connected, false otherwise.
     */
    public boolean isConnected() {
        return socketClient.isConnected();
    }
}