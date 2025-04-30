import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        final int PORT = 55000;

        try{
            ServerSocket server = new ServerSocket(PORT);
            Socket client = null;
            System.err.println("Server is waiting in PORT "+ PORT);

            client = server.accept();
            System.err.println("New client Connected...");

            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

            
            while(true){
                Date date = new Date();
                String msg = dis.readUTF();
                System.err.println("Client: " + msg);
                if (msg.equalsIgnoreCase("Over") || msg.equals("")) {
                    System.err.println("Client disconnected..");
                    break;
                }
                dos.writeUTF(date.toString());
                dos.flush();


            }
            dis.close();
            dos.close();
            client.close();
            server.close();

        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
