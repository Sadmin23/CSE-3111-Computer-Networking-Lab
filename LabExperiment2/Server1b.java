import java.net.*;
import java.io.*;

public class Server1b {
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
            System.out.println("From Client: " + (int) cMsg);

            int serverMsg = (int) cMsg;
            // serverMsg = serverMsg.toUpperCase();

            boolean ans = isPrime(serverMsg);

            if (ans == true)
                oos.writeObject("The number is prime");
            else
                oos.writeObject("The number is not prime");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= num / 2; i++) {
            if ((num % i) == 0)
                return false;
        }
        return true;
    }
}
