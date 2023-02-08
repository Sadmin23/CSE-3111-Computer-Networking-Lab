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

        byte[] buffer2 = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());

        String[] strings = message.split("##");

        String Name = strings[0];
        String Value = strings[1];
        String Type = strings[2];
        String TTL = strings[3];

        System.out.println("Name: " + Name);
        System.out.println("Value: " + Value);
        System.out.println("Type: " + Type);
        System.out.println("TTL: " + TTL);

        socket.close();
    }
}
