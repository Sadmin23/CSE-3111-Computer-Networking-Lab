package task3;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.*;
import java.util.*;

public class congestionClient {
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

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 5001);
        int recvBufferSize = 2;
        int windowSize = 4 * recvBufferSize;
        clientSocket.setReceiveBufferSize(recvBufferSize);

        DecimalFormat df = new DecimalFormat("#0.000");

        Stack<Integer> window = new Stack<>();

        window.push(8);
        window.push(4);
        window.push(2);
        window.push(1);

        int seqNum = 0;
        int expectedAckNum = 0;

        String data = "This is a sample test message send to the Sever to check the control algorithm.";
        int dataLen = data.length();

        long timeout = 2; // in seconds
        long StartTime = System.nanoTime();
        long startTime = System.currentTimeMillis();
        double Avg_RTT = 0.2;

        while (expectedAckNum < dataLen) {

            if (!window.empty())
                windowSize = window.pop();

            if (window.empty())
                windowSize++;

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

            // System.out.println(
            // "RTT: " + df.format(SampleRTT) + " ms\n" +
            // "Estimated RTT: " + df.format(EstimatedRTT) + " ms\n" +
            // "Dev RTT: " + df.format(DevRTT) + " ms\n" +
            // "RTO: " + df.format(RTO) + " ms\n");

            Avg_RTT = EstimatedRTT;

            int[] result = fromHeader(ackHeader);

            int ackNum = result[1];

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
                "Total delay: " + df.format(delay) + " ms\n" +
                        "Average RTT: " + df.format(Avg_RTT) + " ms\n");

        clientSocket.close();
    }
}
