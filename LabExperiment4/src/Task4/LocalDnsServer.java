package Task4;

import java.io.*;
import java.net.*;
import java.nio.*;

public class LocalDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(5000);
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

        // receive message from client

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

        System.out.println("Received from client: " + message);

        // send message to Root DNS Server

        byte[] sendData;

        System.out.println("Sending to Root DNS: " + message);

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

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

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 7000);
        socket.send(sendPacket);

        // receive message from Root DNS Server

        receiveData = new byte[1024];

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        receivedBuffer = ByteBuffer.wrap(receiveData);
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
        System.out.println("Received from Root DNS: " + message);

        // send message to Root TLD DNS Server

        System.out.println("Sending to TLD DNS: " + message);

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(24 + messageLength);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        // receive message from TLD DNS Server

        receiveData = new byte[1024];

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        receivedBuffer = ByteBuffer.wrap(receiveData);

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
        System.out.println("Received from TLD DNS: " + message);

        // send message to Root Auth DNS Server

        System.out.println("Sending to Auth DNS: " + message);

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(24 + messageLength);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 9000);
        socket.send(sendPacket);

        // receive message from Auth DNS Server

        receiveData = new byte[1024];

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        receivedBuffer = ByteBuffer.wrap(receiveData);

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
        System.out.println("Received from TLD DNS: " + message);

        // send message to client

        System.out.println("Sending to Client: " + message);

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

        buffer = ByteBuffer.allocate(24 + messageLength);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);
        buffer.putInt(messageLength);
        buffer.put(messageBytes);
        buffer.putInt(messageLength);

        sendData = buffer.array();

        sendPacket = new DatagramPacket(sendData, sendData.length, address, 1234);
        socket.send(sendPacket);

        socket.close();
    }

}
