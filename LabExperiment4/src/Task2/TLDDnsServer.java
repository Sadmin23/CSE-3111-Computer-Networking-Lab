package Task2;

import java.io.*;
import java.net.*;
import java.nio.*;

public class TLDDnsServer {

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

        String message;

        // Receiving message from Local DNS Server

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
        message = new String(messageBytes);
        String[] strings = message.split("##");

        Name = strings[0];
        Type = strings[2];
        TTL = strings[3];
        Value = "9000";

        System.out.println("Receiving message from local DNS ...");

        message = Name + "##" + Value + "##" + Type + "##" + TTL;

        // Sending message from Local DNS Server

        byte[] sendData;

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

        identification = 1;
        flags = 1;
        numQuestions = 0;
        numAnswerRRs = 1;
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

        System.out.println("Sending message to Local DNS ...");

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

        socket.close();

    }
}
