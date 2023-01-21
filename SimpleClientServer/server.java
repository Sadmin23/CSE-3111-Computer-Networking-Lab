package SimpleClientServer;

import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) {

        try {
            System.out.println("Waiting for clients...");
            ServerSocket ss = new ServerSocket(9806);
            Socket soc = ss.accept();
            System.out.println("Connection established");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
