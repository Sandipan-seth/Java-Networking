import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer {
    private static final int PORT = 9876;
    private static int clientCounter = 1;

    // ClientInfo stores the socket and output stream
    static class ClientInfo {
        Socket socket;
        DataOutputStream out;

        ClientInfo(Socket socket, DataOutputStream out) {
            this.socket = socket;
            this.out = out;
        }
    }

    // Thread-safe map of clients
    private static final Map<Integer, ClientInfo> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP Chat Server started on port " + PORT);

            // Thread to accept new clients
            new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        int clientNumber = clientCounter++;

                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                        clients.put(clientNumber, new ClientInfo(clientSocket, out));
                        System.out.println("New client connected: Client " + clientNumber);

                        // Start thread to handle this client's messages
                        new Thread(() -> handleClient(clientNumber, in)).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Main thread: server sends messages
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Server: ");
                String message = scanner.nextLine();
                sendMessageToAllClients("Server: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(int clientNumber, DataInputStream in) {
        try {
            while (true) {
                String message = in.readUTF(); // read client's message
                if (message.equalsIgnoreCase("bye")) {
                    System.out.println("Client " + clientNumber + " disconnected.");
                    clients.get(clientNumber).socket.close();
                    clients.remove(clientNumber);
                    break;
                }
                System.out.println("Client " + clientNumber + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Connection to Client " + clientNumber + " lost.");
            clients.remove(clientNumber);
        }
    }

    private static void sendMessageToAllClients(String message) {
        for (Map.Entry<Integer, ClientInfo> entry : clients.entrySet()) {
            int clientNumber = entry.getKey();
            DataOutputStream out = entry.getValue().out;
            try {
                out.writeUTF(message);
                out.flush();
                System.out.println("Message sent to Client " + clientNumber);
            } catch (IOException e) {
                System.out.println("Failed to send message to Client " + clientNumber);
                e.printStackTrace();
            }
        }
    }
}
