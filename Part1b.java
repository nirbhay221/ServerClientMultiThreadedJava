
    import java.io.*;
import java.net.*;

    public class Part1b
    { //Code Reference from https://lindevs.com/create-single-threaded-tcp-echo-server-and-client-using-java
        private final static String HOSTNAME = "csa1.bu.edu"; //server hostname
        private final static int PORT = 58000; // port number to connect with

        public static void main(String[] args) throws IOException
        {
            try (Socket clientSocket = new Socket(HOSTNAME, PORT); // initializing client socket
                 InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                 BufferedReader in = new BufferedReader(isr);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                System.out.println("Connected to " + HOSTNAME + " on port " + PORT);

                String data = "Connection Process";//sending this to the server.

                System.out.println("Sending to server:\n" + data);
                out.println(data);

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Client received: " + line); // client received the message from the server .
                }
            }
        }
    }

