package Task2;

import java.io.*;
import java.net.*;
import java.util.*;

public class DV {

    private static final int MAX = Integer.MAX_VALUE;

    private static int change = 1;

    public static boolean compareSubArrays(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        for (int k = 0; k < A[0][0].length; k++) {
            if (A[i1][j1][k] != B[i2][j2][k]) {
                return false;
            }
        }
        return true;
    }

    public static void print3DArray(int[][][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i][j].length; k++) {
                    int x = arr[i][j][k];

                    if (x == MAX)
                        System.out.print("∞ ");
                    else
                        System.out.print(x + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i < 4; i++) {

            int x = arr[i];

            System.out.print(x + " ");
        }
        System.out.println();
    }

    public static void dijkstra(int[] router, int[][] graph, int source) {
        int count = 4;
        boolean[] visitedVertex = new boolean[count];
        int distance[] = new int[count];

        for (int i = 0; i < count; i++) {
            visitedVertex[i] = false;
            distance[i] = MAX;
        }

        distance[source] = 0;

        for (int i = 0; i < count; i++) {

            int u = findMinDistance(distance, visitedVertex);
            visitedVertex[u] = true;

            for (int v = 0; v < count; v++) {
                if (!visitedVertex[v] && graph[u][v] != 0 && graph[u][v] != MAX
                        && (distance[u] + graph[u][v] < distance[v])) {
                    distance[v] = distance[u] + graph[u][v];
                }
            }
        }
        for (int i = 0; i < count; i++) {
            if (router[i] != distance[i]) {
                router[i] = distance[i];
                change = 1;
            }
        }
    }

    private static int findMinDistance(int[] distance, boolean[] visitedVertex) {
        int minDistance = MAX;
        int minDistanceVertex = -1;
        for (int i = 0; i < 4; i++) {
            if (!visitedVertex[i] && distance[i] < minDistance) {
                minDistance = distance[i];
                minDistanceVertex = i;
            }
        }

        return minDistanceVertex;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        long starttime = System.currentTimeMillis();

        long duration = 0;

        int[][] D = {
                { 0, 2, 7, MAX },
                { 2, 0, 1, 3 },
                { 7, 1, 0, 2 },
                { MAX, 3, 2, 0 }
        };

        int[][][] Router = new int[4][4][4];

        Router[0] = new int[][] {
                { 0, 2, 7, MAX },
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX }
        };
        Router[1] = new int[][] {
                { MAX, MAX, MAX, MAX },
                { 2, 0, 1, 3 },
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX }
        };
        Router[2] = new int[][] {
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX },
                { 7, 1, 0, 2 },
                { MAX, MAX, MAX, MAX }
        };
        Router[3] = new int[][] {
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX },
                { MAX, MAX, MAX, MAX },
                { MAX, 3, 2, 0 }
        };

        int[] port = new int[] { 5000, 6000, 7000, 8000 };

        Vector<Integer>[] adj = new Vector[4];

        adj[0] = new Vector<Integer>(Arrays.asList(1, 2));

        adj[1] = new Vector<Integer>(Arrays.asList(0, 2, 3));

        adj[2] = new Vector<Integer>(Arrays.asList(0, 1, 3));

        adj[3] = new Vector<Integer>(Arrays.asList(1, 2));

        Vector<Integer>[] send = new Vector[4];

        for (int i = 0; i < 4; i++) {
            send[i] = new Vector<Integer>();
        }

        send[0].add(0);
        send[1].add(1);
        send[2].add(2);
        send[3].add(3);

        Queue<Integer> queue = new ArrayDeque<>();
        Queue<Integer> rqueue = new ArrayDeque();

        int flag = 0;

        while (true) {

            change = 0;

            for (int i = 0; i < 4; i++) {
                send[i].clear();
            }

            send[0].add(0);
            send[1].add(1);
            send[2].add(2);
            send[3].add(3);

            queue.add(0);

            int i = 0;

            while (!queue.isEmpty()) {

                int xyz = queue.remove();

                // System.out.println("I: " + i + "\n");

                // print3DArray(Router);

                DatagramSocket socket1 = new DatagramSocket();
                InetAddress address1 = InetAddress.getByName("localhost");

                for (int ii = 0; ii < 4; ii++) {
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    ObjectOutputStream out1 = new ObjectOutputStream(baos1);
                    out1.writeObject(Router[ii]);
                    byte[] data1 = baos1.toByteArray();
                    DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address1, port[ii]);
                    socket1.send(packet1);
                    byte[] data2 = String.valueOf(1).getBytes();
                    DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address1, port[ii]);
                    socket1.send(packet2);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int j = 0; j < send[xyz].size(); j++) {
                    for (int k = 0; k < adj[xyz].size(); k++) {

                        int x = send[xyz].get(j);
                        int y = adj[xyz].get(k);

                        if (!compareSubArrays(Router, Router, xyz, x, y, x)) {
                            System.arraycopy(Router[xyz][x], 0, Router[y][x], 0, 4);
                            if (!queue.contains(y))
                                queue.add(y);
                        }

                        if (!send[y].contains(x))
                            send[y].add(x);

                    }
                }

                if (System.currentTimeMillis() - starttime >= 10000 && flag == 0) {
                    Router[1][1][3] = 2;

                    D[1][3] = 2;
                    D[2][0] = 2;

                    rqueue.add(1);

                    while (!queue.isEmpty()) {
                        int value = queue.poll();
                        if (value != 1) {
                            rqueue.add(value);
                        }
                    }

                    while (!rqueue.isEmpty()) {
                        queue.add(rqueue.poll());
                    }

                    flag = 1;
                    System.out.println("Values changed\n");
                    print3DArray(Router);
                }

                i++;
            }

            System.out.println("I= " + i + "\n");

            print3DArray(Router);

            DatagramSocket socket1 = new DatagramSocket();
            InetAddress address1 = InetAddress.getByName("localhost");

            for (int ii = 0; ii < 4; ii++) {
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ObjectOutputStream out1 = new ObjectOutputStream(baos1);
                out1.writeObject(Router[ii]);
                byte[] data1 = baos1.toByteArray();
                DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address1, port[ii]);
                socket1.send(packet1);
                byte[] data2 = String.valueOf(1).getBytes();
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address1, port[ii]);
                socket1.send(packet2);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Dijkstra started");

            for (int a = 0; a < 4; a++)
                dijkstra(Router[a][a], D, a);
            i++;

            socket1 = new DatagramSocket();
            address1 = InetAddress.getByName("localhost");

            int value;

            if (change == 1)
                value = 1;
            else
                value = 0;

            for (int ii = 0; ii < 4; ii++) {
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ObjectOutputStream out1 = new ObjectOutputStream(baos1);
                out1.writeObject(Router[ii]);
                byte[] data1 = baos1.toByteArray();
                DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address1, port[ii]);
                socket1.send(packet1);
                byte[] data2 = String.valueOf(change).getBytes();
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address1, port[ii]);
                socket1.send(packet2);
            }
        }
    }
}