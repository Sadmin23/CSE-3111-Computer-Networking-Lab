package Task3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class TLDDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(9800);
        InetAddress address = InetAddress.getByName("localhost");

        // receive message from Root DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);

        System.out.println("Received from Root DNS: " + domain);

        // send message to Auth DNS Server

        String IP = "1.1.0.0";

        byte[] sendData;

        System.out.println("Sending to Auth DNS: " + IP);

        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9000);
        socket.send(sendPacket);

        // receive message from Auth DNS Server

        receiveData = new byte[1024];

        DatagramPacket receivePacket2 = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket2);

        receivedBuffer = ByteBuffer.wrap(receiveData);

        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        domain = new String(messageBytes);

        System.out.println("Received from Auth DNS: " + domain);

        // send message to Root DNS Server

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

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 7000);
        socket.send(sendPacket);

    }
}
