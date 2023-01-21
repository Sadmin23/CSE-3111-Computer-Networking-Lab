package FactorialServer;

import java.net.Socket;
import java.io.*;

public class FactorialClient {

    public static void main(String[] args) {

        try {
            System.out.println("Client started");
            Socket soc = new Socket("localhost", 9806);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a number:");
            int num = Integer.parseInt(userInput.readLine());
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println(num);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            System.out.println(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
