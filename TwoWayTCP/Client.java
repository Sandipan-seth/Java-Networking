import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
        final int PORT = 5000;
        Socket client = new Socket("127.0.0.1", PORT);
        System.err.println("Client is connectedf to port " + PORT);


        Scanner sc = new Scanner(System.in);

        DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        
        while (true) {
            System.err.print("Enter msg: ");
            String msg = sc.nextLine();
            dos.writeUTF(msg);
            if (msg.equalsIgnoreCase("over") || msg.equals("")) {
                System.err.println("Disconnecting...");
                break;
            }
            dos.flush();

            String reply="";
            reply = dis.readUTF();
            System.err.println("Server: "+reply);

        }
        sc.close();
        dos.close();
        client.close();
    }
}
