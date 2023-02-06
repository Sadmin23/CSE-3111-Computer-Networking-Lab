package LabExperiment4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class DnsServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(53);
        byte[] receiveData = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Extract the header data from the received packet
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

            socket.close();
        }
    }
}
