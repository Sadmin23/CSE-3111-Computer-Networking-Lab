package Task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
public class AuthDnsServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String message = new String(messageBytes);

        InetAddress address = InetAddress.getByName("localhost");

        byte[] sendData;

        // Header
        int queryId2 = 1234;
        byte queryType2 = 1;
        byte queryClass2 = 1;

        // String
        String message2 = "0.0.0.0";
        byte[] messageBytes2 = message2.getBytes();
        int messageLength2 = messageBytes2.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength2);
        buffer.putShort((short) queryId2);
        buffer.put(queryType2);
        buffer.put(queryClass2);
        buffer.putInt(messageLength2);
        buffer.put(messageBytes2);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 1234);
        socket.send(sendPacket);

        socket.close();

    }
}