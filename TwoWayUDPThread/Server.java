import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;

public class Server{
    static volatile InetAddress clientAddress = null;
    static volatile int clientPort = -1;
    public static void main(String[] args) throws Exception {
        
            final int PORT = 50000;
            DatagramSocket server = new DatagramSocket(PORT);
            Sending send = new Sending(server);
            Receiving receiving = new Receiving(server);
            send.start();
            receiving.start();

    }
    
    static class Sending extends Thread {
        DatagramSocket server;

        public Sending(DatagramSocket server) {
            this.server = server;
        }

        @Override
        public void run() {
            try (Scanner sc = new Scanner(System.in)) {
                while (true) {
                    if (clientAddress == null || clientPort == -1) {
                        Thread.sleep(1000);
                        continue;
                    }

                    String reply = sc.nextLine();
                    byte[] rply = reply.getBytes();

                    DatagramPacket dp = new DatagramPacket(rply, rply.length, clientAddress, clientPort);
                    server.send(dp);
                }
            } catch (Exception e) {
                System.err.println("Sending error: " + e);
            }
        }
    }


    static class Receiving extends Thread {
        DatagramSocket server = null;
        public Receiving(DatagramSocket server){
            this.server = server;
        }
        @Override
        public void run() {
            try{

                while(true){
                    byte[] msg = new byte[1024];
                    DatagramPacket dp = new DatagramPacket(msg, msg.length);
                    server.receive(dp);

                    clientAddress = dp.getAddress();
                    clientPort = dp.getPort();

                    String message = new String(dp.getData(), 0);
                    System.err.println(message);
                    if(message.equalsIgnoreCase("exit")){
                        System.err.println("Client Disconnected");
                        clientAddress = null;
                        clientPort = -1;
                    }
                }

            }catch(Exception e){
                System.err.println(e);
            }


        }
    }
}

