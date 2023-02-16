package Task3;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class AuthDnsServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9000);
        InetAddress address = InetAddress.getByName("localhost");

        Map<String, String> A = new HashMap<>();
        Map<String, String> AAAA = new HashMap<>();
        Map<String, String> CNAME = new HashMap<>();
        Map<String, String> MX = new HashMap<>();
        Map<String, String> NS = new HashMap<>();

        A.put("cse.du.ac.bd.", "192.0.2.1");
        A.put("ns1.cse.du.ac.bd.", "192.0.2.2");
        A.put("ns2.cse.du.ac.bd.", "192.0.2.3");
        A.put("mail.cse.du.ac.bd.", "192.0.2.4");

        AAAA.put("cse.du.ac.bd.", "2001:db8::1");
        AAAA.put("ns1.cse.du.ac.bd.", "2001:db8::2");
        AAAA.put("ns2.cse.du.ac.bd.", "2001:db8::3");
        AAAA.put("mail.cse.du.ac.bd.", "2001:db8::4");

        CNAME.put("www.cse.du.ac.bd.", "cse.du.ac.bd.");

        MX.put("cse.du.ac.bd.", "10 mail.cse.du.ac.bd.");

        NS.put("cse.du.ac.bd.", "ns1.cse.du.ac.bd.");

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
        String domain = new String(messageBytes);
        System.out.println("Receiving from TLD DNS: " + domain);

        // Sending message from Local DNS Server

        byte[] sendData;

        String IP = "1.0.2.1";
        messageBytes = IP.getBytes();
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

        System.out.println("Sending to TLD DNS: " + IP);

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9800);
        socket.send(sendPacket);

        socket.close();

    }

}
