import java.net.*;
import java.io.*;
public class Server {
    public static void main(String[] args) throws Exception {
        final int PORT = 5000;
        ServerSocket server = new ServerSocket(PORT);
        System.err.println("Server is waiting in port "+PORT);
        Socket client = null;
        client = server.accept();
        System.err.println("New Client Accepted.....");

        DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));


        while(true){
            String msg = "";
            msg = dis.readUTF();
            System.err.println("Client: "+msg);
            if(msg.equalsIgnoreCase("over")|| msg.equals("")){
                System.err.println("Client Disconnected");
                break;
            }
        }

        client.close();
        dis.close();
        server.close();

    }
}