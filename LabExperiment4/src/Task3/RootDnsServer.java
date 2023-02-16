package Task3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class RootDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(7000);
        InetAddress address = InetAddress.getByName("localhost");

        // receive message from Local DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);

        System.out.println("Received from Local DNS: " + domain);

        // send message to Root TLD DNS Server

        String IP = "0.0.1.0";

        byte[] sendData;

        System.out.println("Sending to TLD DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9800);
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

        // send message to Local DNS Server

        IP = "1.1.1.1";

        System.out.println("Sending to Root DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

    }
}