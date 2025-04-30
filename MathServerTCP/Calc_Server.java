import java.io.*;
import java.net.*;


public class Calc_Server {
    public static void main(String[] args) {
        final int port = 50000;
        try(ServerSocket server = new ServerSocket(port)){
            System.err.println("Server is Connected to server on port " + port);
            Socket client = server.accept();
            System.err.println("Client Connected...");

            DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

            while(true){
                int choice = dis.readInt();
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

                    case 3:
                        result = num1 * num2;
                        operation = num1 + " * " + num2;
                        break;

                    case 4:
                        if (num2 == 0) {
                            dos.writeInt(Integer.MIN_VALUE);
                            dos.writeUTF("Division by zero is not allowed.");
                            dos.flush();
                            continue; 
                        }
                        result = num1 / num2;
                        operation = num1 + " / " + num2;
                        break;
                }

                System.out.println(operation +" = " + result);
                dos.writeInt(result);
                dos.writeUTF(operation);
                dos.flush();

            }

        }catch(Exception e){
            System.err.println(e);
        }
    }
    
}