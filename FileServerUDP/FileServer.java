import java.io.File;
import java.io.FileInputStream;
import java.net.*;

class Fliehandler extends Thread {
    DatagramSocket socket;
    DatagramPacket packet;

    public Fliehandler(DatagramSocket socket, DatagramPacket packet) {
        this.packet = packet;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String filename = new String(packet.getData(), 0, packet.getLength());
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            // The "ServerFile" folder path is added here
            File file = new File("ServerFile/" + filename);

            if (!file.exists()) {
                byte[] notFound = "File not found".getBytes();
                socket.send(new DatagramPacket(notFound, notFound.length, clientAddress, clientPort));
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1500];
            int byteread;
            while ((byteread = fis.read(buffer)) != -1) {
                DatagramPacket sendPacket = new DatagramPacket(buffer, byteread, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

public class FileServer {
    public FileServer() throws Exception {
        DatagramSocket socket = new DatagramSocket(3000);
        System.out.println("Server started");

        while (true) {
            byte[] buffer = new byte[1500];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            Fliehandler obj = new Fliehandler(socket, packet);

            obj.start();
        }
    }

    public static void main(String[] args) {
        try {
            new FileServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
