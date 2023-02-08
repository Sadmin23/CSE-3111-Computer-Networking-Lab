import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class DnsClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");

        DnsMessage packet = new DnsMessage((short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0);

        // Construct the header data for the DNS message
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putShort((short) packet.identification);
        buffer.putShort((short) packet.flags);
        buffer.putShort((short) packet.numQuestions);
        buffer.putShort((short) packet.numAnswerRRs);
        buffer.putShort((short) packet.numAuthorityRRs);
        buffer.putShort((short) packet.numAdditionalRRs);

        byte[] headerData = buffer.array();

        // Construct the message body with additional data if needed
        byte[] message = headerData;

        DatagramPacket sendPacket = new DatagramPacket(message, message.length, serverAddress, 53);
        socket.send(sendPacket);

        String Name = "cse.du.ac.bd";
        String Value = "ns1.cse.du.ac.bd.";
        String Type = "NS";
        String TTL = "86400";

        String message2 = Name + "##" + Value + "##" + Type + "##" + TTL;

        byte[] buffer2 = message2.getBytes();
        InetAddress address = InetAddress.getByName("localhost");
        int port = 53;
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, address, port);
        socket.send(packet2);
    }
}
