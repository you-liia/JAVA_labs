import java.net.ServerSocket;
import java.net.Socket;


public class ServerTCP extends Thread{
    ServerSocket serverSocket;

    public ServerTCP() {
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server is started");
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true){
                Socket clientSocket = serverSocket.accept();
                Connection con = new Connection(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerTCP();
    }
}
