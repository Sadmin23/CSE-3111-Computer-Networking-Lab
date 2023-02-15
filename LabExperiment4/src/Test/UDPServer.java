package Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        // Create a DatagramSocket
        int port = 1234;
        DatagramSocket socket = new DatagramSocket(port);

        // Receive a message from the client
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Client message: " + message);

        // Send a response to the client
        String response = message;
        buffer = response.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
        socket.send(packet);

        // Close the socket
        socket.close();
    }
}