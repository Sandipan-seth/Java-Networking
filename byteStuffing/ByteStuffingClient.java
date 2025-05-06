import java.net.*;
import java.util.*;
import java.io.*;

public class ByteStuffingClient {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        // Opening a socket for establishing the connection
        Socket s = new Socket("localhost", 45678);
        // Declaring Input Streams for taking input
        DataInputStream dis = new DataInputStream(s.getInputStream());
        // Declaring output streams for output
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        while (true) {
            // taking message input from the user
            System.out.println("Enter the Message to be Sent: ");
            // storing user input message
            String data = sc.nextLine();
            String res = new String();
            // performing byte stuffing on input data
            for (int i = 0; i < data.length(); i++) {
                // if 'F' is in the data then stuff 'F' with 'E'
                if (data.charAt(i) == 'F')
                    res = res + 'E' + data.charAt(i);
                // if 'E' is in the data then stuff 'E' with 'E'
                else if (data.charAt(i) == 'E')
                    res = res + 'E' + data.charAt(i);
                // otherwise simply append the input character
                else
                    res = res + data.charAt(i);
            }
            // Stuffing of the starting and end of the data with 'F'
            res = 'F' + res + 'F';
            System.out.println("The data being sent to the receiver(after byte stuffing) is: " + res);
            // Send it to the receiver
            dos.writeUTF(res);
            System.out.println("Message Sending....");
            if (dis.readUTF().equals("success"))
                System.out.println("Thanks for the Feedback Server!!");
            // End Messaging
            dos.writeUTF("bye");
            break;
        }
        // Closing all the connections
        s.close();
        dis.close();
        dos.close();
    }
}