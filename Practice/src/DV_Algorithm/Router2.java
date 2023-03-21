package DV_Algorithm;

import java.io.*;
import java.net.*;

public class Router2 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = new ServerSocket(12345);
        Socket socket = serverSocket.accept();

        // Get the input stream
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Receive the 2D array and the integer value
        int[][] array = (int[][]) in.readObject();
        int value = in.readInt();

        // Close the socket and the input stream
        in.close();
        socket.close();

    }
}