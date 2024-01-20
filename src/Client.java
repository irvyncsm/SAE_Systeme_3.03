import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Représente un client qui se connecte à un serveur pour la messagerie.
 */
public class Client {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ChatApplication application;
    private boolean connected;
    private boolean isFirstConnection;
    private User user;

    /**
     * Constructeur de la classe Client.
     *
     * @param application L'application de chat associée à ce client.
     */
    public Client(ChatApplication application) {
        this.application = application;
        connected = false;
        isFirstConnection = true;
        new Thread(this::connectToServer).start();
    }

    /**
     * Méthode privée pour établir la connexion avec le serveur.
     */
    private void connectToServer() {
        while (!connected) {
            try {
                client = new Socket("localhost", 9999);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);

                new Thread(this::receiveMessages).start();
                System.out.println("Connecté au serveur.");
                this.application.setStatut("Connecté au serveur.");
                this.application.setTextFieldDisabled(false);
                connected = true;
            } catch (IOException e) {
                System.out.println("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");
                this.application.setStatut("Impossible de se connecter au serveur. Nouvelle tentative dans 5 secondes.");
                this.application.clearChatArea();
                this.application.setTextFieldDisabled(true);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    /**
     * Méthode privée pour recevoir les messages du serveur.
     */
    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                application.traiterMessage(message);
            }
        } catch (IOException e) {
            shutdown();
            connected = false;
            new Thread(this::connectToServer).start();
        }
    }

    /**
     * Envoie un message au serveur.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Traite le message utilisateur, extrait le contenu et le transmet à l'application.
     *
     * @param message Le message utilisateur.
     */
    public void handleUserMessage(String message) {
        String content = message.substring(message.indexOf("content") + 10, message.indexOf("date") - 3);
        content = content.substring(1, content.length() - 1);
        if (isFirstConnection) {
            user = new User(content);
            isFirstConnection = false;
        }
        application.traiterMessage("Vous: " + content);
        sendMessage(message);
    }

    /**
     * Ferme proprement les flux et la connexion du client.
     */
    public void shutdown() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient l'utilisateur associé à ce client.
     *
     * @return L'utilisateur associé à ce client.
     */
    public User getUser() {
        return user;
    }

    /**
     * Interface pour la gestion des messages.
     */
    interface MessageHandler {
        void handleMessage(String message);
    }
}
