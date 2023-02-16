package Task1;

import java.io.*;
import java.net.*;
import java.nio.*;
public class AuthDnsServer extends DnsMessage {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
        InetAddress address = InetAddress.getByName("localhost");

        //Receiving domain name from client

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

        System.out.println("identification: " + identification);
        System.out.println("flags: " + flags);
        System.out.println("numQuestions: " + numQuestions);
        System.out.println("numAnswerRRs: " + numAnswerRRs);
        System.out.println("numAuthorityRRs: " + numAuthorityRRs);
        System.out.println("numAdditionalRRs: " + numAdditionalRRs);
        System.out.println("Message Length: " + messageLength);
        System.out.println("Message: " + message);
        //Sending IP address to client

        byte[] sendData;

        String IP = message;
        byte[] messageBytes2 = IP.getBytes();
        int messageLength2 = messageBytes2.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength2);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength2);
        buffer.put(messageBytes2);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 1234);
        socket.send(sendPacket);

        socket.close();

    }
}