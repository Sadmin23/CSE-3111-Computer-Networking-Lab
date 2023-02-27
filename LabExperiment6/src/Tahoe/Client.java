package Tahoe;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.text.*;

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

    public static boolean duplicate_Acks() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if (randomNumber < 20)
            return true;
        return false;
    }

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 5000);
        int recvBufferSize = 2;
        int windowSize = 4 * recvBufferSize;
        clientSocket.setReceiveBufferSize(recvBufferSize);

        DecimalFormat df = new DecimalFormat("#0.000");

        int cwnd = 1;
        int ssthrs = 8;
        int flag = 0;

        int seqNum = 0;
        int expectedAckNum = 0;

        String data = "This is a sample test message send to the Sever to check the control algorithm.";
        int dataLen = data.length();

        long timeout = 2; // in seconds
        long startTime = System.currentTimeMillis();
        long StartTime = System.nanoTime();

        while (expectedAckNum < dataLen) {

            if (cwnd <= ssthrs && flag == 0) {
                windowSize = cwnd;
                if (cwnd * 2 <= ssthrs)
                    cwnd *= 2;
                else {
                    flag = 1;
                    cwnd = ssthrs;
                }
            } else {
                if (!duplicate_Acks()) {
                    cwnd++;
                    windowSize = cwnd;
                } else {
                    ssthrs = cwnd / 2 - 1;
                    cwnd = 1;
                    flag = 0;
                    windowSize = cwnd;
                    if (cwnd * 2 <= ssthrs)
                        cwnd *= 2;
                    System.out.println("\nDuplicate Acks received...");
                }
            }

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

            System.out.println("\nSeq Num: " + seqNum + "\nWindow Size: " + windowSize);

            seqNum += sendSize;
            expectedAckNum = ackNum;

            if ((System.currentTimeMillis() - startTime) > timeout * 1000) {
                seqNum = expectedAckNum;
                startTime = System.currentTimeMillis();
            }
        }
        long endtime = System.nanoTime();

        long duration = (endtime - StartTime);
        double delay = (double) duration / 1_000_000.0;

        System.out.println(
                "\nTotal delay: " + df.format(delay) + " ms\n\n");

        clientSocket.close();
    }
}
