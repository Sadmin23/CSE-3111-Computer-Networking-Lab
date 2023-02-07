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

        String str1 = "Hello";
        String str2 = "World";

        byte[] bytes1 = str1.getBytes();
        byte[] bytes2 = str2.getBytes();

        int totalLength = bytes1.length + bytes2.length + 4; // 4 bytes for storing lengths of each string
        byte[] combined = new byte[totalLength];

        ByteBuffer buffer2 = ByteBuffer.wrap(combined);
        buffer2.putInt(bytes1.length);
        buffer2.put(bytes1);

        DatagramPacket sendPacket2 = new DatagramPacket(combined, combined.length, serverAddress, 53);
        socket.send(sendPacket2);

        socket.close();
    }
}
