import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int PORT = 50000;
   public static void main(String[] args) throws Exception{
        ServerSocket server  =  new ServerSocket(PORT);
        System.err.println("Server created....");
        Socket client= new Socket();
        client = server.accept();

        DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        while(true){
            String msg = dis.readUTF();
            System.err.println("Stuffed Data:"+msg);
            int cnt =0 ;
            String data ="";
            for(int i=8; i<msg.length()-8; i++){
                char ch = msg.charAt(i);
                if(ch=='1'){
                    cnt++;
                    data = data+ch;
                    if(cnt==5){

                        i++;
                        cnt=0;
                    }
                }
                else{
                    data = data+ch;
                    cnt =0 ;
                }
            }
            System.out.println("Original data: "+data);
        }

   } 
}
