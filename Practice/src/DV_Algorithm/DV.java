package DV_Algorithm;

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

        while (change != 0) {

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

                System.out.println("I: " + i + "\n");

                print3DArray(Router);

                Socket socket = new Socket("localhost", 12345);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeObject(Router[0]);
                out.writeInt(1);
                out.flush();

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

                i++;
            }

            System.out.println("I= " + i + "\n");

            print3DArray(Router);

            for (int a = 0; a < 4; a++)
                dijkstra(Router[a][a], D, a);
            i++;

            System.out.println("I= " + i + "\n");

            print3DArray(Router);
        }
    }
}