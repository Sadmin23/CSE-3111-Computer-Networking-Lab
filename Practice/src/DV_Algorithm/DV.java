package DV_Algorithm;

public class DV {

    private static final int MAX = Integer.MAX_VALUE;

    public boolean compareSubRouterays(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        for (int k = 0; k < A[0][0].length; k++) {
            if (A[i1][j1][k] != B[i2][j2][k]) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareSubArrays(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        for (int k = 0; k < A[0][0].length; k++) {
            if (A[i1][j1][k] != B[i2][j2][k]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int[][][] Router = new int[3][3][3];

        Router[0] = new int[][] {
                { 0, 2, 7 },
                { MAX, MAX, MAX },
                { MAX, MAX, MAX }
        };
        Router[1] = new int[][] {
                { MAX, MAX, MAX },
                { 0, 2, 7 },
                { MAX, MAX, MAX }
        };
        Router[2] = new int[][] {
                { MAX, MAX, MAX },
                { MAX, MAX, MAX },
                { 7, 1, 0 }
        };

        System.out.println(compareSubArrays(Router, Router, 0, 0, 1, 1));

        for (int i = 0; i < Router.length; i++) {
            for (int j = 0; j < Router[i].length; j++) {
                for (int k = 0; k < Router[i][j].length; k++) {
                    System.out.print(Router[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

    }

}
