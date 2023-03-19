package LS;

import java.net.*;
import java.util.*;
import java.io.*;

public class Router3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        DatagramSocket datagramSocket = new DatagramSocket(6000);

        // Create a buffer for the received data
        byte[] buffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);

        // Receive the datagram packet containing the byte array with the serialized
        // vector
        datagramSocket.receive(datagramPacket);
        System.out.println("Vector received");

        // Deserialize the byte array to a vector
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Vector<Integer> vector = (Vector<Integer>) objectInputStream.readObject();

        // Print the elements of the vector
        for (int element : vector) {
            System.out.println(element);
        }
    }
}
