import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 9876;
    private static DatagramSocket socket;
    private static int clientCounter = 1;

    // ClientInfo holds IP and port
    static class ClientInfo {
        InetAddress address;
        int port;

        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }

    // Maps: client IP + port as string → client number
    private static final Map<String, Integer> clientNumbers = new ConcurrentHashMap<>();
    // Maps: client number → ClientInfo
    private static final Map<Integer, ClientInfo> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Chat Server started on port " + PORT);

            // Thread to handle incoming messages
            Thread receiverThread = new Thread(() -> {
                byte[] receiveBuffer = new byte[1024];

                while (true) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        socket.receive(receivePacket);

                        InetAddress address = receivePacket.getAddress();
                        int port = receivePacket.getPort();
                        String clientKey = address.toString() + ":" + port;

                        // Assign a new client number if not already known
                        if (!clientNumbers.containsKey(clientKey)) {
                            int clientNumber = clientCounter++;
                            clientNumbers.put(clientKey, clientNumber);
                            clients.put(clientNumber, new ClientInfo(address, port));
                            System.out.println("New client connected: Client " + clientNumber);
                        }

                        int clientNumber = clientNumbers.get(clientKey);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                        // Handle client disconnect
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

            // Main thread: server sends messages to all clients
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

        for (Map.Entry<Integer, ClientInfo> entry : clients.entrySet()) {
            int clientNumber = entry.getKey();
            ClientInfo client = entry.getValue();

            try {
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer,
                        sendBuffer.length,
                        client.address,
                        client.port);
                socket.send(sendPacket);
                System.out.println("Message sent to Client " + clientNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
