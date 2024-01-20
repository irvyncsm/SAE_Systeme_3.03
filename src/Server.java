import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * La classe Server représente le serveur de chat qui gère les connexions des clients.
 * Chaque instance de cette classe est capable de traiter plusieurs connexions simultanées.
 */
public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections; // Liste des gestionnaires de connexion des clients
    private ServerSocket server; // Le socket du serveur
    private boolean done; // Indicateur pour arrêter le serveur
    private ExecutorService pool; // Pool d'exécution pour gérer les threads des clients

    /**
     * Constructeur de la classe Server.
     * Initialise la liste des connexions et l'indicateur done.
     */
    public Server() {
        connections = new ArrayList<>();
        done = false;
    }

    /**
     * Méthode exécutée lorsqu'un thread Server est démarré.
     * Gère les connexions entrantes et crée un gestionnaire de connexion pour chaque client.
     */
    @Override
    public void run() {
        try {
            // Création du socket du serveur et du pool d'exécution
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();

            // Boucle d'attente des connexions entrantes
            while (!done) {
                // Acceptation d'une nouvelle connexion
                Socket client = server.accept();

                // Création d'un gestionnaire de connexion pour le nouveau client
                ConnectionHandler handler = new ConnectionHandler(client, this);

                // Ajout du gestionnaire de connexion à la liste des connexions
                synchronized (connections) {
                    connections.add(handler);
                }

                // Exécution du gestionnaire de connexion dans le pool
                pool.execute(handler);
            }
        } catch (IOException e) {
            // Gestion de l'arrêt inattendu du serveur
            shutdown();
        }
    }

    /**
     * Envoie un message à tous les clients connectés, à l'exception de l'expéditeur.
     * @param senderName Le nom de l'expéditeur.
     * @param message Le message à diffuser.
     */
    public void broadcast(String senderName, String message) {
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    handler.sendMessage(message);
                    System.out.println(senderName + " a envoyé : " + message + " à " + handler.getName());
                }
            }
        }
    }

    /**
     * Arrête proprement le serveur en fermant le socket du serveur et en notifiant tous les gestionnaires de connexion.
     */
    public void shutdown() {
        try {
            done = true;

            // Fermeture du socket du serveur
            if (!server.isClosed()) {
                server.close();
            }

            // Notification de tous les gestionnaires de connexion pour qu'ils se ferment
            synchronized (connections) {
                for (ConnectionHandler handler : connections) {
                    handler.shutdown();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un gestionnaire de connexion de la liste des connexions.
     * @param handler Le gestionnaire de connexion à supprimer.
     */
    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    /**
     * Retourne la liste des gestionnaires de connexion.
     * @return La liste des gestionnaires de connexion.
     */
    public ArrayList<ConnectionHandler> getConnections() {
        return connections;
    }

    /**
     * Méthode principale pour démarrer le serveur.
     * @param args Les arguments de la ligne de commande (non utilisés dans cette implémentation).
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
