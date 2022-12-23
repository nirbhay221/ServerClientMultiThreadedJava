import java.io.*;
import java.net.*;

public class PacketsClient
{
    private final static String HOSTNAME = "localhost";
    private final static int PORT = 8080;

    public static void main(String[] args) throws IOException
    {
        try (Socket clientSocket = new Socket(HOSTNAME, PORT);
             InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader in = new BufferedReader(isr);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             DataOutputStream outserver = new DataOutputStream(clientSocket.getOutputStream());



        ) {
            System.out.println("Connected to " + HOSTNAME + " on port " + PORT);

        System.out.println("Number of Bytes sent");
        byte[] message = new byte[1];
        message[0] = (byte) 12314;

for(int i =0 ; i < message.length;i++){
    System.out.println(message[i]);
    System.out.println(message.length);
    outserver.writeInt(message.length);
    outserver.write(message);
    System.out.println("byte array received from server class: \n");
    DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());


    int length = inFromClient.readInt();

//                    if(length>0) {
//                        byte[] message1 = new byte[length];
    inFromClient.readFully(message, 0, message.length); // read the message
//                    }


    byte[] messageFromServer = new byte[length];
    for(int j = 0; j < messageFromServer.length; j++) {
        System.out.println(messageFromServer[j]);
    }
    System.out.println(messageFromServer);
}

        }
    }
}