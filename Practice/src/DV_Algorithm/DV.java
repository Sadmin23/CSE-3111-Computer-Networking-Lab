package DV_Algorithm;

import java.util.*;

public class DV {

    private static final int MAX = Integer.MAX_VALUE;

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

    public static int minDistance(int dist[], Boolean sptSet[]) {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < 4; v++)
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    public static void dijkstra(int graph[][], int src) {
        int dist[] = new int[4]; // The output array. dist[i] will hold the shortest distance from src to i.

        Boolean sptSet[] = new Boolean[4]; // sptSet[i] will be true if vertex i is included in shortest path tree or
                                           // shortest distance from src to i is finalized.

        for (int i = 0; i < 4; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        dist[src] = 0;

        for (int count = 0; count < 3; count++) {
            int u = minDistance(dist, sptSet);

            sptSet[u] = true;

            for (int v = 0; v < 4; v++)
                if (!sptSet[v] && graph[u][v] != Integer.MAX_VALUE && dist[u] != Integer.MAX_VALUE
                        && dist[u] + graph[u][v] < dist[v])
                    dist[v] = dist[u] + graph[u][v];
        }

    }

    public static void main(String[] args) {

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

        queue.add(0);

        int i = 0;

        while (!queue.isEmpty()) {

            int xyz = queue.remove();

            System.out.println("I: " + i + "\n");

            print3DArray(Router);

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

        dijkstra(Router[0], 0);

        print3DArray(Router);

    }
}