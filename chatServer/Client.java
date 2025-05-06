import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 9876;
    private static final String SERVER_ADDRESS = "localhost";

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            Scanner scanner = new Scanner(System.in);

            Thread receiverThread = new Thread(() -> {
                byte[] receiveBuffer = new byte[1024];
                while (true) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,
                                receiveBuffer.length);
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0,
                                receivePacket.getLength());
                        System.out.println("Server: " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            });

            receiverThread.start();

            while (true) {
                System.out.print("Client: ");
                String message = scanner.nextLine();
                byte[] sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer,
                        sendBuffer.length, serverAddress, SERVER_PORT);
                socket.send(sendPacket);

                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Disconnected from server.");
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}