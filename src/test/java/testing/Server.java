package testing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Socket> connections;
    private ServerSocket serverSocket;
    private int port;

    public static void main(String[] args) {
        Server server = new Server(6000);
        server.start();
    }

    public Server(int _port){
        this.port = _port;
    }

    public void start() {
        try {
            connections = new ArrayList<Socket>();
            serverSocket = new ServerSocket(port);
            System.out.println("Server running. Waiting for connections...");

            while (true) {
                Socket newConnection = serverSocket.accept();
                connections.add(newConnection);
                System.out.println("New connection added.");

                if (connections.size() >= 2) {
                    Socket socket1 = connections.remove(0);
                    Socket socket2 = connections.remove(0);
                    createGameThread(socket1, socket2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDrawByString(String boardStr)
    {
        boolean isDraw = true;
        for (int i = 0; i < 9; ++i) {
            if (boardStr.charAt(i) == '_') {
                isDraw = false;
            }
        }
        return isDraw;
    }

    public void createGameThread(Socket socket1, Socket socket2) {
        Thread gameThread = new Thread(() -> {
            System.out.println("Game thread created for " + socket1.getInetAddress() + " and " + socket2.getInetAddress());

            try {
                PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
                PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
                BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

                String inf = "1_________";

                TicTacToe board = new TicTacToe();

                System.out.println("Waiting for players to accept begin the game");

                in1.readLine();
                in2.readLine();

                while(true) {
                    out1.println(inf);
                    inf = in1.readLine();
                    board.setBoardByString(inf.substring(1));

                    if (board.isGameOver() || checkDrawByString(inf.substring(1))) {
                        out1.println("GAMEOVER");
                        out2.println("GAMEOVER");
                        break;
                    }

                    out2.println(inf);
                    inf = in2.readLine();
                    board.setBoardByString(inf.substring(1));

                    if (board.isGameOver() || checkDrawByString(inf.substring(1))) {
                        out1.println("GAMEOVER");
                        out2.println("GAMEOVER");
                        break;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        });
        gameThread.start();
    }
}
