import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringJoiner;

public class Client
{
    private final static String HOSTNAME = "csa1.bu.edu";
    private final static int PORT = 58000;

    public static void main(String[] args) throws IOException
    {      Scanner Input ;
        int PacketSize;
        Input= new Scanner(System.in);
        double[] RTT;
        double sum = 0 ;
        double sum2 =0;
        double[] Throughput = new double[2000];
        double size_of_data;
        double meanThroughput;
        String newNumber;
        String SecondPhase;
        String newOneByte;
        String ThirdPhase;
        double meanrtt ;
        int sequenceNumber;
        RTT = new double[20];
        long startTime ;
        long elapsedTime;
        double elapsedTimeSeconds;
        long difference;
        String FirstPhase;
        long Conversion;
        try (Socket clientSocket = new Socket(HOSTNAME, PORT);
             InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader in = new BufferedReader(isr);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        ) {
            System.out.println("Connected to " + HOSTNAME + " on port " + PORT);

            String data = "Making First Connection";
String SelectMode;
            System.out.println("Sending to server:\n" + data);
            out.println(data);

            String line;

            while ((line = in.readLine()) != null) { //Making tcp Connection
                System.out.println("TCP Connection Made : Client received: " + line);
                break;
            }
            System.out.println("TCP Connection Successful now moving to Protocol Phase 1"); // When the server replies the first time we move to First Phase for sending the message
            System.out.println("Enter RTT OR tput");
            SelectMode = Input.nextLine();
            while(!SelectMode.equalsIgnoreCase("RTT") && !SelectMode.equalsIgnoreCase("TPUT")){//if none of the above keywords are pressed ,

                SelectMode = Input.nextLine();  //it asks for input again
            }
            int ServerDelay =0;//Initially Server Delay = 0
            System.out.println("Enter Server Delay (ms)");
            while (true) {

                if (Input.hasNextInt()) {
                    ServerDelay = Input.nextInt();
                    break;
                } else {
                    Input.next();
                }
            }

            System.out.println("Enter the Bytes you want to send to the server :" );
            if(SelectMode.equalsIgnoreCase("RTT")) {//if user selects rtt , we send data packets to check for mean rtt values .
                while (true) {

                    if (Input.hasNextInt()) {
                        PacketSize = Input.nextInt();
                        break;
                    } else {
                        Input.next();
                    }
                }
                System.out.println(PacketSize);
                switch (PacketSize){                    // 1, 100, 200, 400, 800 and 1000 - Data Packet Size
                    case 1:
                        System.out.println("Sending Data Packet of Size 1");
                        FirstPhase = "s rtt 10 1 "+ServerDelay+"\n";// First Phase Format for indicating 10 probe messages to be delivered.
                        System.out.println(FirstPhase);
                        out.println(FirstPhase);//sending First Phase to Server'
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){ // if the server receives it successfully , we move to next phase for delivery
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                    }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();//else we close the connection with client
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {//sending 10 probe messages of the size we indicated in the first phase
                            byte[] onebyte = "a".getBytes();
                             newOneByte = new String(onebyte, StandardCharsets.UTF_8);
                           sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + newOneByte + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");//Checking Echo Message
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;//calculating rtt for each probe
                            System.out.println("RTT for the 1 Byte Delivery : " + elapsedTimeSeconds + " seconds or "+elapsedTimeSeconds*1000+" ms" );
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                        sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;// mean rtt calculated
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or "+ meanrtt*1000 + " ms");
                        ThirdPhase = "t\n";
                        String TerminationSuccess = "200 OK: Closing Connection";
                        out.println(ThirdPhase);// third phase check to terminate the connection
                        out.flush();
                        String ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }}


                    case 100://Every other data packet sent here has a similar process like the first one
                        System.out.println("Sending Data Packet of Size 100");
                        FirstPhase = "s rtt 10 100 "+ServerDelay+"\n";//First Phase sent to server .
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){//
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {//creating 100 bytes of message
                            byte[] hundredbyte = new byte[100];
                            String[] hundreds = new String[100];
                            for(int j = 0 ; j < 100 ; j++){
                                hundreds[j] = "a";//getting 100 strings
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 100 ; j++){
                               joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);//converting it to bytes
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8); //converting it back to string for the payload
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 100 Bytes Delivery : " + elapsedTimeSeconds + " seconds  or "+elapsedTimeSeconds*1000+" ms" );
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or " + meanrtt*1000 + " ms");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                        System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                    }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }}


                    case 200 ://For 200 bytes of data , the packet is created similarly to 100 bytes data packet .

                        System.out.println("Sending Data Packet of Size 200");
                        FirstPhase = "s rtt 10 200 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] twohundredbyte = new byte[200];
                            String[] twohundreds = new String[200];//creating 200 bytes of message
                            for(int j = 0 ; j < 200 ; j++){
                                twohundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 200 ; j++){
                                joiner.add(twohundreds[j]);
                            }
                            String twohundredJoined =  joiner.toString();
                            twohundredbyte = twohundredJoined.getBytes(StandardCharsets.UTF_8);
                            String twohundredstring;
                            twohundredstring = new String(twohundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + twohundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 200 Byte Delivery : " + elapsedTimeSeconds + "seconds or "+elapsedTimeSeconds*1000+" ms");
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or"+ meanrtt*1000 + " ms");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 400:

                        System.out.println("Sending Data Packet of Size 400");
                        FirstPhase = "s rtt 10 400 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[400];
                            String[] hundreds = new String[400];
                            for(int j = 0 ; j < 400 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 400 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 400 Bytes Delivery : " + elapsedTimeSeconds + "seconds or "+elapsedTimeSeconds*1000+" ms");
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or" + meanrtt*1000 + " ms");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 800:

                        System.out.println("Sending Data Packet of Size 800");
                        FirstPhase = "s rtt 10 800 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{

                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[800];
                            String[] hundreds = new String[800];
                            for(int j = 0 ; j < 800 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 800 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 800 Bytes Delivery : " + elapsedTimeSeconds + "seconds or "+elapsedTimeSeconds*1000+" ms");
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or" + meanrtt*1000 + " ms");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 1000:

                        System.out.println("Sending Data Packet of Size 1000");
                        FirstPhase = "s rtt 10 1000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{

                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[1000];
                            String[] hundreds = new String[1000];
                            for(int j = 0 ; j < 1000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 1000 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 1000 Bytes Delivery : " + elapsedTimeSeconds + "seconds  or "+elapsedTimeSeconds*1000+" ms");
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or" + meanrtt*1000 + " ms." );
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }

                }

            }
            else if(SelectMode.equalsIgnoreCase("TPUT")){//if we get the tput as input , we look for tput in the first phase .
                while (true) {

                    if (Input.hasNextInt()) {
                        PacketSize = Input.nextInt();
                        break;
                    } else {
                        Input.next();
                    }
                }
                System.out.println(PacketSize);
                switch (PacketSize){                    // 1000,2000,4000,8000,16000,32000 - data packet size
                    case 10://This is the 1000 byte data size used . Can't use 1000 , because it's already in use above.
                        System.out.println("Sending Data Packet of Size 1000");
                        FirstPhase = "s tput 10 1000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[1000];
                            String[] hundreds = new String[1000];
                            for(int j = 0 ; j < 1000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 1000 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 1000 Bytes Delivery : " + elapsedTimeSeconds + "seconds or "+elapsedTimeSeconds*1000+" ms");
                            RTT[i] = elapsedTimeSeconds;
                            Throughput[i] =(double) 1000 / RTT[i];
                            System.out.println("Throughput for the 1000 Bytes Delivery : " + (Throughput[i]*8)/1000 + "kbps Or "+ (Throughput[i]*8)/1000000+ "mbps");
                            System.out.println();


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum2 = Throughput[i]+sum2;

                        }
                        meanrtt = sum / 10  ;
                        meanThroughput = sum2/10;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or" + meanrtt*1000 + " ms");
                        System.out.println("Mean Throughput : " +(meanThroughput*8)/1000 + "kbps. Or" + (meanThroughput*8)/1000000+"mbps");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        String ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 2000:
                        System.out.println("Sending Data Packet of Size 2000");
                        FirstPhase = "s tput 10 2000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[2000];
                            String[] hundreds = new String[2000];
                            for(int j = 0 ; j < 2000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 2000 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            Throughput[i] = (double) 2000/elapsedTimeSeconds;
                            System.out.println("RTT for the 2000 Bytes Delivery : " + elapsedTimeSeconds + " seconds or"+ elapsedTimeSeconds*1000 + " ms");
                            System.out.println("Throughput for the 2000 Bytes Delivery : " + (Throughput[i]*8)/1000 + "kbps Or "+(Throughput[i]*8)/1000000+" mbps");
                            RTT[i] = elapsedTimeSeconds;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum2 = Throughput[i]+sum2;

                        }
                        meanThroughput = sum2/10;
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or"+ meanrtt*1000 +" ms");
                        System.out.println("Mean Throughput : " +(meanThroughput*8)/1000 + "kbps. Or "+ (meanThroughput*8)/1000000+" mbps");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 4000 :

                        System.out.println("Sending Data Packet of Size 4000");
                        FirstPhase = "s tput 10 4000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{

                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] twohundredbyte = new byte[4000];
                            String[] twohundreds = new String[4000];
                            for(int j = 0 ; j < 4000 ; j++){
                                twohundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 4000 ; j++){
                                joiner.add(twohundreds[j]);
                            }
                            String twohundredJoined =  joiner.toString();
                            twohundredbyte = twohundredJoined.getBytes(StandardCharsets.UTF_8);
                            String twohundredstring;
                            twohundredstring = new String(twohundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + twohundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            System.out.println("RTT for the 4000 Bytes Delivery : " + elapsedTimeSeconds + " seconds or "+elapsedTimeSeconds*1000 + " ms");
                            RTT[i] = elapsedTimeSeconds;
                            Throughput[i] = 4000/ elapsedTimeSeconds;
                            System.out.println("Throughput for the 4000 Bytes Delivery : " +  (Throughput[i]*8)/1000 + "kbps or" + (Throughput[i]*8)/1000000 + "mbps");


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum2 = Throughput[i]+sum2;

                        }
                        meanThroughput = sum2/10;
                        meanrtt = sum / 10  ;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or" + meanrtt*1000 +" ms");
                        System.out.println("Mean RTT : " +(meanThroughput*8)/1000 + "kbps + or"+(meanThroughput*8)/1000000+" mbps" );
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                            System.out.println("Connection Ended Erroneously");
                            System.out.println(ErrorNewMessage+" received.");
                            clientSocket.close();
                            System.exit(0);}
                        else {
                            System.out.println("Connection Ended Erroneously");
                            clientSocket.close();
                            System.exit(0);
                        }
                        }
                    case 8000:

                        System.out.println("Sending Data Packet of Size 8000");
                        FirstPhase = "s tput 10 8000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{

                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[8000];
                            String[] hundreds = new String[8000];
                            for(int j = 0 ; j < 8000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 8000; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                 clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            Throughput[i] =(double) 8000/elapsedTimeSeconds;
                            System.out.println("RTT for the 8000 Bytes Delivery : " + elapsedTimeSeconds + "seconds Or " + elapsedTimeSeconds*1000 +" ms");
                            System.out.println("Throughput for the 8000 Bytes Delivery : " + (Throughput[i]*8)/1000 + "kbps or"+(Throughput[i]*8)/1000000 + "mbps" );
                            RTT[i] = elapsedTimeSeconds;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }

                        for(int i =1 ; i<=10; i++){
                            sum2 = Throughput[i]+sum2;

                        }
                        meanrtt = sum / 10  ;
                        meanThroughput = sum2/10;
                        System.out.println("Mean RTT : " +meanrtt + "seconds.Or" + meanrtt*1000 + "ms");
                        System.out.println("Mean Throughput : " +(meanThroughput*8)/1000 + "kbps. Or "+ (meanThroughput*8)/1000000 + "mbps"  );
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 16000:

                        System.out.println("Sending Data Packet of Size 16000");
                        FirstPhase = "s tput 10 16000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[16000];
                            String[] hundreds = new String[16000];
                            for(int j = 0 ; j < 16000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 16000 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            Throughput[i] =(double) 16000/elapsedTimeSeconds;
                            System.out.println("RTT for the 16000 Bytes Delivery : " + elapsedTimeSeconds + "seconds Or "+ elapsedTimeSeconds*1000+" ms");
                            System.out.println("Throughput for the 16000 Bytes Delivery : " + (Throughput[i]*8)/1000  + "kbps or"+(Throughput[i]*8)/1000000 + "mbps");
                            RTT[i] = elapsedTimeSeconds;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }
                        for(int i =1 ; i<=10; i++){
                            sum2= Throughput[i]+sum2;

                        }
                        meanrtt = sum / 10  ;
                        meanThroughput = sum2/10;
                        System.out.println("Mean RTT : " +meanrtt + "seconds. Or"+ meanrtt*1000+ "ms");
                        System.out.println("Mean RTT : " +(meanThroughput*8)/1000 + "kbps. or"+ (meanThroughput*8)/1000000+" mbps");
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }
                    case 32000:

                        System.out.println("Sending Data Packet of Size 32000");
                        FirstPhase = "s tput 10 32000 "+ServerDelay+"\n";
                        out.println(FirstPhase);
                        out.flush();
                        if(in.readLine().equalsIgnoreCase("200 OK: Ready")){
                            System.out.println("Starting Second Phase of Sending Packets - 10 Probe Messages");

                        }
                        else{
                            String ErrorMessage = in.readLine();
                            System.out.println(ErrorMessage+" received.");
                            System.out.println("Closing Connection");
                            clientSocket.close();
                            System.exit(0);
                        }
                        for(int i =1; i <= 10 ; i++) {
                            byte[] hundredbyte = new byte[32000];
                            String[] hundreds = new String[32000];
                            for(int j = 0 ; j < 32000 ; j++){
                                hundreds[j] = "a";
                            }
                            StringJoiner joiner = new StringJoiner("");
                            for(int j = 0 ; j < 32000 ; j++){
                                joiner.add(hundreds[j]);
                            }
                            String hundredJoined =  joiner.toString();
                            hundredbyte = hundredJoined.getBytes(StandardCharsets.UTF_8);
                            String hundredstring;
                            hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
                            sequenceNumber = i;
                            newNumber = String.valueOf(sequenceNumber);

                            SecondPhase = "m " + newNumber + " " + hundredstring + "\n";
                            startTime = System.nanoTime();
                            out.println(SecondPhase);
                            out.flush();
                            while (in.readLine().equalsIgnoreCase("404 ERROR: Invalid Measurement Message")) {
                                System.out.println("Connection Closing error received.");
                                clientSocket.close();
                                System.exit(0);
                            }
                            while (in.readLine().equalsIgnoreCase(SecondPhase)) {
                                break;
                            }
                            System.out.println("Probe Acknowledged");
                            elapsedTime = System.nanoTime();
                            difference = elapsedTime - startTime;
                            elapsedTimeSeconds = (double) difference / 1000000000;
                            Throughput[i] =(double) 32000/elapsedTimeSeconds;
                            System.out.println("RTT for the 32000 Bytes Delivery : " + elapsedTimeSeconds + "seconds Or "+elapsedTimeSeconds*1000+" ms");
                            System.out.println("Throughput for the 32000 Bytes Delivery : " + (Throughput[i]*8)/1000 + "kbps or"+(Throughput[i]*8)/1000000+"mbps");
                            RTT[i] = elapsedTimeSeconds;


                        }
                        for(int i =1 ; i<=10; i++){
                            sum = RTT[i]+sum;

                        }

                        for(int i =1 ; i<=10; i++){
                            sum2 = Throughput[i]+sum2;

                        }
                        meanrtt = sum / 10  ;
                        meanThroughput = sum2/10;
                        System.out.println("Mean RTT : " +meanrtt + "seconds .Or"+ meanrtt*1000 +" ms");
                        System.out.println("Mean Throughput : " + (meanThroughput*8)/1000 + "kbps . Or"+(meanThroughput*8)/1000000 + "mbps" );
                        ThirdPhase = "t\n";
                        out.println(ThirdPhase);
                        out.flush();
                        ErrorNewMessage = "404 ERROR: Invalid Connection Termination Message";
                        if(in.readLine().equalsIgnoreCase("200 OK: Closing Connection")){
                            System.out.println("Connection Ended Successfully.");
                            clientSocket.close();
                            System.exit(0);
                        }
                        else{
                            if(in.readLine().equalsIgnoreCase(ErrorNewMessage)){
                                System.out.println("Connection Ended Erroneously");
                                System.out.println(ErrorNewMessage+" received.");
                                clientSocket.close();
                                System.exit(0);}
                            else {
                                System.out.println("Connection Ended Erroneously");
                                clientSocket.close();
                                System.exit(0);
                            }
                        }

                }
            }

        }
    }
}