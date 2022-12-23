import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Server class
class multithreadedserver {
    public static void main(String[] args)
    {//Code reference for multithreading server from https://www.geeksforgeeks.org/multithreaded-servers-in-java/
        ServerSocket server = null;

        try {


            server = new ServerSocket(58000);//server listening on 58000
            server.setReuseAddress(true);

          //waiting for client request
            while (true) {


                Socket client = server.accept();


                System.out.println("Client Connected");
                System.out.println("Client Details are as follows: ");
                System.out.println( client.getInetAddress().getHostAddress());



                ServingClient clientSocket
                        = new ServingClient(client);//Thread object created


                new Thread(clientSocket).start(); //This Thread handles the client separately
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static class ServingClient implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ServingClient(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {

                out = new PrintWriter(
                        clientSocket.getOutputStream(), true);//output data stream

                in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));


                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Server received: " + line + ". Sending to client");
                    out.println(line);
                    out.flush();
                    break;
                }


                System.out.println("TCP connection between server and client started : ");
                System.out.println("First Phase starts : ");
                String FirstPhase;
                String SecondPhase;
                String ThirdPhase;
                String TerminationPhase;

                FirstPhase = in.readLine();
                String MessagePhase = "^m\\s(?:10|1|[2-9])\\s.*";//regex for checking the second phase by the server
                Pattern p = Pattern.compile(MessagePhase);
                int messageSize;
                int p1;
                int p2;
                int p3;
                String fragment1;
                String fragment2;
                String fragment3;
                int NumberOfProbes = 0;
                int serverDelay = 0;

                String str1 = "s rtt 10 1 0";
                String str2 = "s rtt 10 100 0";
                String str3 = "s rtt 10 200 0";
                String str4 = "s rtt 10 400 0";
                String str5 = "s rtt 10 800 0";
                String str6 = "s rtt 10 1000 0";
                String str7 = "s tput 10 1000 0";
                String str8 = "s tput 10 2000 0";
                String str9 = "s tput 10 4000 0";
                String str10 = "s tput 10 8000 0";
                String str11 = "s tput 10 16000 0";
                String str12 = "s tput 10 32000 0";
                String str13 = "s rtt 10 1 1000";
                String str14 = "s rtt 10 100 1000";
                String str15 = "s rtt 10 200 1000";
                String str16 = "s rtt 10 400 1000";
                String str17 = "s rtt 10 800 1000";
                String str18 = "s rtt 10 1000 1000";
                String str19 = "s rtt 10 1 2000";
                String str20 = "s rtt 10 100 2000";
                String str21 = "s rtt 10 200 2000";
                String str22 = "s rtt 10 400 2000";
                String str23 = "s rtt 10 800 2000";
                String str24 = "s rtt 10 1000 2000";
                String str25 = "s rtt 10 1 500";
                String str26 = "s rtt 10 100 500";
                String str27 = "s rtt 10 200 500";
                String str28 = "s rtt 10 400 500";
                String str29 = "s rtt 10 800 500";
                String str30 = "s rtt 10 1000 500";
                String str31 = "s tput 10 1000 500";
                String str32 = "s tput 10 2000 500";
                String str33 = "s tput 10 4000 500";
                String str34 = "s tput 10 8000 500";
                String str35 = "s tput 10 16000 500";
                String str36 = "s tput 10 32000 500";
                String str37 = "s tput 10 1000 1000";
                String str38 = "s tput 10 2000 1000";
                String str39 = "s tput 10 4000 1000";
                String str40 = "s tput 10 8000 1000";
                String str41 = "s tput 10 16000 1000";
                String str42 = "s tput 10 32000 1000";
                String str43 = "s tput 10 1000 2000";
                String str44 = "s tput 10 2000 2000";
                String str45 = "s tput 10 4000 2000";
                String str46 = "s tput 10 8000 2000";
                String str47 = "s tput 10 16000 2000";
                String str48 = "s tput 10 32000 2000";
                TerminationPhase = "t";
//checks for the first phase format and extract values of number of probes to be sent , message size and server delay
                if (FirstPhase.equalsIgnoreCase(str1) || FirstPhase.equalsIgnoreCase(str2) || FirstPhase.equalsIgnoreCase(str3) || FirstPhase.equalsIgnoreCase(str4) || FirstPhase.equalsIgnoreCase(str5) || FirstPhase.equalsIgnoreCase(str6) || FirstPhase.equalsIgnoreCase(str13) || FirstPhase.equalsIgnoreCase(str14) || FirstPhase.equalsIgnoreCase(str15) || FirstPhase.equalsIgnoreCase(str16) || FirstPhase.equalsIgnoreCase(str17) || FirstPhase.equalsIgnoreCase(str18) || FirstPhase.equalsIgnoreCase(str19) || FirstPhase.equalsIgnoreCase(str20) || FirstPhase.equalsIgnoreCase(str21) || FirstPhase.equalsIgnoreCase(str22) || FirstPhase.equalsIgnoreCase(str23) || FirstPhase.equalsIgnoreCase(str24) || FirstPhase.equalsIgnoreCase(str25) || FirstPhase.equalsIgnoreCase(str26) || FirstPhase.equalsIgnoreCase(str27) || FirstPhase.equalsIgnoreCase(str28) || FirstPhase.equalsIgnoreCase(str29) || FirstPhase.equalsIgnoreCase(str30)) {
                    p1 = FirstPhase.indexOf(' ', 4);
                    p2 = FirstPhase.indexOf(' ', 6);
                    p3 = FirstPhase.indexOf(' ', 9);
                    fragment1 = FirstPhase.substring(p1 + 1, p2);
                    System.out.println(FirstPhase);
                    NumberOfProbes = Integer.parseInt(fragment1);
                    System.out.println("Number Of Probes to be sent :" + NumberOfProbes);
                    fragment2 = FirstPhase.substring(p2 + 1, p3);
                    messageSize = Integer.parseInt(fragment2);
                    System.out.println("Message size to be received : " + messageSize);
                    fragment3 = FirstPhase.substring(p3 + 1, FirstPhase.length());
                    serverDelay = Integer.parseInt(fragment3);
                    System.out.println("Server Delay Introduced : " + serverDelay);
                    out.println("200 OK: Ready");
                    out.flush();


                }

//checks for the first phase format and extract values of number of probes to be sent , message size and server delay
                else if (FirstPhase.equalsIgnoreCase(str7) || FirstPhase.equalsIgnoreCase(str8) || FirstPhase.equalsIgnoreCase(str9) || FirstPhase.equalsIgnoreCase(str10) || FirstPhase.equalsIgnoreCase(str11) || FirstPhase.equalsIgnoreCase(str12) || FirstPhase.equalsIgnoreCase(str31) || FirstPhase.equalsIgnoreCase(str32) || FirstPhase.equalsIgnoreCase(str33) || FirstPhase.equalsIgnoreCase(str34) || FirstPhase.equalsIgnoreCase(str35) || FirstPhase.equalsIgnoreCase(str36) || FirstPhase.equalsIgnoreCase(str37) || FirstPhase.equalsIgnoreCase(str38) || FirstPhase.equalsIgnoreCase(str39) || FirstPhase.equalsIgnoreCase(str40) || FirstPhase.equalsIgnoreCase(str41) || FirstPhase.equalsIgnoreCase(str42) || FirstPhase.equalsIgnoreCase(str43) || FirstPhase.equalsIgnoreCase(str44) || FirstPhase.equalsIgnoreCase(str45) || FirstPhase.equalsIgnoreCase(str46) || FirstPhase.equalsIgnoreCase(str47) || FirstPhase.equalsIgnoreCase(str48)) {
                    p1 = FirstPhase.indexOf(' ', 4);
                    p2 = FirstPhase.indexOf(' ', 7);
                    p3 = FirstPhase.indexOf(' ', 10);
                    fragment1 = FirstPhase.substring(p1 + 1, p2);
                    NumberOfProbes = Integer.parseInt(fragment1);
                    System.out.println("Number Of Probes to be sent :" + NumberOfProbes);
                    fragment2 = FirstPhase.substring(p2 + 1, p3);
                    messageSize = Integer.parseInt(fragment2);
                    System.out.println("Message size to be received : " + messageSize);
                    fragment3 = FirstPhase.substring(p3 + 1, FirstPhase.length());
                    serverDelay = Integer.parseInt(fragment3);
                    System.out.println("Server Delay Introduced : " + serverDelay);
                    out.println("200 OK: Ready");
                    out.flush();
                } else {

                    out.println("404 ERROR: Invalid Connection Setup Message");// if the format is null or invalid - we return error message to close connection .
                    out.flush();
                }
                System.out.println("First Phase Completed ");
                System.out.println("Second Phase starts : ");

              try {//Checking the second Phase format
                  for (int i = 1; i <= 2 * NumberOfProbes; i++) {
                      SecondPhase = in.readLine();

                      if (SecondPhase != null) {
                          Matcher m = p.matcher(SecondPhase);
                          boolean check = m.matches();
                          if (check) {
                              System.out.println("Message " + i / 2 + " received and starting to send the echo message to client . ");
                              int p4 = SecondPhase.indexOf(' ',0);
                              int p5 = SecondPhase.indexOf(' ',2);
                              String Fragment5 = SecondPhase.substring(p4+1,p5);
                              int SequenceNumber = Integer.parseInt(Fragment5);
                              System.out.println(SequenceNumber);
                              if(SequenceNumber != i/2){//checking for each sequence number
                                  out.println("404 ERROR: Invalid Measurement Message");
                              }

                          }

                          try {
                              Thread.sleep(serverDelay);
                          } catch (InterruptedException e) {
                              throw new RuntimeException(e);
                          }
                          out.println((SecondPhase));
                          out.flush();
                      } else {
                          out.println("404 ERROR: Invalid Measurement Message");
                          out.flush();

                      }
                  }
              }
              catch(Exception e){
                  System.out.println(e.getMessage());
                  out.println("404 ERROR: Invalid Measurement Message");
                  out.flush();
              }
                System.out.println("Second Phase Completed");
                System.out.println("Third Phase Starts : ");
                //Checking third phase format
                while(in.readLine() != null){

                    if(in.readLine().equalsIgnoreCase("t")){
                        System.out.println("Third Phase Completing");
                        System.out.println("Termination Message Received");
                        System.out.println("Closing Connection with Client . ");
                        out.println("200 OK: Closing Connection");
                        out.flush();
                        break;

                    }


                    }




            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}