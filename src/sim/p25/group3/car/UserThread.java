package sim.p25.group3.car;
/**
 * Ce thread gère la connexion pour chaque client connecté, donc le serveur
 * peut gérer plusieurs clients en même temps.
 *
 * @author group3.p25.sim
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class UserThread extends Thread {
    private Socket socket;
    private TCPChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, TCPChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Envoie une liste d'utilisateurs en ligne à l'utilisateur nouvellement connecté.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Envoie un message au client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}