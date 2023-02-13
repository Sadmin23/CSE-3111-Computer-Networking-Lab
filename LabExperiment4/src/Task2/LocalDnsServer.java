package Task2;

import java.io.*;
import java.net.*;
import java.nio.*;
public class LocalDnsServer {


    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(5000);
        InetAddress address = InetAddress.getByName("localhost");

        //receive message from client

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        ByteBuffer receivedBuffer = ByteBuffer.wrap(receiveData);

        int messageLength = receivedBuffer.getInt();
        byte[] messageBytes = new byte[messageLength];
        receivedBuffer.get(messageBytes, 0, Math.min(messageLength, receivedBuffer.remaining()));
        String domain = new String(messageBytes);

        System.out.println("Received from client: " + domain);

        //send message to Root DNS Server

        String IP="0.0.0.0";

        byte[] sendData;

        System.out.println("Sending to Root DNS: " + IP);

        byte[] messageBytes2 = IP.getBytes();
        int messageLength2 = messageBytes2.length;

        ByteBuffer buffer = ByteBuffer.allocate(12 + messageLength2);
        buffer.putShort((short) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 2);
        buffer.putInt(messageLength2);
        buffer.put(messageBytes2);

        sendData = buffer.array();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 7000);
        socket.send(sendPacket);

        //receive message from Root DNS Server

        byte[] receiveData2 = new byte[1024];

        DatagramPacket receivePacket2 = new DatagramPacket(receiveData2, receiveData2.length);
        socket.receive(receivePacket2);

        ByteBuffer receivedBuffer2 = ByteBuffer.wrap(receiveData2);

        int messageLength3 = receivedBuffer2.getInt();
        byte[] messageBytes3 = new byte[messageLength3];
        receivedBuffer2.get(messageBytes3, 0, Math.min(messageLength3, receivedBuffer2.remaining()));
        String domain2 = new String(messageBytes3);
        System.out.println("Received from Root DNS: " + domain2);


        //send message to Root TLD DNS Server

        String IP2="0.1.0.1";

        byte[] sendData2;

        System.out.println("Sending to TLD DNS: " + IP2);

        byte[] messageBytes4 = IP2.getBytes();
        int messageLength4 = messageBytes4.length;

        ByteBuffer buffer2 = ByteBuffer.allocate(12 + messageLength4);
        buffer2.putShort((short) 1);
        buffer2.put((byte) 2);
        buffer2.put((byte) 2);
        buffer2.putInt(messageLength4);
        buffer2.put(messageBytes4);

        sendData2 = buffer2.array();

        DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address, 9876);
        socket.send(sendPacket2);

        //receive message from TLD DNS Server

        byte[] receiveData3 = new byte[1024];

        DatagramPacket receivePacket3 = new DatagramPacket(receiveData3, receiveData3.length);
        socket.receive(receivePacket3);

        ByteBuffer receivedBuffer3 = ByteBuffer.wrap(receiveData3);

        int messageLength5 = receivedBuffer3.getInt();
        byte[] messageBytes5 = new byte[messageLength5];
        receivedBuffer3.get(messageBytes5, 0, Math.min(messageLength5, receivedBuffer3.remaining()));
        String domain3 = new String(messageBytes5);
        System.out.println("Received from TLD DNS: " + domain3);

        //send message to Root Auth DNS Server

        //receive message from Auth DNS Server


        //send message to client

        socket.close();

    }

}
