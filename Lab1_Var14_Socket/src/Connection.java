import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class Connection extends Thread{
    protected Socket netClient;
    protected BufferedReader fromClient;
    protected PrintStream toClient;

    public Connection(Socket client) {
        netClient = client;
        try {
            fromClient = new BufferedReader(new InputStreamReader(netClient.getInputStream()));
            toClient = new PrintStream(netClient.getOutputStream());
        } catch (IOException e) {
            try {
                netClient.close();
            } catch (IOException e1) {
                System.err.println("Unable to set up streams " + e1);
            }
        }
        this.start();
    }

    @Override
    public void run() {
        String clientMessage;
        Eratosfen eratosfen;

        try {
            while(true) {
                clientMessage = fromClient.readLine();
                if (clientMessage.length() == 0)
                    break;
                try {
                    int n = Integer.parseInt(clientMessage);
                    eratosfen = new Eratosfen(n);
                    List<Integer> res = eratosfen.getPrimes().stream().filter(e -> n % e == 0).collect(Collectors.toList());
                    if (res.isEmpty()){
                        toClient.println("No one prime divider found!");
                    } else {
                        toClient.println("Prime dividers: " + res.toString());
                    }

                } catch (NumberFormatException nfe){
                    toClient.println("Wrong input data");
                }
            }
        } catch (IOException ignored){}
        finally {
            try {
                netClient.close();
            } catch (IOException ignored) {}
        }
    }
}
