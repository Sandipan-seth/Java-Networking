import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;


public class Calc_Client {
    public static void main(String[] args) {
        final int port = 50000;
        
        try(Socket client = new Socket("localhost",port)){
            System.err.println("Client is Connected to server on port "+port);
            
            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            Scanner sc = new Scanner(System.in);


            while(true){
                System.out.println("\n===== MENU =====");
                System.out.println("1. Addition");
                System.out.println("2. Subtraction");
                System.out.println("3. Multiplication");
                System.out.println("4. Division");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                dos.writeInt(choice);
                dos.flush();

                if(choice == 5){
                    System.err.println("Client disconnected");
                    break;
                }

                if(choice > 5){
                    System.err.println("Enter a valid choice..");
                    continue;
                }

                System.err.print("Enter 1st number:");
                int num1 = sc.nextInt();
                System.err.print("Enter 2nd number:");
                int num2 = sc.nextInt();
                dos.writeInt(num1);
                dos.writeInt(num2);
                dos.flush();

                int result = dis.readInt();
                String operation = dis.readUTF();

                if (result == Integer.MIN_VALUE) {
                    System.err.println(operation); 
                } else {
                    System.err.println("Computed result " + operation + " = " + result);
                }

            }

            client.close();
            sc.close();
            
        }catch(Exception e){
            System.err.println(e);
        }
    }   
}
