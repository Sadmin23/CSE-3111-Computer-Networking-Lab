import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Math;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Client Started...");
        // Socket socket = new Socket("10.33.2.75", 5000);

        Socket socket = new Socket("localhost", 5000);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Client Connected...");

        System.out.println("Enter your username:");
        String name = scanner.nextLine();
        System.out.println("Enter your password:");
        String pass = scanner.nextLine();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        objectOutputStream.writeObject(name);
        objectOutputStream.writeObject(pass);

        Object fromServer1 = objectInputStream.readObject();

        if ((boolean) fromServer1 == true) {
            System.out.println("\nLogin Successful...");

            String str;
            int val;

            while (true) {

                System.out.println("\nChoose Option please:\n");
                System.out.println("Press b to check balance");
                System.out.println("Press c to Credit balance");
                System.out.println("Press d to Debit balance");
                System.out.println("Press q to Log Out\n");

                str = scanner.nextLine();
                objectOutputStream.writeObject(str);

                while (true) {
                    if (error() == true)
                        break;
                }

                if (str.equals("q")) {

                    Object fromServer = objectInputStream.readObject();
                    System.out.println("\n" + fromServer);

                    break;
                }
                try {
                    Object fromServer = objectInputStream.readObject();
                    System.out.println("\n" + fromServer);

                    if (str.equals("c") || str.equals("d")) {

                        val = scanner.nextInt();
                        scanner.nextLine();
                        objectOutputStream.writeObject(val);

                        while (true) {
                            if (error() == true)
                                break;
                        }

                        try {
                            Object fromServer2 = objectInputStream.readObject();
                            System.out.println("\n" + fromServer2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else {
            System.out.println("Login Failed! Try again...");
            System.exit(0);
        }
    }

    static boolean error() {

        int num = (int) Math.floor(Math.random() * (100));

        // int num = (int) Math.random() * 100;

        // System.out.println(num);

        if (num < 50) {
            System.out.println("\nData packets sent successfully to the Server...\n");
            return true;
        } else {
            System.out.println("\nData packets not sent to the server\nResending packets...\n");
            return false;
        }
    }
}