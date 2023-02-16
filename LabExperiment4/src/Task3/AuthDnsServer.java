package Task3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class AuthDnsServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9000);
        InetAddress address = InetAddress.getByName("localhost");

        // Receiving message from TLD DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);
        System.out.println("Received from TLD DNS: " + domain);

        // Sending message to TLD DNS Server

        byte[] sendData;

        String IP = "1.1.1.1";
        messageBytes = IP.getBytes();
        messageLength = messageBytes.length;

        System.out.println("Sending to TLD DNS: " + IP);

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9800);
        socket.send(sendPacket);

        socket.close();

    }

}
