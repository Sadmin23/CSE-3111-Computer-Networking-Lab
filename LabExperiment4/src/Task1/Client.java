package Task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        InetAddress address = InetAddress.getByName("localhost");
        byte[] sendData;

        // Header
        int queryId = 1234;
        byte queryType = 1;
        byte queryClass = 1;

        // String
        String message = "www.cse.du.ac.bd";
        System.out.println("Sendding: "+message);
        byte[] messageBytes = message.getBytes();
        int messageLength = messageBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort((short) queryId);
        buffer.put(queryType);
        buffer.put(queryClass);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);
        int queryId2 = receivedBuffer.getShort();
        byte queryType2 = receivedBuffer.get();
        byte queryClass2 = receivedBuffer.get();
        int messageLength2 = receivedBuffer.getInt();
        byte[] messageBytes2 = new byte[messageLength];
        receivedBuffer.get(messageBytes2, 0, messageLength2);
        String message2 = new String(messageBytes2);

        System.out.println("Recieved: "+ message2);

        socket.close();
    }

}
