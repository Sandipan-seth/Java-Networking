import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 50000;

    public static void main(String[] args) {
        System.out.println("[STARTING] Echo Server is starting...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[LISTENING] Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[NEW CONNECTION] Connected to " + clientSocket.getInetAddress());

                ClientHandler thread = new ClientHandler(clientSocket);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream())); 
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                String inputLine;
                while ((inputLine = dis.readUTF()) != null) {
                    System.out.println("[RECEIVED] " + inputLine + " from " + clientSocket.getRemoteSocketAddress());
                    dos.writeUTF(inputLine);
                    dos.flush();
                }

            } catch (IOException e) {
                System.err.println("[ERROR] Client handler exception: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("[DISCONNECTED] " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.err.println("[ERROR] Couldn't close client socket: " + e.getMessage());
                }
            }
        }
    }
}
