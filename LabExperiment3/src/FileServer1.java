import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class FileServer1 {

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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Read the first line of the request (POST /file.txt HTTP/1.1)
                String request = in.readLine();
                String[] requestParts = request.split(" ");

                // Make sure the request is a POST request
                if (!requestParts[0].equals("POST")) {
                    out.writeBytes("HTTP/1.1 400 Bad Request\r\n");
                    out.writeBytes("\r\n");
                    out.flush();
                    clientSocket.close();
                    return;
                }

                // Get the requested file name
                String fileName = requestParts[1];
                fileName = fileName.substring(1);

                // Get the content length from the headers
                int contentLength = 0;
                String line;
                while (!(line = in.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
                    }
                }

                // Read the file data
                byte[] fileData = new byte[contentLength];
                int bytesRead = 0;
                while (bytesRead < contentLength) {
                    bytesRead += clientSocket.getInputStream().read(fileData, bytesRead, contentLength - bytesRead);
                }

                // Save the file
                FileOutputStream fileOut = new FileOutputStream(fileName);
                fileOut.write(fileData);
                fileOut.close();

                // Send the HTTP response
                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("\r\n");
                out.flush();

                // Close the socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
