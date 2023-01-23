import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Server2 {
    String user;
    String password;
    int balance;
    int req_id;

    Server2(String user, String password, int balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getReq_id() {
        return this.req_id;
    }

    public void setReq_id() {
        this.req_id = getReq_id() + 1;
    }

    public String checkBalance() {
        return "Your current balance is: " + getBalance() + " taka";
    }

    public void credit(int value) {
        setBalance(getBalance() + value);
    }

    public boolean debit(int value) {
        if (getBalance() >= value) {
            setBalance(getBalance() - value);
            return true;
        } else
            return false;
    }

    public static void main(String args[]) throws IOException {
        int userNo = -1;

        Server2[] users;

        users = new Server2[3];

        users[0] = new Server2("Rahim", "1234", 50000);
        users[1] = new Server2("Karim", "1234", 60000);
        users[2] = new Server2("Rafiq", "1234", 40000);

        System.out.println("Server started");
        System.out.println("Waiting for Clients...");
        ServerSocket serverSocket = new ServerSocket(5000);

        Socket socket = serverSocket.accept();
        System.out.println("Client Accepted");
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        try {
            Object cMsg1 = ois.readObject();
            Object cMsg2 = ois.readObject();

            String Name = (String) cMsg1;
            String Pass = (String) cMsg2;

            for (int i = 0; i < 3; i++) {
                if (Name.equals(users[i].user) && Pass.equals(users[i].password)) {
                    oos.writeObject(true);
                    userNo = i;
                    break;
                } else
                    oos.writeObject(false);
            }
            while (true) {
                Object cMsg3 = ois.readObject();
                String command = (String) cMsg3;

                // Object cMsg4 = ois.readObject();
                // int value = (int) cMsg4;

                if (userNo >= 0) {
                    if (command.equals("c")) {

                        sendPackets();

                        oos.writeObject("Enter amount to be credited:\n");

                        Object cMsg4 = ois.readObject();
                        int value = (int) cMsg4;

                        users[userNo].credit(value);

                        sendPackets();

                        oos.writeObject("Your account has been credited by " + value + " taka\n"
                                + users[userNo].checkBalance());

                    } else if (command.equals("d")) {

                        sendPackets();

                        oos.writeObject("Enter amount to be debited:\n");

                        Object cMsg4 = ois.readObject();
                        int value = (int) cMsg4;

                        sendPackets();

                        if (users[userNo].debit(value) == true)
                            oos.writeObject("Your account has been debited by " + value + " taka\n"
                                    + users[userNo].checkBalance());
                        else
                            oos.writeObject("Insufficient Balance\n" + users[userNo].checkBalance());
                    } else if (command.equals("q")) {

                        sendPackets();

                        oos.writeObject("Log Out Successful...\n");

                        System.out.println("System shutting down...\n");

                        break;
                    } else if (command.equals("b")) {

                        sendPackets();

                        oos.writeObject(users[userNo].checkBalance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean error() {

        int num = (int) Math.floor(Math.random() * (100));

        if (num < 50) {
            System.out.println("\nData packets sent successfully to the Client...\n");
            return true;
        } else {
            System.out.println("\nData packets not sent to the Client\nResending packets...\n");
            return false;
        }
    }

    static void sendPackets() {
        while (true) {
            if (error() == true)
                break;
        }
    }
}