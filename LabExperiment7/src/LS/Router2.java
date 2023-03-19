package LS;

import java.net.*;
import java.io.*;

public class Router2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Create socket and set listening port
        DatagramSocket socket = new DatagramSocket(5000);

        // Create a buffer to receive the data
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Receive the data
        socket.receive(receivePacket);

        // Convert the bytes to an array
        ByteArrayInputStream byteStream = new ByteArrayInputStream(receiveData);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        int[] data = (int[]) objStream.readObject();

        // Print the received data
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }

        // Close the socket
        socket.close();
    }
}
