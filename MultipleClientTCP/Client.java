import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 50000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);

                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

                Scanner scanner = new Scanner(System.in)) {
                System.out.println("[CONNECTED] Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
                System.out.println("Type messages to send. Type 'exit' to quit.");

            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("[DISCONNECTING] Exiting client.");
                    break;
                }

                dos.writeUTF(message);
                dos.flush();

            
                String response = dis.readUTF();
                
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Client exception: " + e.getMessage());
        }
    }
}
