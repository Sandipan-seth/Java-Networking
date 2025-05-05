import java.io.FileOutputStream;
import java.net.*;

public class FileClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 3000;

            String filename = "server.txt";
            byte[] sendData = filename.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);

            FileOutputStream fos = new FileOutputStream("ClientFile/received_" + filename);
            byte[] receiveBuffer = new byte[1500];

            socket.setSoTimeout(3000);

            while (true) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket);

                    String received = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    if (received.equals("File not found")) {
                        System.out.println("Server: File not found");
                        break;
                    }
                    fos.write(receivePacket.getData(), 0, receivePacket.getLength());
                } catch (SocketTimeoutException e) {
                    System.out.println("File transfer complete.");
                    break;
                }
            }

            fos.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}