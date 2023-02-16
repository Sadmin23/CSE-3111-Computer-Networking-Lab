package Task1;

import java.io.*;
import java.net.*;
import java.nio.*;
public class Client extends DnsMessage {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        InetAddress address = InetAddress.getByName("localhost");

        // Sending Domain to Auth DNS Server

        byte[] sendData;

        String domain = "www.cse.du.ac.bd";
        System.out.println("Sending: " + domain);
        byte[] messageBytes = domain.getBytes();
        int messageLength = messageBytes.length;

        identification=1;
        flags=1;
        numQuestions=1;
        numAnswerRRs=1;
        numAuthorityRRs=1;
        numAdditionalRRs=1;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        // Receiving IP from the Auth DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);
        int queryId = receivedBuffer.getShort();
        byte queryType = receivedBuffer.get();
        byte queryClass = receivedBuffer.get();
        int messageLength2 = receivedBuffer.getInt();
        byte[] messageBytes2 = new byte[messageLength];
        receivedBuffer.get(messageBytes2, 0, messageLength);
        String message = new String(messageBytes2);

        System.out.println("Query ID: " + queryId);
        System.out.println("Query Type: " + queryType);
        System.out.println("Query Class: " + queryClass);
        System.out.println("Message Length: " + messageLength2);
        System.out.println("Message: " + message);

        socket.close();
    }

}
