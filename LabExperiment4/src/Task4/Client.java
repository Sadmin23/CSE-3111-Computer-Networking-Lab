package Task4;

import java.io.*;
import java.net.*;
import java.nio.*;

public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        InetAddress address = InetAddress.getByName("localhost");

        short identification;
        short flags;
        short numQuestions;
        short numAnswerRRs;
        short numAuthorityRRs;
        short numAdditionalRRs;

        String Name;
        String Value;
        String Type;
        String TTL;

        String message;

        // Sending Domain to Local DNS Server

        byte[] sendData;

        Name = "ns1.cse.du.ac.bd.";
        Type = "A";
        TTL = "86400";

        message = Name + "##" + Type + "##" + TTL;

        System.out.println("Sending: " + message);
        byte[] messageBytes = message.getBytes();
        int messageLength = messageBytes.length;

        identification = 1;
        flags = 1;
        numQuestions = 1;
        numAnswerRRs = 0;
        numAuthorityRRs = 0;
        numAdditionalRRs = 0;

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

        long startTime = System.nanoTime();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

        // Receiving IP from the Local DNS Server

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
        message = new String(messageBytes);
        String[] strings = message.split("##");

        Name = strings[0];
        Value = strings[1];
        Type = strings[2];
        TTL = strings[3];

        System.out.println("identification: " + identification);
        System.out.println("flags: " + flags);
        System.out.println("numQuestions: " + numQuestions);
        System.out.println("numAnswerRRs: " + numAnswerRRs);
        System.out.println("numAuthorityRRs: " + numAuthorityRRs);
        System.out.println("numAdditionalRRs: " + numAdditionalRRs);
        System.out.println("Value: " + Value);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime); // duration in nanoseconds
        double seconds = (double) duration / 1_000_000.0; // duration in milliseconds

        System.out.println("Execution time: " + seconds + " ms");

        socket.close();
    }

}
