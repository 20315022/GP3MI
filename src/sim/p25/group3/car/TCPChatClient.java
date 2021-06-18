package sim.p25.group3.car;
/**
 * C'est le programme client de chat.
 * Tapez 'bye' pour terminer le programme.
 *
 * @author group3.p25.sim
 */
import java.net.*;
import java.io.*;

public class TCPChatClient {
    private String hostname;
    private int port;
    private String userName;

    public TCPChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        TCPChatClient client = new TCPChatClient(hostname, port);
        client.execute();
    }
}
