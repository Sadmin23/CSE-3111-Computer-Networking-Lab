package DV_Algorithm;

import java.io.*;
import java.net.*;

public class Router3 {

    private static final int MAX = Integer.MAX_VALUE;

    public static void print3DArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int x = arr[i][j];

                if (x == MAX)
                    System.out.print("∞ ");
                else
                    System.out.print(x + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket socket = new DatagramSocket(7000);

        while (true) {

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            // System.out.println("Server 1 is listening on port ");

            socket.receive(packet);
            // System.out.println("Server 1 has received a packet from " +
            // packet.getAddress().getHostName());

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream in = new ObjectInputStream(bais);
            int[][] array = (int[][]) in.readObject();

            print3DArray(array);
        }

    }
}