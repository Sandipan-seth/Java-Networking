import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws Exception {

        int PORT = 55000;
        DatagramSocket client = new DatagramSocket();
        String OsName = System.getProperty("os.name");
        String OsVersion = System.getProperty("os.version");
        String username = System.getProperty("user.name");
        String address = "localhost";

        InetAddress ip = InetAddress.getByName(address);
        String message = " OS: "+ OsName + " Version: "+ OsVersion + " UserName: "+ username + " IP: "+ip+"\n";
        byte[] buff = new byte[1024];
        buff = message.getBytes();
        DatagramPacket dp = new DatagramPacket(buff, buff.length, ip, PORT);
        client.send(dp);
        System.err.println("Log Send");
        System.err.println("Client Closed");

    }
}
