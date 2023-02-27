package task2;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.*;
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

    public static boolean duplicate_Acks() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        System.out.println(randomNumber);
        if (randomNumber < 10)
            return true;
        return false;
    }

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 5000);
        int recvBufferSize = 2;
        int windowSize = 4 * recvBufferSize;
        clientSocket.setReceiveBufferSize(recvBufferSize);

        DecimalFormat df = new DecimalFormat("#0.000");

        Stack<Integer> window = new Stack<>();

        int cwnd = 1;
        int ssthrs = 8;
        int flag = 0;

        int seqNum = 0;
        int expectedAckNum = 0;

        String data = "This is a sample test message send to the Sever to check the control algorithm.";
        int dataLen = data.length();

        long timeout = 2; // in seconds
        long startTime = System.currentTimeMillis();

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
                    System.out.println("Duplicate Acks received...");
                }
            }
            long RTT_starttime = System.nanoTime();

            int sendSize = Math.min(windowSize, dataLen - expectedAckNum);

            byte[] header = toHeader(seqNum, expectedAckNum, 1, 0, sendSize);
            byte[] message = data.substring(seqNum, seqNum + sendSize).getBytes();
            byte[] segment = new byte[header.length + message.length];
            System.arraycopy(header, 0, segment, 0, header.length);
            System.arraycopy(message, 0, segment, header.length, message.length);

            clientSocket.getOutputStream().write(segment);

            byte[] ackHeader = new byte[12];
            clientSocket.getInputStream().read(ackHeader);

            double EstimatedRTT = 0.2;
            double alpha = 0.125;
            double DevRTT = 0.2;
            double beta = 0.125;

            long RTT_endtime = System.nanoTime();

            long duration = (RTT_endtime - RTT_starttime);
            double SampleRTT = (double) duration / 1_000_000.0;

            EstimatedRTT = (1 - alpha) * EstimatedRTT + alpha * SampleRTT;

            DevRTT = (1 - beta) * DevRTT + beta * (SampleRTT - EstimatedRTT);

            double RTO = EstimatedRTT + 4 * DevRTT;

            System.out.println(
                    "RTT: " + df.format(SampleRTT) + " ms\n" +
                            "Estimated RTT: " + df.format(EstimatedRTT) + " ms\n" +
                            "Dev RTT: " + df.format(DevRTT) + " ms\n" +
                            "RTO: " + df.format(RTO) + " ms\n");

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