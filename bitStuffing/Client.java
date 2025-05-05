
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 50000;
    public static void main(String[] args) throws Exception {
        Socket client = new Socket("localhost",PORT);

        DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.err.print("Enter the Data:");
            String original = sc.nextLine();
            if (original.equals("exit")) {
                System.err.println("Client Exited");
                break;
            }

            int cnt =0 ;
            int i =0;
            String data ="";
            while(i<original.length()){
                char ch = original.charAt(i);
                if(ch == '1'){
                    cnt++;
                    if(cnt<5){
                        data = data+ch;
                    }
                    else{
                        data = data+ch+"0";
                        cnt=0;
                    }
                }
                else{
                    cnt =0 ;
                    data = data + ch;
                }
                i++;
            }

            String stuffed = "01111110" + data + "01111110";
            
            dos.writeUTF(stuffed);
            dos.flush();
        }



    }
}
