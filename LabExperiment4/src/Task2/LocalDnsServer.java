package Task2;

import java.io.*;
import java.net.*;
import java.nio.*;

public class LocalDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(5000);
        InetAddress address = InetAddress.getByName("localhost");

        // receive message from client

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);

        System.out.println("Received from client: " + domain);

        // send message to Root DNS Server

        String IP = "0.0.0.0";

        byte[] sendData;

        System.out.println("Sending to Root DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 7000);
        socket.send(sendPacket);

        // receive message from Root DNS Server

        byte[] receiveData2 = new byte[1024];

        DatagramPacket receivePacket2 = new DatagramPacket(receiveData2, receiveData2.length);
        socket.receive(receivePacket2);

        receivedBuffer = ByteBuffer.wrap(receiveData2);

        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        domain = new String(messageBytes);
        System.out.println("Received from Root DNS: " + domain);

        // send message to Root TLD DNS Server

        IP = "0.0.0.1";

        System.out.println("Sending to TLD DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        ByteBuffer buffer2 = ByteBuffer.allocate(12 + messageLength);
        buffer2.putShort((short) 1);
        buffer2.put((byte) 2);
        buffer2.put((byte) 2);
        buffer2.putInt(messageLength);
        buffer2.put(messageBytes);

        sendData = buffer2.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        // receive message from TLD DNS Server

        receiveData = new byte[1024];

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        receivedBuffer = ByteBuffer.wrap(receiveData);

        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        domain = new String(messageBytes);
        System.out.println("Received from TLD DNS: " + domain);

        // send message to Root Auth DNS Server

        IP = "0.0.1.0";

        System.out.println("Sending to TLD DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 9000);
        socket.send(sendPacket);

        // receive message from Auth DNS Server

        receiveData = new byte[1024];

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        receivedBuffer = ByteBuffer.wrap(receiveData);

        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        domain = new String(messageBytes);
        System.out.println("Received from TLD DNS: " + domain);

        // send message to client

        IP = "0.0.1.0";

        System.out.println("Sending to Client: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 1234);
        socket.send(sendPacket);

        socket.close();
    }

}
