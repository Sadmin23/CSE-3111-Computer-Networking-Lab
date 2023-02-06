package LabExperiment4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DnsClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");

        // Construct the header data for the DNS message
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putShort((short) 12345);
        buffer.putShort((short) 0x0100);
        buffer.putShort((short) 1);
        buffer.putShort((short) 0);
        buffer.putShort((short) 0);
        buffer.putShort((short) 0);

        byte[] headerData = buffer.array();

        // Construct the message body with additional data if needed
        byte[] message = headerData;

        DatagramPacket sendPacket = new DatagramPacket(message, message.length, serverAddress, 53);
        socket.send(sendPacket);
    }
}
