import java.net.*;
import java.io.*;

public class Server1a {
    public static void main(String args[]) throws IOException {
        System.out.println("Server started");
        System.out.println("Waiting for Clients...");
        ServerSocket serverSocket = new ServerSocket(5000);

        Socket socket = serverSocket.accept();
        System.out.println("Client Accepted");
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        try {
            Object cMsg = ois.readObject();
            System.out.println("From Client: " + (String) cMsg);

            String serverMsg = (String) cMsg;
            serverMsg = serverMsg.toUpperCase();

            oos.writeObject(serverMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
