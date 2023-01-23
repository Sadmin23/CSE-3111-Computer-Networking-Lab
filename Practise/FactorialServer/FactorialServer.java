package Practise.FactorialServer;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class FactorialServer {
    public static void main(String[] args) {

        try {
            System.out.println("Waiting for clients...");
            ServerSocket ss = new ServerSocket(9806);
            Socket soc = ss.accept();
            System.out.println("Connection established");
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            int num = Integer.parseInt(in.readLine());
            num = calculateFactorial(num);
            // String str = in.readLine();
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println("Ans: " + num);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int calculateFactorial(int x) {

        int fact = 1;

        for (int i = 1; i <= x; i++)
            fact *= i;

        return fact;
    }
}
