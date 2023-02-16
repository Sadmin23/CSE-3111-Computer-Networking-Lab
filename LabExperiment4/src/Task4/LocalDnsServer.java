package Task4;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class LocalDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(5000);
        InetAddress address = InetAddress.getByName("localhost");

        Map<String, String> A = new HashMap<>();
        Map<String, String> AAAA = new HashMap<>();
        Map<String, String> CNAME = new HashMap<>();
        Map<String, String> MX = new HashMap<>();
        Map<String, String> NS = new HashMap<>();

        A.put("cse.du.ac.bd.", "192.0.2.1");
        A.put("ns1.cse.du.ac.bd.", "192.0.2.2");

        AAAA.put("cse.du.ac.bd.", "2001:db8::1");
        AAAA.put("ns1.cse.du.ac.bd.", "2001:db8::2");

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

        int port;

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
        strings = message.split("##");

        Name = strings[0];
        Type = strings[1];
        TTL = strings[2];

        switch (Type) {
            case "A":
                if (A.containsKey(Name))
                    Value = A.get(Name);
                else
                    Value = "Requested data not found";
                break;
            case "AAAA":
                if (AAAA.containsKey(Name))
                    Value = AAAA.get(Name);
                else
                    Value = "Requested data not found";
                break;
            case "CNAME":
                if (CNAME.containsKey(Name))
                    Value = CNAME.get(Name);
                else
                    Value = "Requested data not found";
                break;
            case "MX":
                if (MX.containsKey(Name))
                    Value = MX.get(Name);
                else
                    Value = "Requested data not found";
                break;
            case "NS":
                if (NS.containsKey(Name))
                    Value = NS.get(Name);
                else
                    Value = "Requested data not found";
                break;
            default:
                Value = "Requested data not found";
                break;
        }
        System.out.println("Value: " + Value);
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

        strings = message.split("##");

        Name = strings[0];
        Value = strings[1];
        Type = strings[2];
        TTL = strings[3];

        port = Integer.valueOf(Value);

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

        sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
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

        strings = message.split("##");

        Name = strings[0];
        Value = strings[1];
        Type = strings[2];
        TTL = strings[3];

        port = Integer.valueOf(Value);

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

        sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
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
