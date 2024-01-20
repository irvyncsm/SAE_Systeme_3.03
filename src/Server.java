import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Représente un serveur de chat simple qui accepte les connexions des clients et gère la diffusion de messages.
 */
public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    /**
     * Initialise une nouvelle instance de Server.
     */
    public Server() {
        connections = new ArrayList<>();
        done = false;
    }

    /**
     * Exécute le serveur, accepte les connexions des clients et les gère avec un pool de threads.
     */
    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client, this);
                synchronized (connections) {
                    connections.add(handler);
                }
                pool.execute(handler);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Diffuse un message à tous les clients connectés, sauf l'expéditeur.
     *
     * @param senderName Le nom de l'expéditeur.
     * @param message    Le message à diffuser.
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
     * Envoie un message à des followers spécifiques de l'expéditeur.
     *
     * @param senderName    Le nom de l'expéditeur.
     * @param message       Le message à envoyer.
     * @param listeFollowers La liste des followers à qui envoyer le message.
     */
    public void postMessage(String senderName, String message, List<String> listeFollowers) {
        synchronized (connections) {
            for (ConnectionHandler handler : connections) {
                if (handler != null && handler.getName() != null && !handler.getName().equals(senderName)) {
                    if (listeFollowers.contains(handler.getName())) {
                        handler.sendMessage(message);
                        System.out.println(senderName + " a envoyé : " + message + " à " + handler.getName());
                    }
                }
            }
        }
    }

    /**
     * Arrête le serveur, ferme le socket du serveur et arrête tous les gestionnaires de connexion.
     */
    public void shutdown() {
        try {
            done = true;
            if (!server.isClosed()) {
                server.close();
            }
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
     * Supprime un gestionnaire de connexion de la liste des connexions actives.
     *
     * @param handler Le gestionnaire de connexion à supprimer.
     */
    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    /**
     * Obtient la liste des gestionnaires de connexion actifs.
     *
     * @return La liste des gestionnaires de connexion.
     */
    public ArrayList<ConnectionHandler> getConnections() {
        return connections;
    }

    /**
     * La méthode principale pour démarrer le serveur.
     *
     * @param args Arguments en ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
