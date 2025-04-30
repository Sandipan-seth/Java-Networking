import java.io.*;
import java.util.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final int PORT = 55000;
        try {
            Socket client = new Socket("127.0.0.1", PORT);
            System.err.println("Client is now connected to Server on PORT " + PORT);

            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

            Scanner sc = new Scanner(System.in);


            while(true){
                System.err.print("Enter msg: ");
                String msg = sc.nextLine();
                dos.writeUTF(msg);

                dos.flush();

                if(msg.equalsIgnoreCase("Over")|| msg.equals("")){
                    System.err.println("Client is disconnecting..");
                    break;
                }

                String reply = dis.readUTF();
                System.err.println("Server: "+reply);

            }

            dis.close();
            dos.close();
            sc.close();
            client.close();

        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
