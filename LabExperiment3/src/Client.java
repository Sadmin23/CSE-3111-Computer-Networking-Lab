import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        System.out.println("Client Started....");
        Socket socket = new Socket("127.0.0.1",5000);
        System.out.println("Client Connected....");

//        DataOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
          DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
          DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        Scanner scanner = new Scanner(System.in);

        String sms = scanner.nextLine();
        dataOutputStream.writeUTF(sms);

        try {
            Object fromServer = dataInputStream.read();
            System.out.println("From Server " + fromServer.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}