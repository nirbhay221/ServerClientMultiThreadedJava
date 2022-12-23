
import java.io.*;
import java.net.*;

public class Part1a
{ //Code Reference from https://lindevs.com/create-single-threaded-tcp-echo-server-and-client-using-java
    private final static int PORT = 58000;

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORT); // initializing server socket

        System.out.println("Listening on port " + PORT);

        while (true) {
            try (Socket socket = serverSocket.accept();//socket accepts client request
                 InputStreamReader isr = new InputStreamReader(socket.getInputStream()); // Input Stream Reader
                 BufferedReader in = new BufferedReader(isr);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true) //Out put Stream writer
            ) {
                System.out.println("Connection accepted");

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Server received: " + line + ". Sending to client");
                    out.println(line);//Echoing the message back

                    if (line.equals("Bye")) {
                        break;
                    }
                }
            }
        }
    }
}