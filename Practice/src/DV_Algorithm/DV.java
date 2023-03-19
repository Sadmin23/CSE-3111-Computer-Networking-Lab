package DV_Algorithm;

import java.util.*;

public class DV {

    private static final int MAX = Integer.MAX_VALUE;

    public static int getValue(int x, int y) {
        if ((x == 0 && y == 1) || (x == 1 && y == 0)) {
            return 2;
        } else if ((x == 0 && y == 2) || (x == 2 && y == 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean compareSubArrays(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        for (int k = 0; k < A[0][0].length; k++) {
            if (A[i1][j1][k] != B[i2][j2][k]) {
                return false;
            }
        }
        return true;
    }

    public static void copyArrayt(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        for (int i = 0; i < A[i1][j1].length; i++) {
            B[i2][j2][i] = A[i1][j1][i];
        }
    }

    public static void updateArray(int[][][] A, int[][] D, int x) {

        for (int y = 0; y < 3; y++) {

            int v = getValue(x, y);

            A[x][x][y] = Math.min(D[x][y] + A[x][y][y], D[x][v] + A[x][v][y]);
        }
    }

    public static void main(String[] args) {

        int[][] D = {
                { 0, 2, 7, },
                { 2, 0, 1 },
                { 7, 1, 0 }

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

            for (int a = 0; a < 4; a++) {
                for (int b = 0; b < 4; b++) {
                    for (int c = 0; c < 4; c++) {

                        int x = Router[a][b][c];

                        if (x == MAX)
                            System.out.print("∞ ");
                        else
                            System.out.print(Router[a][b][c] + " ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
            }

            for (int j = 0; j < send[xyz].size(); j++) {
                for (int k = 0; k < adj[xyz].size(); k++) {

                    int x = send[xyz].get(j);
                    int y = adj[xyz].get(k);

                    queue.add(y);

                    System.arraycopy(Router[xyz][x], 0, Router[y][x], 0, 4);

                    if (!send[y].contains(x))
                        send[y].add(x);
                }
            }

            i++;
        }

        System.out.println("I= " + i + "\n");

        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                for (int c = 0; c < 4; c++) {

                    int x = Router[a][b][c];

                    if (x == MAX)
                        System.out.print("∞ ");
                    else
                        System.out.print(Router[a][b][c] + " ");
                }
                System.out.println();
            }
            System.out.println("\n");
        }

        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 4; j++) {
        // if (i != j) {
        // if (compareSubArrays(Router, Router, j, i, i, i))
        // continue;
        // else {
        // System.arraycopy(Router[i][i], 0, Router[j][i], 0, 3);
        // }
        // }
        // }
        // }

    }

}
