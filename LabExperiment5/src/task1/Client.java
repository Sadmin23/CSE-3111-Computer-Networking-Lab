package task1;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

public class Client {
    public static byte[] toHeader(int seqNum, int ackNum, int ack, int sf, int rwnd) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(seqNum);
        buffer.putInt(ackNum);
        buffer.put((byte) ack);
        buffer.put((byte) sf);
        buffer.putShort((short) rwnd);
        return buffer.array();
    }

    public static int[] fromHeader(byte[] segment) {
        ByteBuffer buffer = ByteBuffer.wrap(segment);
        int seqNum = buffer.getInt();
        int ackNum = buffer.getInt();
        int ack = buffer.get();
        int sf = buffer.get();
        int rwnd = buffer.getShort();
        return new int[] { seqNum, ackNum, ack, sf, rwnd };
    }

    // 1,2,4,8
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 5000);
        int recvBufferSize = 2;
        int windowSize = 1;
        clientSocket.setReceiveBufferSize(recvBufferSize);

        int seqNum = 0;

        int expectedAckNum = 0;

        String data = "This is a sample test message send to the Sever to check the control algorithm.";

        System.out.println("String to be sent: " + data);

        int dataLen = data.length();

        long timeout = 2; // in seconds
        long startTime = System.currentTimeMillis();

        while (expectedAckNum < dataLen) {
            int sendSize = Math.min(windowSize, dataLen - expectedAckNum);

            byte[] header = toHeader(seqNum, expectedAckNum, 1, 0, sendSize);
            byte[] message = data.substring(seqNum, seqNum + sendSize).getBytes();
            byte[] segment = new byte[header.length + message.length];
            System.arraycopy(header, 0, segment, 0, header.length);
            System.arraycopy(message, 0, segment, header.length, message.length);

            clientSocket.getOutputStream().write(segment);

            byte[] ackHeader = new byte[12];
            clientSocket.getInputStream().read(ackHeader);

            int[] result = fromHeader(ackHeader);

            int ackNum = result[1];

            seqNum += sendSize;
            expectedAckNum = ackNum;

            if ((System.currentTimeMillis() - startTime) > timeout * 1000) {
                seqNum = expectedAckNum;
                startTime = System.currentTimeMillis();
            }
        }

        clientSocket.close();
    }
}
