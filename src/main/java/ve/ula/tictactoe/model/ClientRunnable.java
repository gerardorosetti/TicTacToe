package ve.ula.tictactoe.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable{

    private int port;

    public ClientRunnable(int _port){
        this.port = _port;
    }

    @Override
    public void run() {
        try{
            System.out.println("Client Started");
            Socket soc = new Socket("localhost", port);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Waiting for another player");

            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            while(in.readLine().equals("WAITING")) {

            }

            System.out.println("Starting Game");
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            boolean gameover = false;
            while(!gameover) {
                String message = in.readLine();
                if (message.equals("PLAY")){
                    System.out.println("Enter the row (0-2):");
                    int row = Integer.parseInt(userInput.readLine());

                    System.out.println("Enter the column (0-2):");
                    int col = Integer.parseInt(userInput.readLine());

                    out.println(row + " " + col);
                }
                if (message.equals("GAMEOVER")) {gameover = true; continue;}
                if (in.readLine().equals("GAMEOVER")) gameover = true;
            }
            System.out.println("GAME FINISHED");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        ClientRunnable client = new ClientRunnable(5900);
        client.run();
    }
}