package Task4;

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
        String[] strings;
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
        message = new String(messageBytes);
        strings = message.split("##");

        Name = strings[0];
        Value = strings[1];
        Type = strings[2];
        TTL = strings[3];

        switch (Type) {
            case "A":
                Value = A.get(Name);
                break;
            case "AAAA":
                Value = AAAA.get(Name);
                break;
            case "CNAME":
                Value = CNAME.get(Name);
                break;
            case "MX":
                Value = MX.get(Name);
                break;
            case "NS":
                Value = NS.get(Name);
                break;
            default:
                Value = "Requested data not found";
                break;
        }

        System.out.println("identification: " + identification);
        System.out.println("flags: " + flags);
        System.out.println("numQuestions: " + numQuestions);
        System.out.println("numAnswerRRs: " + numAnswerRRs);
        System.out.println("numAuthorityRRs: " + numAuthorityRRs);
        System.out.println("numAdditionalRRs: " + numAdditionalRRs);
        System.out.println("Message Length: " + messageLength);
        System.out.println("Name: " + Name);
        System.out.println("Type: " + Type);
        System.out.println("TTL: " + TTL);

        // Sending IP address to client

        byte[] sendData;

        message = Name + "##" + Value + "##" + Type + "##" + TTL;

        messageBytes = message.getBytes();
        messageLength = messageBytes.length;

        identification = 1;
        flags = 1;
        numQuestions = 1;
        numAnswerRRs = 1;
        numAuthorityRRs = 1;
        numAdditionalRRs = 1;

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

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

        socket.close();

    }
}