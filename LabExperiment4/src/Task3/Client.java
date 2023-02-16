package Task3;

import java.io.*;
import java.net.*;
import java.nio.*;

public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        InetAddress address = InetAddress.getByName("localhost");

        // Sending Domain to Local DNS Server

        byte[] sendData;

        String domain = "www.cse.du.ac.bd";
        System.out.println("Sending domain: " + domain);
        byte[] messageBytes = domain.getBytes();
        int messageLength = messageBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

        // Receiving IP from the Local DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);
        int queryId2 = receivedBuffer.getShort();
        byte queryType2 = receivedBuffer.get();
        byte queryClass2 = receivedBuffer.get();
        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, messageLength);
        String IP = new String(messageBytes);

        System.out.println("Received IP: " + IP);

        socket.close();
    }

}
