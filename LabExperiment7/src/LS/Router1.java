package LS;

import java.net.*;
import java.util.*;
import java.io.*;

public class Router1 {
    public static void main(String[] args) throws IOException {
        // Create socket and set destination address and port
        DatagramSocket socket = new DatagramSocket(4000);
        InetAddress address = InetAddress.getByName("localhost");
        int port = 5000;

        // Create an array to send
        Vector<Integer> data = new Vector<Integer>();
        data.add(3);

        // Convert the array to bytes
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(data);
        objStream.flush();
        byte[] sendData = byteStream.toByteArray();

        // Create a packet and send the data
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);
        DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, address, 6000);
        socket.send(sendPacket2);
        // Close the socket
        socket.close();
    }
}
