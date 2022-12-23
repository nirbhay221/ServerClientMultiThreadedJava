import java.awt.font.NumericShaper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner Input = new Scanner(System.in);
        int PacketSize = 0;
        int sum = 0 ;
        int MessageSize= 0;
        int ServerDelay = 0 ;
        int NumberOfProbes = 0 ;
        String[] s = new String[100];
        for(int i = 0 ; i < 100 ; i++){
            s[i] = "a";

        }
        StringJoiner join = new StringJoiner("");
        for(int i = 0 ; i < 100 ; i++){
            join.add(s[i]);

        }

        String ask =join.toString();
        byte[] hundredbyte = ask.getBytes(StandardCharsets.UTF_8);
        String hundredstring;
        hundredstring = new String(hundredbyte, StandardCharsets.UTF_8);
        System.out.println(hundredstring);
        byte[] data = new byte[200];
        data = ask.getBytes(StandardCharsets.UTF_8);


        Pattern p = Pattern.compile("^m\\s(?:10|1|[2-9])\\s.*");//. represents single character
        Matcher m = p.matcher("m 1 a");
        String str3 = "s rtt 10 1000 1000";
        String str5 = "s tput 10 1 0";
        int p1 = str3.indexOf(' ',4);
        int p2 = str3.indexOf(' ',6);
        int p4 = str3.indexOf(' ',9);
        String str4 = str3.substring(p1+1,p2);
        NumberOfProbes = Integer.parseInt(str4);
          String messageWork = str3.substring(p2+1,p4);
          MessageSize = Integer.parseInt(messageWork);
          String serverd = str3.substring(p4+1,str3.length());
          ServerDelay = Integer.parseInt(serverd);
        System.out.println("Number of Probes : " + NumberOfProbes);
        System.out.println("Message Size : "+MessageSize);
        System.out.println("Server Delay : "+ ServerDelay);
        Thread.sleep(0);

        int p11 = str5.indexOf(' ',4);
        int p12 = str5.indexOf(' ',7);
        int p13 = str5.indexOf(' ',10);
        String str6 = str5.substring(p11+1,p12);
        int NumberOfProbes1 = Integer.parseInt(str6);
        System.out.println("Number of Probes  :" + NumberOfProbes1);
        String messageWork1 = str5.substring(p12+1,p13);
        int MessageSize1 = Integer.parseInt(messageWork1);
        System.out.println("Message Size : " + MessageSize1);
        String serverdd = str5.substring(p13+1,str5.length());
        int ServerDelay1 = Integer.parseInt(serverdd);
        System.out.println("Server Delay : "+ServerDelay1);
        boolean b = m.matches();
        System.out.println(b);
        String str = "This is raw text!";
byte[] onebyte = "a".getBytes();
String and = new String(data,StandardCharsets.UTF_8);
        System.out.println("Hundred Bytes length :"+ and.length());
        String newOneByte = new String(onebyte, StandardCharsets.UTF_8);
        System.out.println("byte length "+onebyte.length+newOneByte.length());
        // string to byte[]
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String str1 = "This is raw text!"+bytes;
        System.out.println(bytes.length);
        System.out.println(str1.length());
        System.out.println("Enter the Bytes you want to send to the server :" );
        System.out.println("Enter RTT OR tput");
        String SelectMode;
        SelectMode = Input.nextLine();
        while(!SelectMode.equalsIgnoreCase("RTT") && !SelectMode.equalsIgnoreCase("TPUT")){//if none of the above keywords are pressed ,

            SelectMode = Input.nextLine();
        }

        System.out.println("Enter the Bytes you want to send to the server :" );
        if(SelectMode.equalsIgnoreCase("RTT")) {
            while (true) {

                if (Input.hasNextInt()) {
                    PacketSize = Input.nextInt();
                    break;
                } else {
                    Input.next();
                }
            }
            System.out.println(PacketSize);


        }
        else if(SelectMode.equalsIgnoreCase("TPUT")){
            while (true) {

                if (Input.hasNextInt()) {
                    PacketSize = Input.nextInt();
                    break;
                } else {
                    Input.next();
                }
            }
            System.out.println(PacketSize);
        }
    }

}