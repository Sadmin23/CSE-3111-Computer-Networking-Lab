package Task1;

import java.io.*;
import java.net.*;
import java.nio.*;

public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        InetAddress address = InetAddress.getByName("localhost");

        // Sending Domain to Auth DNS Server

        byte[] sendData;

        String domain = "www.cse.du.ac.bd";
        System.out.println("Sending: " + domain);
        byte[] messageBytes = domain.getBytes();
        int messageLength = messageBytes.length;

        short identification = 1;
        short flags = 1;
        short numQuestions = 1;
        short numAnswerRRs = 1;
        short numAuthorityRRs = 1;
        short numAdditionalRRs = 1;

        ByteBuffer buffer = ByteBuffer.allocate(24 + messageLength);
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
        identification = receivedBuffer.getShort();
        flags = receivedBuffer.getShort();
        numQuestions = receivedBuffer.getShort();
        numAnswerRRs = receivedBuffer.getShort();
        numAuthorityRRs = receivedBuffer.getShort();
        numAdditionalRRs = receivedBuffer.getShort();

        messageLength = receivedBuffer.getInt();
        messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, messageLength);
        String message = new String(messageBytes);

        System.out.println("identification: " + identification);
        System.out.println("flags: " + flags);
        System.out.println("numQuestions: " + numQuestions);
        System.out.println("numAnswerRRs: " + numAnswerRRs);
        System.out.println("numAuthorityRRs: " + numAuthorityRRs);
        System.out.println("numAdditionalRRs: " + numAdditionalRRs);
        System.out.println("Message Length: " + messageLength);
        System.out.println("Message: " + message);

        socket.close();
    }

}
