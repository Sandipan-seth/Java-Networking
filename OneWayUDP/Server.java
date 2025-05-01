import java.net.*;

public class Server {
    public static void main(String[] args) throws Exception {
        final int PORT = 50000;

        try (DatagramSocket server = new DatagramSocket(PORT)) {
            System.err.println("Server is waiting on port " + PORT + "...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                server.receive(dp); 
             
                InetAddress clientAddress = dp.getAddress();
                int clientPort = dp.getPort();
                System.err.println("Client connected from " + clientAddress.getHostAddress() + ":" + clientPort);

                String message = new String(dp.getData(), 0, dp.getLength());
                System.err.println("Client: " + message);

           
                if (message.equalsIgnoreCase("over")) {
                    System.err.println("Client disconnected...");
                }
            }
        }
    }
}
