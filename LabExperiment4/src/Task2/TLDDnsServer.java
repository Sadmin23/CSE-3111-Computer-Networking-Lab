package Task2;

import java.io.*;
import java.net.*;
import java.nio.*;
public class TLDDnsServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(9876);
        InetAddress address = InetAddress.getByName("localhost");

        //Receiving message from Local DNS Server

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);

        System.out.println("Receiving from local DNS: " + domain);

        //Sending message from Local DNS Server

        byte[] sendData;

        String IP = "0.0.1.0";
        byte[] messageBytes2 = IP.getBytes();
        int messageLength2 = messageBytes2.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength2);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength2);
        buffer.put(messageBytes2);

        sendData = buffer.array();

        System.out.println("Sending to Local DNS: " + IP);

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 5000);
        socket.send(sendPacket);

        socket.close();

    }
}
