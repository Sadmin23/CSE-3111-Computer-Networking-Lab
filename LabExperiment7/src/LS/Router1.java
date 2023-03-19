package LS;

import java.net.*;
import java.io.*;

public class Router1 {
    public static void main(String[] args) throws IOException {
        // Create socket and set destination address and port
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        int port = 5000;

        // Create an array to send
        int[] data = { 1 };

        // Convert the array to bytes
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(data);
        objStream.flush();
        byte[] sendData = byteStream.toByteArray();

        // Create a packet and send the data
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(sendPacket);

        // Close the socket
        socket.close();
    }
}
