import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;


public class Calc_Server {
    public static void main(String[] args) {
        final int port = 50000;
        try(ServerSocket server = new ServerSocket(port)){
            System.err.println("Server is Connected to server on port " + port);
            Socket client = server.accept();
            System.err.println("Client Connected...");
            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

            while(true){
                int choice = dis.readInt();
                System.err.println(choice);
                if(choice == 5){
                    System.err.println("Client Disconnected...");
                    break;
                }
                int num1 = dis.readInt();
                int num2 = dis.readInt();
                String operation ="";
                int result=0;

                switch (choice) {
                    case 1:
                        result = num1 + num2;
                        operation = num1 + " + " + num2;
                        break;
                    
                    case 2:
                        result = num1 - num2;
                        operation = num1 + " - " + num2;
                        break;
                    default:
                        break;
                }

            }
            




        }catch(Exception e){
            System.err.println(e);
        }
    }
    
}