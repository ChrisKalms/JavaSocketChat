import java.net.ServerSocket; //server
import java.net.Socket; //client socket
import java.io.IOException; //Exception 
import java.io.BufferedReader; //reads text from a character stream
import java.io.InputStreamReader; //bridge from byte stream to character stream
import java.io.DataOutputStream; //write primative java data types to an ouput stream //read by data input stream

public class Server{
    public static void main(String[] args) throws IOException{        
        ServerSocket s = new ServerSocket(1234);
        
        Socket c1 = s.accept();
        System.out.println("client 1 has connected");
        Socket c2 = s.accept();
        System.out.println("client 2 has connected");

        //input stream from client one
        BufferedReader br1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
        //input stream from client two
        BufferedReader br2 = new BufferedReader(new InputStreamReader(c2.getInputStream()));
        //output stream for client one
        DataOutputStream do1 = new DataOutputStream(c1.getOutputStream());
        //output stream for client two
        DataOutputStream do2 = new DataOutputStream(c2.getOutputStream());

        while(true){
            if(br1.ready()){
				String in = br1.readLine();
                System.out.println("client 1: " + in); 
				
                do2.writeBytes(in);
                do2.write(13);
                do2.write(10);
                do2.flush();
            } 
            if(br2.ready()){
				String in = br2.readLine();
                System.out.println("client 2: " + in);
                do1.writeBytes(in);
                do1.write(13);
                do1.write(10);
                do1.flush();
            }
        }
        
    }
}