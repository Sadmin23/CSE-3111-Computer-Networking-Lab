import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client1a {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Client Started...");
        Socket socket = new Socket("10.33.2.75", 5000);
        System.out.println("Client Connected...");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a sms:");
        String sms = scanner.nextLine();

        objectOutputStream.writeObject(sms);

        try {
            Object fromServer = objectInputStream.readObject();
            System.out.println("From server " + (String) fromServer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}