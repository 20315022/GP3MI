package sim.p25.group3.car;
/**
 * Serveur Messagerie instantanée.
 * Appuyez simultanément sur  Ctrl + C pour terminer le programme.
 *
 * @author group3.p25.sim
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class TCPChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public TCPChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

           System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java TCPChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        TCPChatServer server = new TCPChatServer(port);
        server.execute();
    }

    /**
     * Delivre un message d'un utilisateur à d'autres (diffusion)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stocke le nom d'utilisateur du client nouvellement connecté.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * Lorsqu'un client est déconnecté, supprime le nom d'utilisateur et le fil utilisateur associés
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Renvoie vrai s'il y a d'autres utilisateurs connectés (ne compte pas l'utilisateur actuellement connecté)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
