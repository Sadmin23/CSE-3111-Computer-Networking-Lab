import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class FileServer2 {

    public static void main(String[] args) throws IOException {
        // Create a thread pool to handle multiple clients simultaneously
        Executor executor = Executors.newFixedThreadPool(10);

        // Create a server socket and bind it to a specific port
        ServerSocket serverSocket = new ServerSocket(5000);

        while (true) {
            // Listen for incoming connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("A new client added!");
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
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Read the first line of the request (GET /file.txt HTTP/1.1)
                String  clientsms = dataInputStream.readUTF();
                String[] requestParts = clientsms.toString().split(" ");
                System.out.println(requestParts[0]);

                // Make sure the request is a GET request
                if (!requestParts[0].equals("GET")) {
                    out.writeBytes("HTTP/1.1 400 Bad Request\r\n");
                    out.writeBytes("\r\n");
                    out.flush();
                    clientSocket.close();
                    return;
                }

                // Get the requested file name
                String fileName = requestParts[1];
                fileName = fileName.substring(1);

                // Open the requested file
                File file = new File(fileName);
                if (!file.exists()) {
                    out.writeBytes("HTTP/1.1 404 Not Found\r\n");
                    out.writeBytes("\r\n");
                    out.flush();
                    clientSocket.close();
                    return;
                }

                // Send the HTTP headers
                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("Content-Type: application/octet-stream\r\n");
                out.writeBytes("Content-Length: " + file.length() + "\r\n");
                out.writeBytes("\r\n");

                // Send the file
                FileInputStream fileIn = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
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