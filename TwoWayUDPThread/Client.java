import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 50000;

        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(SERVER_IP);

        
        Thread sender = new Thread(() -> {
            try (Scanner sc = new Scanner(System.in)) {
                while (true) {
                    // System.out.print("You: ");
                    String message = sc.nextLine();
                    byte[] data = message.getBytes();

                    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);
                    socket.send(packet);

                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Disconnected from server.");
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Client send error: " + e);
            }
        });

        Thread receiver = new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String reply = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(reply);
                }
            } catch (Exception e) {
                System.err.println("Client receive error (probably closed): " + e);
            }
        });

        sender.start();
        receiver.start();
    }
}
