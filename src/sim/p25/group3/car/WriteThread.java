package sim.p25.group3.car;
/**
 * Ce fil est responsable de la lecture de l'entrée de l'utilisateur et de son envoi
 * au serveur.
 * Il s'exécute dans une boucle infinie jusqu'à ce que l'utilisateur tape « bye » pour quitter.
 *
 * @author group3.p25.sim
 */
import java.io.*;
import java.net.*;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private TCPChatClient client;

    public WriteThread(Socket socket, TCPChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        Console console = System.console();

        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do {
            text = console.readLine("[" + userName + "]: ");
            writer.println(text);

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}