package Task1;

import java.io.*;
import java.net.*;
import java.nio.*;

public class AuthDnsServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
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

        // Receiving domain name from client

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

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, messageLength);
        String message = new String(messageBytes);
        String[] strings = message.split("##");

        System.out.println("identification: " + identification);
        System.out.println("flags: " + flags);
        System.out.println("numQuestions: " + numQuestions);
        System.out.println("numAnswerRRs: " + numAnswerRRs);
        System.out.println("numAuthorityRRs: " + numAuthorityRRs);
        System.out.println("numAdditionalRRs: " + numAdditionalRRs);
        System.out.println("Message Length: " + messageLength);
        System.out.println("Name: " + strings[0]);
        System.out.println("Value: " + strings[1]);
        System.out.println("Type: " + strings[2]);
        System.out.println("TTL: " + strings[3]);

        // Sending IP address to client

        byte[] sendData;

        String IP = message;
        byte[] messageBytes2 = IP.getBytes();
        int messageLength2 = messageBytes2.length;

        identification = 1;
        flags = 1;
        numQuestions = 1;
        numAnswerRRs = 1;
        numAuthorityRRs = 1;
        numAdditionalRRs = 1;

        ByteBuffer buffer = ByteBuffer.allocate(24 + messageLength2);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 1234);
        socket.send(sendPacket);

        socket.close();

    }
}