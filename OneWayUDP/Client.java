import java.util.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
        final int PORT = 50000;

        try( DatagramSocket client = new DatagramSocket()){
            System.out.println("Client connected...");
            Scanner sc  = new Scanner(System.in);
            DatagramPacket dp = null;
            
            while(true){
                byte[] msg = new byte[1024];
                System.err.print("Enter msg:");
                String m = sc.nextLine();
                msg= m.getBytes();
                InetAddress ip = InetAddress.getByName("localhost");
                dp = new DatagramPacket(msg,msg.length, ip ,PORT);
                client.send(dp);

                if(m.equalsIgnoreCase("over")){
                    System.err.println("Client is Disconnecting....");
                    break;
                }


            }
            sc.close();
            client.close();
        }
    }
}
