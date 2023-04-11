package task2;

import java.io.*;
import java.net.*;
import java.util.*;

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
        System.out.println();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket socket = new DatagramSocket(9000);

        while (true) {

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream in = new ObjectInputStream(bais);
            int[][] array = (int[][]) in.readObject();

            print3DArray(array);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            socket.receive(packet);
            String string = new String(packet.getData(), 0, packet.getLength());
            int number = Integer.parseInt(string);

            if (number == 3)
                System.out.println("\nSending data from Router 3 -> Router 1 & Router 2\n");
            else if (number == 5)
                System.out.println("Bellman Ford running\n");
            else if (number == 0) {
                socket.close();
                in.close();
                break;
            }
        }
    }
}