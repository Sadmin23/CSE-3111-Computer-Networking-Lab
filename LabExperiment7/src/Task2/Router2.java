package Task2;

import java.io.*;
import java.net.*;

public class Router2 {

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
        DatagramSocket socket = new DatagramSocket(6000);

        while (true) {

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream in = new ObjectInputStream(bais);
            int[][] array = (int[][]) in.readObject();

            print3DArray(array);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            socket.receive(packet);
            String string = new String(packet.getData(), 0, packet.getLength());
            int number = Integer.parseInt(string);

            if (number == 0) {
                socket.close();
                in.close();
                break;
            }
        }

    }

}