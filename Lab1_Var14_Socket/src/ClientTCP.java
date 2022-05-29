import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) {

        PrintStream out;
        BufferedReader in;
        BufferedReader stdin;

        try {
            Socket clientSocket = new Socket("localhost", 8080);
            out = new PrintStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdin = new BufferedReader(new InputStreamReader(System.in));

            String str;

            System.out.print("Enter integer number -> ");
            while((str = stdin.readLine()).length() != 0) {
                out.println(str);
                System.out.println(in.readLine());
                System.out.print("Enter integer number -> ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
