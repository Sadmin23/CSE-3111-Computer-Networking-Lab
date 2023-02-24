package task2;

import java.net.*;
import java.io.*;

public class CongestionAvoidance {
    private static final int PORT = 8080;
    private static final int MAX_PACKET_SIZE = 1024;
    private static final double INITIAL_WINDOW_SIZE = 1.0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleConnection(clientSocket);
                    } catch (IOException e) {
                        System.err.println("Error handling connection: " + e);
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT + ": " + e);
        }
    }

    private static void handleConnection(Socket clientSocket) throws IOException {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            double windowSize = INITIAL_WINDOW_SIZE;
            byte[] buffer = new byte[MAX_PACKET_SIZE];
            int numPacketsReceived = 0;

            while (true) {
                int numBytesReceived = in.read(buffer);
                if (numBytesReceived < 0) {
                    break;
                }

                numPacketsReceived++;
                if (numPacketsReceived % 10 == 0) {
                    windowSize *= 0.5; // congestion control: decrease window size
                }

                out.write(buffer, 0, numBytesReceived);
                out.flush();

                if (windowSize < 1.0 && numPacketsReceived % 10 == 0) {
                    windowSize += 0.1; // congestion control: increase window size
                }
            }
        }
    }
}
