import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class FileServer3 {

    public static void main(String[] args) throws IOException {
        // Create a thread pool to handle multiple clients simultaneously
        Executor executor = Executors.newFixedThreadPool(10);

        // Create a server socket and bind it to a specific port
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            // Listen for incoming connections
            Socket clientSocket = serverSocket.accept();
            executor.execute(new ClientHandler(clientSocket));
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Get the input and output streams
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();

                // Create a buffer to read data from the client
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);

                // Get the requested file name from the client
                String fileName = new String(buffer, 0, bytesRead);

                // Open the requested file
                FileInputStream fileIn = new FileInputStream(fileName);

                // Send the file to the client
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                // Close the socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
