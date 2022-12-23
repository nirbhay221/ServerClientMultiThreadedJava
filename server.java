import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class server
{
    private final static int PORT = 58000;

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Listening on port " + PORT);

        while (true) {
            try (Socket socket = serverSocket.accept();
                 InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                 BufferedReader in = new BufferedReader(isr);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                System.out.println("Connection accepted");

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Server received: " + line + ". Sending to client");
                    out.println(line);
                    break;
                }


                System.out.println("TCP connection between server and client started : ");
                System.out.println("First Phase starts : ");
                String FirstPhase ;
                String SecondPhase;
                String ThirdPhase;
                String TerminationPhase ;

                FirstPhase = in.readLine();
                String MessagePhase = "^m\\s(?:10|1|[2-9])\\s.*";
                Pattern p = Pattern.compile(MessagePhase);
                int messageSize;
                int p1;
                int p2;
                int p3;
                String fragment1;
                String fragment2;
                String fragment3;
                int NumberOfProbes;
                int serverDelay = 0;

                String str1 = "s rtt 10 1 0" ;
                String str2 = "s rtt 10 100 0" ;
                String str3 = "s rtt 10 200 0" ;
                String str4 = "s rtt 10 400 0" ;
                String str5 = "s rtt 10 800 0" ;
                String str6 = "s rtt 10 1000 0";
                String str7 = "s tput 10 1000 0" ;
                String str8 = "s tput 10 2000 0" ;
                String str9 = "s tput 10 4000 0" ;
                String str10 = "s tput 10 8000 0" ;
                String str11 = "s tput 10 16000 0" ;
                String str12 = "s tput 10 32000 0";
                String str13 = "s rtt 10 1 1000" ;
                String str14 = "s rtt 10 100 1000" ;
                String str15 = "s rtt 10 200 1000" ;
                String str16 = "s rtt 10 400 1000" ;
                String str17 = "s rtt 10 800 1000" ;
                String str18 = "s rtt 10 1000 1000";
                String str19 = "s rtt 10 1 2000" ;
                String str20 = "s rtt 10 100 2000" ;
                String str21 = "s rtt 10 200 2000" ;
                String str22 = "s rtt 10 400 2000" ;
                String str23 = "s rtt 10 800 2000" ;
                String str24 = "s rtt 10 1000 2000";
                String str25 = "s rtt 10 1 500" ;
                String str26 = "s rtt 10 100 500" ;
                String str27 = "s rtt 10 200 500" ;
                String str28 = "s rtt 10 400 500" ;
                String str29 = "s rtt 10 800 500" ;
                String str30 = "s rtt 10 1000 500";
                String str31 = "s tput 10 1000 500" ;
                String str32 = "s tput 10 2000 500" ;
                String str33 = "s tput 10 4000 500" ;
                String str34 = "s tput 10 8000 500" ;
                String str35 = "s tput 10 16000 500" ;
                String str36 = "s tput 10 32000 500";
                String str37 = "s tput 10 1000 1000" ;
                String str38 = "s tput 10 2000 1000" ;
                String str39 = "s tput 10 4000 1000" ;
                String str40 = "s tput 10 8000 1000" ;
                String str41 = "s tput 10 16000 1000" ;
                String str42 = "s tput 10 32000 1000";
                String str43 = "s tput 10 1000 2000" ;
                String str44 = "s tput 10 2000 2000" ;
                String str45 = "s tput 10 4000 2000" ;
                String str46 = "s tput 10 8000 2000" ;
                String str47 = "s tput 10 16000 2000" ;
                String str48 = "s tput 10 32000 2000";
                TerminationPhase = "t";
                if(FirstPhase == null){
                    System.out.println("Connection Ending ");
                   out.println("404 ERROR: Invalid Connection Setup Message");

                }
                else if(FirstPhase.equalsIgnoreCase(str1) || FirstPhase.equalsIgnoreCase(str2) || FirstPhase.equalsIgnoreCase(str3) || FirstPhase.equalsIgnoreCase(str4) || FirstPhase.equalsIgnoreCase(str5) || FirstPhase.equalsIgnoreCase(str6) || FirstPhase.equalsIgnoreCase(str13)|| FirstPhase.equalsIgnoreCase(str14)|| FirstPhase.equalsIgnoreCase(str15)|| FirstPhase.equalsIgnoreCase(str16)|| FirstPhase.equalsIgnoreCase(str17)|| FirstPhase.equalsIgnoreCase(str18)|| FirstPhase.equalsIgnoreCase(str19)|| FirstPhase.equalsIgnoreCase(str20)|| FirstPhase.equalsIgnoreCase(str21)|| FirstPhase.equalsIgnoreCase(str22)|| FirstPhase.equalsIgnoreCase(str23)|| FirstPhase.equalsIgnoreCase(str24)|| FirstPhase.equalsIgnoreCase(str25)|| FirstPhase.equalsIgnoreCase(str26)|| FirstPhase.equalsIgnoreCase(str27)|| FirstPhase.equalsIgnoreCase(str28)|| FirstPhase.equalsIgnoreCase(str29)|| FirstPhase.equalsIgnoreCase(str30))
                {
                    p1 = FirstPhase.indexOf(' ',4);
                    p2 = FirstPhase.indexOf(' ',6);
                    p3 = FirstPhase.indexOf(' ',9);
                    fragment1 = FirstPhase.substring(p1+1,p2);
                    System.out.println(FirstPhase);
                    NumberOfProbes = Integer.parseInt(fragment1);
                    System.out.println("Number Of Probes  :" + NumberOfProbes);
                    fragment2 = FirstPhase.substring(p2+1,p3);
                    messageSize = Integer.parseInt(fragment2);
                    System.out.println("Message size to be received : "+ messageSize);
                    fragment3 = FirstPhase.substring(p3+1,FirstPhase.length());
                    serverDelay = Integer.parseInt(fragment3);
                    System.out.println("Server Delay Introduced : "+ serverDelay);
                    out.println("200 OK: Ready");

                }
                else if(FirstPhase.equalsIgnoreCase(str7) || FirstPhase.equalsIgnoreCase(str8) || FirstPhase.equalsIgnoreCase(str9) || FirstPhase.equalsIgnoreCase(str10)|| FirstPhase.equalsIgnoreCase(str11) || FirstPhase.equalsIgnoreCase(str12)|| FirstPhase.equalsIgnoreCase(str31)|| FirstPhase.equalsIgnoreCase(str32)|| FirstPhase.equalsIgnoreCase(str33)|| FirstPhase.equalsIgnoreCase(str34)|| FirstPhase.equalsIgnoreCase(str35)|| FirstPhase.equalsIgnoreCase(str36)|| FirstPhase.equalsIgnoreCase(str37)|| FirstPhase.equalsIgnoreCase(str38)|| FirstPhase.equalsIgnoreCase(str39)|| FirstPhase.equalsIgnoreCase(str40)|| FirstPhase.equalsIgnoreCase(str41)|| FirstPhase.equalsIgnoreCase(str42)|| FirstPhase.equalsIgnoreCase(str43)|| FirstPhase.equalsIgnoreCase(str44)|| FirstPhase.equalsIgnoreCase(str45)|| FirstPhase.equalsIgnoreCase(str46)|| FirstPhase.equalsIgnoreCase(str47)|| FirstPhase.equalsIgnoreCase(str48)){
                    p1 = FirstPhase.indexOf(' ',4);
                    p2 = FirstPhase.indexOf(' ',7);
                    p3 = FirstPhase.indexOf(' ',10);
                    fragment1 = FirstPhase.substring(p1+1,p2);
                    NumberOfProbes = Integer.parseInt(fragment1);
                    System.out.println("Number Of Probes to be sent :" + NumberOfProbes);
                    fragment2 = FirstPhase.substring(p2+1,p3);
                    messageSize = Integer.parseInt(fragment2);
                    System.out.println("Message size to be received : "+ messageSize);
                    fragment3 = FirstPhase.substring(p3+1,FirstPhase.length());
                    serverDelay = Integer.parseInt(fragment3);
                    System.out.println("Server Delay Introduced : "+ serverDelay);
                             out.println("200 OK: Ready");
                }

                else{

                    out.println("404 ERROR: Invalid Connection Setup Message");

                }
                System.out.println("First Phase Completed ");
                System.out.println("Second Phase starts : ");
                for(int i = 1 ; i <= 20 ;  i++){
                SecondPhase = in.readLine();

                if (SecondPhase != null){
                Matcher m = p.matcher(SecondPhase);
                boolean check = m.matches();
                if(check){
                    System.out.println("working "+i/2);
                }

                    try {
                        Thread.sleep(serverDelay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    out.println((SecondPhase));
                }
                else {
                    out.println("404 ERROR: Invalid Measurement Message");
                    System.exit(0);
                }
                }

                System.out.println("Second Phase Completed");
                System.out.println("Third Phase Starts : ");
                ThirdPhase = in.readLine();
                while(ThirdPhase != null) {
                    if(ThirdPhase != null){
                        out.println("200 OK: Closing Connection");
                        System.out.println("Connection ended successfully");
                        System.exit(0);}
                    else{
                        out.println("404 ERROR: Invalid Connection Termination Message");
                        System.out.println("Connection ended Erroneously");
                        System.exit(0);}
                    }
                }
                }


    }
}