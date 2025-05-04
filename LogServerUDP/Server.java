import java.io.FileWriter;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        final int PORT = 55000;
        try {
            DatagramSocket server = new DatagramSocket(PORT);
            String path = "C:\\Users\\SANDIPAN SETH\\Desktop\\java\\LogServerUDP\\Log.txt";
            FileWriter fw;
            while (true) {

                fw = new FileWriter(path, true);
                byte[] buff = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buff, buff.length);
                server.receive(dp);

                String message = new String(dp.getData(), 0, dp.getLength());
                System.err.println(message);

                Date date = new Date();

                String log = message + "Time: " + date.toString() + " Port: " + dp.getPort() + " Sender IP: "
                        + dp.getAddress();

                fw.append(log);
                fw.close();

            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
