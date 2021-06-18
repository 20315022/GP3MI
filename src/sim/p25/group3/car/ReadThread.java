package sim.p25.group3.car;
/**
 * Ce thread est responsable de la lecture de l'entrée du serveur et de son impression
 * à la console.
 * Il s'exécute dans une boucle infinie jusqu'à ce que le client se déconnecte du serveur.
 *
 * @author group3.p25.sim
 */
import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private TCPChatClient client;

    public ReadThread(Socket socket, TCPChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);

                // imprime le nom d'utilisateur après avoir affiché le message du serveur
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
