package SimpleClientServer;

import java.net.Socket;

public class client {
    public static void main(String[] args) {

        try {
            System.out.println("Client started");
            Socket soc = new Socket("localhost", 9806);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
