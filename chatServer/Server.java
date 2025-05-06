import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private static final int PORT = 9876;
    private static DatagramSocket socket;
    private static int clientCounter = 1;
    private static Map<String, Integer> clientNumbers = new HashMap<>();
    private static Map<Integer, InetSocketAddress> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Chat Server started on port " + PORT);

            Thread receiverThread = new Thread(() -> {
                byte[] receiveBuffer = new byte[1024];

                while (true) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,
                                receiveBuffer.length);
                        socket.receive(receivePacket);

                        InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(),
                                receivePacket.getPort());
                        String clientKey = clientAddress.toString();

                        // Assign a client number if it's a new client
                        if (!clientNumbers.containsKey(clientKey)) {
                            clientNumbers.put(clientKey, clientCounter);
                            clients.put(clientCounter, clientAddress);
                            System.out.println("New client connected: Client " + clientCounter);
                            clientCounter++;
                        }

                        int clientNumber = clientNumbers.get(clientKey);
                        String message = new String(receivePacket.getData(), 0,
                                receivePacket.getLength());

                        if (message.equalsIgnoreCase("bye")) {
                            System.out.println("Client " + clientNumber + " disconnected.");
                            clients.remove(clientNumber);
                            clientNumbers.remove(clientKey);
                            continue;
                        }

                        System.out.println("Client " + clientNumber + ": " + message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            receiverThread.start();

            // Server's message sender
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Server: ");
                String serverMessage = scanner.nextLine();
                sendMessageToAllClients(serverMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToAllClients(String message) {
        byte[] sendBuffer = message.getBytes();
        for (Map.Entry<Integer, InetSocketAddress> entry : clients.entrySet()) {
            int clientNumber = entry.getKey();
            InetSocketAddress clientAddress = entry.getValue();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer,
                        sendBuffer.length, clientAddress.getAddress(), clientAddress.getPort());
                socket.send(sendPacket);
                System.out.println("Message sent to Client " + clientNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}