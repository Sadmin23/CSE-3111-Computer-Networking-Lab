package task1;

import java.io.*;
import java.net.*;
import java.util.*;

public class DV2 {

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

    public static void BellmanFord(int[][][] A, int[][] D, int x) {

        for (int y = 0; y < 3; y++) {

            int v = getValue(x, y);

            A[x][x][y] = Math.min(D[x][y] + A[x][y][y], D[x][v] + A[x][v][y]);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        int[][] D = {
                { 0, 2, 7 },
                { 2, 0, 1 },
                { 7, 1, 0 }

        };

        int[][][] Router = new int[3][3][3];

        Router[0] = new int[][] {
                { 0, 2, 7 },
                { MAX, MAX, MAX },
                { MAX, MAX, MAX }
        };
        Router[1] = new int[][] {
                { MAX, MAX, MAX },
                { 2, 0, 1 },
                { MAX, MAX, MAX }
        };
        Router[2] = new int[][] {
                { MAX, MAX, MAX },
                { MAX, MAX, MAX },
                { 7, 1, 0 }
        };

        int flag;
        int iteration = 0;

        while (true) {

            System.out.println("i = " + iteration + "\n");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {

                        int x = Router[i][j][k];

                        if (x == MAX)
                            System.out.print("∞ ");
                        else
                            System.out.print(Router[i][j][k] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            flag = 1;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i != j) {
                        if (compareSubArrays(Router, Router, j, i, i, i))
                            continue;
                        else {
                            System.arraycopy(Router[i][i], 0, Router[j][i], 0, 3);
                            flag = 0;
                        }
                    }
                }
            }
            if (flag != 1) {
                for (int i = 0; i < 3; i++) {
                    BellmanFord(Router, D, i);
                }
            } else
                break;

            iteration++;
        }
    }
}