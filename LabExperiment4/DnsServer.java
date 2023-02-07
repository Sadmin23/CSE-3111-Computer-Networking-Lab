import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class DnsServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(53);
        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        byte[] headerData = new byte[12];
        System.arraycopy(receiveData, 0, headerData, 0, 12);

        ByteBuffer buffer = ByteBuffer.wrap(headerData);
        int identification = buffer.getShort() & 0xFFFF;
        int flags = buffer.getShort() & 0xFFFF;
        int questions = buffer.getShort() & 0xFFFF;
        int answerRRs = buffer.getShort() & 0xFFFF;
        int authorityRRs = buffer.getShort() & 0xFFFF;
        int additionalRRs = buffer.getShort() & 0xFFFF;

        System.out.println("Identification: " + identification);
        System.out.println("Flags: " + flags);
        System.out.println("No. of Questions: " + questions);
        System.out.println("No. of Answer RRs: " + answerRRs);
        System.out.println("No. of Authority RRs: " + authorityRRs);
        System.out.println("No. of Additional RRs: " + additionalRRs);

        // Add your logic for processing the header data and constructing a response
        // here
        DatagramPacket receivePacket2 = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket2);

        byte[] received = receivePacket2.getData();

        ByteBuffer buffer2 = ByteBuffer.wrap(received);
        int len1 = buffer2.getInt();
        byte[] strBytes1 = new byte[len1];
        buffer2.get(strBytes1, 0, len1);

        // int len2 = buffer2.getInt();
        // byte[] strBytes2 = new byte[len2];
        // buffer2.get(strBytes2, 0, len2);

        String str1Received = new String(strBytes1);
        // String str2Received = new String(strBytes2);

        System.out.println(str1Received + ".");
        // System.out.println(str2Received + ".");

        socket.close();
    }
}
