package Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        // Create a DatagramSocket
        DatagramSocket socket = new DatagramSocket();

        // Send a message to the server
        String message = "Hello, server!";
        byte[] buffer = message.getBytes();
        InetAddress address = InetAddress.getByName("localhost");
        int port = 1234;
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);

        // Receive a response from the server
        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String response = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Server response: " + response);

        // Close the socket
        socket.close();
    }
}