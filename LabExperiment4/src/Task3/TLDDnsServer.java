package Task3;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TLDDnsServer {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(7000);
        InetAddress address = InetAddress.getByName("localhost");

        //receive message from Root DNS Server


        //send message to Auth DNS Server

        //receive message from Auth DNS Server


        //send message to Root DNS Server

    }
}
